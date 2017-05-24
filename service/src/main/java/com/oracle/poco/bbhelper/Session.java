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

import jp.gr.java_conf.hhayakawa_jp.beehive4j.BeehiveApiDefinitions;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.BeehiveContext;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.BeehiveResponse;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.InvtCreateInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.InvtListByRangeInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.InvtReadBatchInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.InvtReadInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.MyWorkspaceInvoker;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.BeehiveApiFaultException;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.BeeId;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.BeeIdList;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.CalendarRange;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.MeetingCreator;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.MeetingParticipantUpdater;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.MeetingParticipantUpdaterOperation;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.MeetingUpdater;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.OccurrenceParticipantStatus;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.OccurrenceStatus;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.OccurrenceType;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.Priority;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.Transparency;

/**
 * このアプリケーションのセッションの実態。
 * beehive4jのコンテキストの取得に成功したら、このオブジェクトのインスタンスを
 * 生成し、このアプリケーション固有のセッションIDに紐づけてメモリに保存する。
 * Beehiveに対する操作はこのオブジェクトの責務。
 *
 * TODO 全体的に、BeehiveContextの生き死にに合わせたハンドリングを実装する
 * TODO スレッドセーフな実装になっていることを確認する
 */
class Session {

    // TODO コンストラクタインジェクションに切り替えてfinalにする
    /**
     * 会議室の一般情報を保持するキャッシュ
     */
    @Autowired
    private ResourceCache resourceCache;
    /**
     * このアプリケーション全体のプロパティ
     */
    @Autowired
    private ApplicationProperties properties;

    /**
     * 最後にインスタンスが利用された時刻
     */
    private long lastUsed;
    /**
     * beehive4jのコンテキスト
     */
    private final BeehiveContext context;
    /**
     * ログイン中のユーザーのカレンダーID。beehiveのAPIで定義されているもの
     */
    private String calendar_id;

    /**
     * コンストラクタ
     *
     * @param context beehive4jのコンテキスト
     */
    Session(BeehiveContext context) {
        super();
        this.lastUsed = System.currentTimeMillis();
        // TODO: コンテキストが不正な値の場合を排除する
        this.context = context;
    }

    /**
     * セッションが有効（タイムアウトしていない）であることを確認する
     *
     * @return セッション有効（タイムアウトしていない）かどうかを表すフラグ
     */
    boolean isActive() {
        return (System.currentTimeMillis() - lastUsed) < properties.getSessionTimeout();
    }

    /**
     * インスタンスが最後に利用された時刻を更新する
     */
    void update() {
        lastUsed = System.currentTimeMillis();
    }

    /**
     * 指定した時間範囲にかぶる会議をリストする
     *
     * @param start 検索対象の時間範囲の起点
     * @param end   検索対象の時間範囲の終点
     * @param floorCategory 会議室のフロアの分類
     *
     * @return 指定した時間範囲にかぶる会議のCollection。一つも該当がない場合null
     * @throws BbhelperException Beehive APIの呼び出しに失敗した場合
     */
    Collection<Invitation> listConflictedInvitations(
            ZonedDateTime start, ZonedDateTime end, FloorCategory floorCategory)
                    throws BbhelperException {
        // TODO 入力値チェック
        List<String> calendar_ids = resourceCache.getCalendarIds(floorCategory);
        List<String> invitation_ids = new ArrayList<>();
        List<BbhelperException> bbhe = new ArrayList<>();
        // TODO ラムダ式内のエラーハンドリングを見直す
        calendar_ids.stream().parallel().forEach(c -> {
            CalendarRange range = new CalendarRange.Builder()
                    .beeId(new BeeId.Builder().id(c).build())
                    .start(start)
                    .end(end)
                    .build();
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
                // TODO エラーハンドリングを簡潔に書けるように工夫する
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
            beeIds.add(new BeeId.Builder().id(i).build());
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

    /**
     * 会議を登録する
     *
     * @param invitation 登録した会議を表すInvitationオブジェクト
     *
     * @return 登録された会議を表すInvitationオブジェクト
     * @throws BbhelperException Beehive APIの呼び出しに失敗した場合
     */
    Invitation createInvitaion(Invitation invitation) throws BbhelperException {
        if (calendar_id == null || calendar_id.length() == 0) {
            calendar_id = getDefaultCalendar();
        }
        // BeeId of user's calendar
        BeeId calendar = new BeeId.Builder().id(calendar_id).build();
        // MeetingUpdater
        Resource resource =
                resourceCache.getResource(invitation.getResource_id());
        List<MeetingParticipantUpdater> participantUpdaters = 
                new ArrayList<MeetingParticipantUpdater>(1);
        BeeId resourceId = new BeeId.Builder()
                .id(resource.getResourceId())
                .build();
        participantUpdaters.add(new MeetingParticipantUpdater.Builder()
                .operation(MeetingParticipantUpdaterOperation.ADD)
                .beeId(resourceId)
                .build());
        MeetingUpdater meetingUpdater = new MeetingUpdater.Builder()
                .name(invitation.getName())
                .start(invitation.getStart())
                .end(invitation.getEnd())
                .status(OccurrenceStatus.TENTATIVE)
                .participantUpdaters(participantUpdaters)
                .locationName(resource.getName())
                .includeOnlineConference(false)
                .inviteeParticipantStatus(OccurrenceParticipantStatus.ACCEPTED)
                .inviteePriority(Priority.MEDIUM)
                .inviteeTransparency(Transparency.TRANSPARENT)
                .build();
        MeetingCreator meetingCreater = new MeetingCreator.Builder()
                .calendar(calendar)
                .meetingUpdater(meetingUpdater)
                .type(OccurrenceType.MEETING)
                .build();
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
