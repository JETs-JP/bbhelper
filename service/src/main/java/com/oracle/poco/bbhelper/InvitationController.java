package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Person;
import com.oracle.poco.bbhelper.utilities.LoggerManager;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveApiDefinitions;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveResponse;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.Beehive4jException;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtListByRangeInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtReadBatchInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.BeeId;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.BeeIdList;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.CalendarRange;

@RestController
@RequestMapping("/invitations")
public class InvitationController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitation(@RequestBody Invitation invitation) {
        InvitationCache.getInstance().put(invitation);
        // TODO Implement
        return "request accepted.";
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitations(@RequestBody List<Invitation> invitations) {
        InvitationCache.getInstance().put(invitations);
        // TODO Implement
        return "request accepted.";
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Invitation> listConflictedInvitaitons(
            @RequestHeader(SessionPool.HEADER_KEY_BBH_AUTHORIZED_SESSION)
            String session_id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime end,
            HttpServletResponse httpResponse) {
        TimeoutManagedContext context =
                SessionPool.getInstance().get(session_id);
        List<String> invitation_ids = new ArrayList<String>();
        Set<String> calendar_ids = ResourceCache.getInstance().getAllCalendarIds();
        calendar_ids.stream().parallel().forEach(c -> {
            CalendarRange range =
                    new CalendarRange(new BeeId(c, null), start, end);
            try {
                InvtListByRangeInvoker invoker = context.getInvoker(
                        BeehiveApiDefinitions.TYPEDEF_INVT_LIST_BY_RANGE);
                invoker.setRequestPayload(range);
                ResponseEntity<BeehiveResponse> response = invoker.invoke();
                BeehiveResponse body = response.getBody();
                if (body != null) {
                    Iterable<JsonNode> elements = body.getJson().get("elements");
                    if (elements != null) {
                        for (JsonNode element : elements) {
                            invitation_ids.add(getNodeAsText(element, "collabId", "id"));
                        }
                    }
                }
            } catch (BbhelperException e) {
                System.out.println(e.getMessage());
                LoggerManager.getLogger().severe(e.getMessage());
                httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Beehive4jException e) {
                // TODO Auto-generated catch block
                System.out.println(e.getMessage());
            }
        });
        if (httpResponse.getStatus() ==
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            return null;
        }

        if (invitation_ids.size() == 0) {
            httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        }

        List<BeeId> beeIds = new ArrayList<BeeId>();
        invitation_ids.stream().parallel().forEach(i -> {
            beeIds.add(new BeeId(i, null));
        });
        BeeIdList beeIdList = new BeeIdList(beeIds);
        ResponseEntity<BeehiveResponse> response = null;
        try {
            InvtReadBatchInvoker invoker = context.getInvoker(
                    BeehiveApiDefinitions.TYPEDEF_INVT_READ_BATCH);
            invoker.setRequestPayload(beeIdList);
            response = invoker.invoke();
        } catch (BbhelperException e) {
            System.out.println(e.getMessage());
            LoggerManager.getLogger().severe(e.getMessage());
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Beehive4jException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
        BeehiveResponse body = response.getBody();
        if (body == null) {
            httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return null;
        }
        return parseInvtReadBatchResult(body.getJson());
    }

    private static List<Invitation> parseInvtReadBatchResult(JsonNode node) {
        Iterable<JsonNode> elements = node.get("elements");
        if (elements == null) {
            return null;
        }
        List<Invitation> retval = new ArrayList<Invitation>();
        for (JsonNode element : elements) {
            Person organizer = new Person(
                    getNodeAsText(element, "organizer", "name"),
                    getNodeAsText(element, "organizer", "address"),
                    null);
            Invitation invitation = new Invitation(
                    element.get("name").asText(),
                    element.get("collabId").get("id").asText(),
                    element.get("invitee").get("participant").get("collabId").get("id").asText(),
                    organizer,
                    ZonedDateTime.parse(
                            element.get("start").asText(),
                            DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    ZonedDateTime.parse(
                            element.get("end").asText(),
                            DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            retval.add(invitation);
        }
        return retval;
    }

    private static String getNodeAsText(JsonNode node, String... names) {
        if (node == null) {
            throw new NullPointerException();
        }
        if (names.length == 0) {
            return node.asText();
        }
        for (String name : names) {
            if ((node = node.get(name)) == null) {
                return null;
            }
        }
        return node.asText();
    }

}
