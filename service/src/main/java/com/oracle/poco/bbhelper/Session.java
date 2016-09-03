package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperUnauthorizedException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Person;
import com.oracle.poco.bbhelper.model.Resource;

import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveApiDefinitions;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.BeehiveResponse;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtCreateInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtListByRangeInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtReadBatchInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.InvtReadInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.MyWorkspaceInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.exception.BeehiveApiFaultException;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.BeeId;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.BeeIdList;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.CalendarRange;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.MeetingCreator;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.MeetingParticipantUpdater;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.MeetingParticipantUpdaterOperation;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.MeetingUpdater;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.OccurrenceParticipantStatus;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.OccurrenceStatus;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.OccurrenceType;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.Priority;
import jp.gr.java_conf.hhayakawa_jp.beehive_client.model.Transparency;

class Session {

    @Autowired
    private ResourceCache resourceCache;

    // TODO タイムアウト値はapplication.propertiesで設定できるようにする
    @Autowired
    private ApplicationProperties properties;

    private long lastUsed;

    private final BeehiveContext context;

    private String calendar_id = null;

    Session(BeehiveContext context) {
        super();
        this.lastUsed = System.currentTimeMillis();
        this.context = context;
    }

    boolean isActive() {
        return (System.currentTimeMillis() - lastUsed) < properties.getSessionTimeout();
    }

    private void update() {
        lastUsed = System.currentTimeMillis();
    }

