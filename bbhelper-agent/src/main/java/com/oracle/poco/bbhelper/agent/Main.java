package com.oracle.poco.bbhelper.agent;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.poco.bbhelper.core.BookableResource;
import com.oracle.poco.bbhelper.core.BookableResourceLoader;
import com.oracle.poco.bbhelper.core.Invitation;
import com.oracle.poco.bbhelper.core.Person;

public class Main {

//    private static final String HOST_BBHELPER =
//            "https://BBHelper-beta-gse00000627.apaas.em2.oraclecloud.com/";
    private static final String HOST_BBHELPER =
            "https://hhayakaw-jse-01-gse00000645.apaas.em2.oraclecloud.com/";
//    private static final String HOST_BBHELPER = "http://localhost:8080/";
    private static final String ENDPOINT_BBHELPER =
            HOST_BBHELPER + "invitations/list";

    private void execute() throws JsonProcessingException, IOException {
        Map<String, BookableResource> resources =
                BookableResourceLoader.getInstance().getBookableResources();
        BeehiveContext context = BeehiveContext.getBeehiveContext("hoge", "bar");
        Set<String> keys = resources.keySet();
        for (String key : keys) {
            BookableResource resource = resources.get(key);
            List<String> invitation_ids =
                    getInvitationIds(context, resource.getCalendar_id());
            List<Invitation> invitations =
                    getInvitaionsReadBatch(context, invitation_ids);
            populateInvitations(invitations);
        }
    }

    private static List<String> getInvitationIds(
            BeehiveContext context, String calendar_id)
                    throws JsonProcessingException, IOException {
        BeehiveInvoker invtListByRange =
                context.getInvoker(BeehiveApiDefinitions.INVT_LIST_BYRANGE);
        ZonedDateTime from = ZonedDateTime.now();
        ZonedDateTime to = ZonedDateTime.now().plusDays(7);
        CalendarRangePayload crp = new CalendarRangePayload(
                new BeeIdPayload(calendar_id),
                from.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                to.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        invtListByRange.setPayload(crp);
        JsonNode invtList = invtListByRange.invoke();
        
        return parseInvitaionIds(invtList);
    }

    private static List<String> parseInvitaionIds(JsonNode node) {
        Iterable<JsonNode> elements = node.get("elements");
        if (elements == null) {
            return null;
        }
        List<String> retval = new ArrayList<String>();
        for (JsonNode element : elements) {
            retval.add(element.get("collabId").get("id").asText());
        }
        return retval;
    }

    private static List<Invitation> getInvitaionsReadBatch(
            BeehiveContext context, List<String> invitation_ids)
                    throws JsonProcessingException, IOException {
        if (invitation_ids == null || invitation_ids.isEmpty()) {
            return null;
        }
        BeehiveInvoker invtReadBatch =
                context.getInvoker(BeehiveApiDefinitions.INVT_READ_BATCH);
        List<BeeIdPayload> beeIds = new ArrayList<BeeIdPayload>();
        for (String id : invitation_ids) {
            beeIds.add(new BeeIdPayload(id));
        }
        BeeIdListPayload blp = new BeeIdListPayload(beeIds);
        invtReadBatch.setPayload(blp);
        System.out.println(blp);
        JsonNode invtReadResult = invtReadBatch.invoke();

        List<Invitation> invitations = parseInvitaitonReadResult(invtReadResult);
        return invitations;
    }
    

    private static List<Invitation> parseInvitaitonReadResult(JsonNode node) {
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

    private static void populateInvitations(List<Invitation> invitations) {
        if (invitations == null || invitations.isEmpty()) {
            return;
        }
        HttpEntity<List<Invitation>> entity =
                new HttpEntity<List<Invitation>>(invitations);
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(
                    ENDPOINT_BBHELPER, HttpMethod.POST, entity, String.class);
        } catch (RestClientException e) {
            printThrowable(e);
        }
    }

    public static void main(String[] args) {
        Main instance = new Main();
        try {
            instance.execute();
        } catch (IOException e) {
            printThrowable(e);
            e.printStackTrace();
            Throwable c = e.getCause();
            if (c != null) {
                printThrowable(c);
                c.printStackTrace();
            }
        }
    }

    private static void printThrowable(Throwable t) {
        System.out.println(t.getMessage());
        t.printStackTrace();
        Throwable c = t.getCause();
        if (c != null) {
            System.out.println(c.getMessage());
            c.printStackTrace();
        }
    }

}
