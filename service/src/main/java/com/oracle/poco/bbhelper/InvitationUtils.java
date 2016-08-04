package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperUnauthorizedException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Person;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveApiDefinitions;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveResponse;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtListByRangeInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtReadBatchInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.BeehiveApiFaultException;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.BeeId;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.BeeIdList;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.CalendarRange;

public class InvitationUtils {

    static Collection<Invitation> listConflictedInvitaitons(
            ZonedDateTime start, ZonedDateTime end,
            FloorCategory floorCategory, TimeoutManagedContext context)
                    throws BbhelperException {
        List<String> calendar_ids =
                ResourceCache.getInstance().getCalendarIds(floorCategory);
        List<String> invitation_ids = new ArrayList<String>();
        List<BbhelperException> bbhe = new ArrayList<BbhelperException>();
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
                bbhe.add(e);
            } catch (BeehiveApiFaultException e) {
                if (HttpStatus.UNAUTHORIZED.equals(e.getHttpStatus())) {
                    bbhe.add(new BbhelperUnauthorizedException(
                            ErrorDescription.UNAUTORIZED, e));
                } else {
                    bbhe.add(new BbhelperBeehive4jException(
                            ErrorDescription.BEEHIVE4J_FAULT, e));
                }

            }
        });
        if (bbhe.size() > 0) { 
            throw bbhe.get(0);
        }

        if (invitation_ids.size() == 0) {
            return null;
        }
        List<BeeId> beeIds = new ArrayList<BeeId>(invitation_ids.size());
        invitation_ids.stream().forEach(i -> {
            beeIds.add(new BeeId(i, null));
        });

        BeeIdList beeIdList = new BeeIdList(beeIds);
        ResponseEntity<BeehiveResponse> response = null;
        try {
            InvtReadBatchInvoker invoker = context.getInvoker(
                    BeehiveApiDefinitions.TYPEDEF_INVT_READ_BATCH);
            invoker.setRequestPayload(beeIdList);
            response = invoker.invoke();
        } catch (BeehiveApiFaultException e) {
            BbhelperException be = null;
            if (HttpStatus.UNAUTHORIZED.equals(e.getHttpStatus())) {
                be = new BbhelperUnauthorizedException(
                        ErrorDescription.UNAUTORIZED, e);
            } else {
                be = new BbhelperBeehive4jException(
                        ErrorDescription.BEEHIVE4J_FAULT, e);
            }
            throw be;
        }
        BeehiveResponse body = response.getBody();
        if (body == null) {
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