    Collection<Invitation> listConflictedInvitaitons(
            ZonedDateTime start, ZonedDateTime end, FloorCategory floorCategory)
                    throws BbhelperException {
        // TODO update()が新規メソッドでも確実に実行されるような工夫が必要
        update();
        List<String> calendar_ids = resourceCache.getCalendarIds(floorCategory);
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
            } catch (BeehiveApiFaultException e) {
                if (HttpStatus.UNAUTHORIZED.equals(e.getHttpStatus())) {
                    bbhe.add(new BbhelperUnauthorizedException(
                            ErrorDescription.UNAUTORIZED, e, e.getHttpStatus()));
                } else {
                    bbhe.add(new BbhelperBeehive4jException(
                            ErrorDescription.BEEHIVE4J_FAULT, e, e.getHttpStatus()));
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
                        ErrorDescription.UNAUTORIZED, e, e.getHttpStatus());
            } else {
                be = new BbhelperBeehive4jException(
                        ErrorDescription.BEEHIVE4J_FAULT, e, e.getHttpStatus());
            }
            throw be;
        }
        BeehiveResponse body = response.getBody();
        if (body == null) {
            return null;
        }
        return parseInvtReadBatchResult(body.getJson());
    }

    private List<Invitation> parseInvtReadBatchResult(JsonNode node) {
        Iterable<JsonNode> elements = node.get("elements");
        if (elements == null) {
            return null;
        }
        List<Invitation> retval = new ArrayList<Invitation>();
        for (JsonNode element : elements) {
            retval.add(parseInvitationJsonNode(element));
        }
        return retval;
    }

    Invitation createInvitaion(Invitation invitation) throws BbhelperException {
        // TODO update()が新規メソッドでも確実に実行されるような工夫が必要
        update();
        if (calendar_id == null || calendar_id.length() == 0) {
            calendar_id = getDefaultCalendar();
        }
        // BeeId
        BeeId calendar = new BeeId(calendar_id, null);
        // MeetingUpdater
        Resource resource =
                resourceCache.getResource(invitation.getResource_id());
        List<MeetingParticipantUpdater> participantUpdaters = 
                new ArrayList<MeetingParticipantUpdater>(1);
        participantUpdaters.add(new MeetingParticipantUpdater(
                null,
                null,
                MeetingParticipantUpdaterOperation.ADD,
                new BeeId(resource.getResource_id(), null)));
        MeetingUpdater meetingUpdater = new MeetingUpdater(
                invitation.getName(),
                null,
                null,
                null,
                invitation.getEnd(),
                false,
                OccurrenceParticipantStatus.ACCEPTED,
                null,
                Priority.MEDIUM,
                Transparency.TRANSPARENT,
                resource.getName(),
                participantUpdaters,
                invitation.getStart(),
                OccurrenceStatus.TENTATIVE, null, null);
        // OccurenceType
        OccurrenceType type = OccurrenceType.MEETING;
        // Create MeetingCreator and invoke
        MeetingCreator meetingCreater = new MeetingCreator(
                calendar, meetingUpdater, type);
        InvtCreateInvoker invtCreateInvoker =
                context.getInvoker(BeehiveApiDefinitions.TYPEDEF_INVT_CREATE);
        invtCreateInvoker.setRequestPayload(meetingCreater);
        ResponseEntity<BeehiveResponse> invtCreateResponse = null;
        try {
            invtCreateResponse = invtCreateInvoker.invoke();
        } catch (BeehiveApiFaultException e) {
            BbhelperException be = null;
            if (HttpStatus.UNAUTHORIZED.equals(e.getHttpStatus())) {
                be = new BbhelperUnauthorizedException(
                        ErrorDescription.UNAUTORIZED, e, e.getHttpStatus());
            } else {
                be = new BbhelperBeehive4jException(
                        ErrorDescription.BEEHIVE4J_FAULT, e, e.getHttpStatus());
            }
            throw be;
        }
        String invitation_id = getNodeAsText(
                invtCreateResponse.getBody().getJson(), "collabId", "id");
        InvtReadInvoker invtReadInvoker =
                context.getInvoker(BeehiveApiDefinitions.TYPEDEF_INVT_READ);
        invtReadInvoker.setPathValue(invitation_id);
        ResponseEntity<BeehiveResponse> invtReadResponse = null;
        try {
            invtReadResponse = invtReadInvoker.invoke();
        } catch (BeehiveApiFaultException e) {
            BbhelperException be = null;
            if (HttpStatus.UNAUTHORIZED.equals(e.getHttpStatus())) {
                be = new BbhelperUnauthorizedException(
                        ErrorDescription.UNAUTORIZED, e, e.getHttpStatus());
            } else {
                be = new BbhelperBeehive4jException(
                        ErrorDescription.BEEHIVE4J_FAULT, e, e.getHttpStatus());
            }
            throw be;
        }
        return parseInvitationJsonNode(invtReadResponse.getBody().getJson());
    }

    private Invitation parseInvitationJsonNode(JsonNode node) {
        Person organizer = new Person(
                getNodeAsText(node, "organizer", "name"),
                getNodeAsText(node, "organizer", "address"));
        Invitation invitation = new Invitation(
                node.get("name").asText(),
                node.get("collabId").get("id").asText(),
                node.get("invitee").get("participant").get("collabId").get("id").asText(),
                organizer,
                ZonedDateTime.parse(
                        node.get("start").asText(),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                ZonedDateTime.parse(
                        node.get("end").asText(),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return invitation;
    }

    private String getDefaultCalendar() throws BbhelperException {
        MyWorkspaceInvoker invoker = context.getInvoker(
                BeehiveApiDefinitions.TYPEDEF_MY_WORKSPACE);
        ResponseEntity<BeehiveResponse> response = null;
        try {
            response = invoker.invoke();
        } catch (BeehiveApiFaultException e) {
            BbhelperException be = null;
            if (HttpStatus.UNAUTHORIZED.equals(e.getHttpStatus())) {
                be = new BbhelperUnauthorizedException(
                        ErrorDescription.UNAUTORIZED, e, e.getHttpStatus());
            } else {
                be = new BbhelperBeehive4jException(
                        ErrorDescription.BEEHIVE4J_FAULT, e, e.getHttpStatus());
            }
            throw be;
        }
        BeehiveResponse body = response.getBody();
        if (body == null) {
            return null;
        }
        return getNodeAsText(body.getJson(), "defaultCalendar", "collabId", "id");
    }

    private String getNodeAsText(JsonNode node, String... names) {
        if (node == null) {
            throw new NullPointerException();
        }
        if (names.length == 0) {
            return node.asText();
        }
        for (String name : names) {
            if (!node.has(name)) {
                throw new IllegalArgumentException(
                        "Json data and requird field names aren't consistent.");
            }
            if ((node = node.get(name)) == null) {
                return null;
            }
        }
        return node.asText();
    }

}
