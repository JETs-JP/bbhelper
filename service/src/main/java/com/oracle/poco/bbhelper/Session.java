package com.oracle.poco.bbhelper;

import com.fasterxml.jackson.databind.JsonNode;
import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jException;
import com.oracle.poco.bbhelper.exception.BbhelperBeehive4jParallelInvocationException;
import com.oracle.poco.bbhelper.exception.BbhelperBeehiveContextExpiredException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.log.*;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Person;
import com.oracle.poco.bbhelper.model.Resource;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.*;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.BeehiveApiFaultException;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.exception.BeehiveUnauthorizedException;
import jp.gr.java_conf.hhayakawa_jp.beehive4j.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * このアプリケーションのセッションの実態。
 * beehive4jのコンテキストの取得に成功したら、このオブジェクトのインスタンスを
 * 生成し、このアプリケーション固有のセッションIDに紐づけてメモリに保存する。
 * Beehiveに対する操作はこのオブジェクトの責務。
 *
 * TODO スレッドセーフな実装になっていることを確認する
 */
class Session {

    private static final BbhelperLogger logger = BbhelperLogger.getLogger(Session.class);

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
    List<Invitation> listConflictedInvitations(
            ZonedDateTime start, ZonedDateTime end, FloorCategory floorCategory)
                    throws BbhelperException {
        List<String> calendar_ids = resourceCache.getCalendarIds(floorCategory);
        List<String> invitation_ids = new ArrayList<>();
        List<BbhelperException> exceptions1 = new ArrayList<>();
        calendar_ids.stream().parallel().forEach(c -> {
            try {
                InvtListByRangeInvoker invoker =
                        context.getInvoker(BeehiveApiDefinitions.TYPEDEF_INVT_LIST_BY_RANGE);
                CalendarRange range = new CalendarRange.Builder()
                        .beeId(new BeeId.Builder().id(c).build())
                        .start(start)
                        .end(end)
                        .build();
                invoker.setRequestPayload(range);
                long begin = System.currentTimeMillis();
                ResponseEntity<BeehiveResponse> response = invoker.invoke();
                long finish = System.currentTimeMillis();
                // beehive4jがExceptionを上げないときは、正常な結果が返ったときとみなしてよい
                logger.info(new Beehive4jInvocationMessage(
                        Result.SUCCESS, invoker.getClass().getName(), finish - begin));
                BeehiveResponse body = response.getBody();
                if (response != null && body != null) {
                    Iterable<JsonNode> elements = body.getJson().get("elements");
                    if (elements != null) {
                        for (JsonNode element : elements) {
                            invitation_ids.add(getNodeAsText(element, "collabId", "id"));
                        }
                    }
                }
            } catch (BeehiveApiFaultException e) {
                exceptions1.add(handleBeehiveApiFaultException(Operation.INVOKE_BEEHIVE4J, e, logger));
            }
        });
        if (exceptions1.size() > 0) {
            throw new BbhelperBeehive4jParallelInvocationException(exceptions1);
        }
        if (invitation_ids.size() == 0) {
            return null;
        }

        List<List<BeeId>> beeIdChunks = new ArrayList<>(1);
        List<BeeId> beeIds = null;
        for (int i = 0; i < invitation_ids.size(); i++) {
            if (i % 100 == 0) {
                beeIds = new ArrayList<>();
                beeIdChunks.add(beeIds);
            }
            // beeIds must be initialized when i == 0.
            beeIds.add(new BeeId.Builder().id(invitation_ids.get(i)).build());
        }
        List<Invitation> invitations = new ArrayList<>();
        List<BbhelperException> exceptions2 = new ArrayList<>();
        beeIdChunks.stream().parallel().forEach(b -> {
            try {
                InvtReadBatchInvoker invoker =
                        context.getInvoker(BeehiveApiDefinitions.TYPEDEF_INVT_READ_BATCH);
                BeeIdList beeIdList = new BeeIdList(b);
                invoker.setRequestPayload(beeIdList);
                long begin = System.currentTimeMillis();
                ResponseEntity<BeehiveResponse> response = invoker.invoke();
                long finish = System.currentTimeMillis();
                // beehive4jがExceptionを上げないときは、正常な結果が返ったときとみなしてよい
                logger.info(new Beehive4jInvocationMessage(
                        Result.SUCCESS, invoker.getClass().getName(), finish - begin));
                BeehiveResponse body = response.getBody();
                if (body != null) {
                    invitations.addAll(parseInvtReadBatchResult(body.getJson()));
                }
            } catch (BeehiveApiFaultException e) {
                exceptions2.add(handleBeehiveApiFaultException(Operation.INVOKE_BEEHIVE4J, e, logger));
            }
        });
        if (exceptions2.size() > 0) {
            throw new BbhelperBeehive4jParallelInvocationException(exceptions2);
        }
        return invitations;
    }

    private List<Invitation> parseInvtReadBatchResult(JsonNode node) {
        Iterable<JsonNode> elements = node.get("elements");
        if (elements == null) {
            return null;
        }
        List<Invitation> retval = new ArrayList<>();
        for (JsonNode element : elements) {
            retval.add(parseInvtReadResponse(element));
        }
        return retval;
    }

    /**
     * 会議を登録する
     *
     * @param committer 登録する会議の情報を保持するオブジェクト
     * @return 登録された会議を表すInvitationオブジェクト
     * @throws BbhelperException Beehive APIの呼び出しに失敗した場合
     */
    Invitation createInvitation(InvitationCommitter committer) throws BbhelperException {
        if (committer == null) {
            return null;
        }
        if (calendar_id == null || calendar_id.length() == 0) {
            calendar_id = getDefaultCalendar();
        }
        // BeeId of user's calendar
        BeeId calendar = new BeeId.Builder().id(calendar_id).build();
        // MeetingUpdater
        Resource resource = resourceCache.getResource(committer.getResourceId());
        List<MeetingParticipantUpdater> participantUpdaters = new ArrayList<>(1);
        BeeId resourceId = new BeeId.Builder()
                .id(resource.getResourceId())
                .build();
        participantUpdaters.add(new MeetingParticipantUpdater.Builder()
                .operation(MeetingParticipantUpdaterOperation.ADD)
                .beeId(resourceId)
                .build());
        MeetingUpdater meetingUpdater = new MeetingUpdater.Builder()
                .name(committer.getName())
                .start(committer.getStart())
                .end(committer.getEnd())
                .status(OccurrenceStatus.TENTATIVE)
                .participantUpdaters(participantUpdaters)
                .locationName(resource.getName())
                .includeOnlineConference(false)
                .inviteeParticipantStatus(OccurrenceParticipantStatus.ACCEPTED)
                .inviteePriority(Priority.MEDIUM)
                .inviteeTransparency(Transparency.TRANSPARENT)
                .build();
        MeetingCreator meetingCreator = new MeetingCreator.Builder()
                .calendar(calendar)
                .meetingUpdater(meetingUpdater)
                .type(OccurrenceType.MEETING)
                .build();
        InvtCreateInvoker invtCreateInvoker =
                context.getInvoker(BeehiveApiDefinitions.TYPEDEF_INVT_CREATE);
        invtCreateInvoker.setRequestPayload(meetingCreator);
        ResponseEntity<BeehiveResponse> invtCreateResponse;
        try {
            long begin = System.currentTimeMillis();
            invtCreateResponse = invtCreateInvoker.invoke();
            long finish = System.currentTimeMillis();
            // beehive4jがExceptionを上げないときは、正常な結果が返ったときとみなしてよい
            logger.info(new Beehive4jInvocationMessage(
                    Result.SUCCESS, invtCreateInvoker.getClass().getName(), finish - begin));
        } catch (BeehiveApiFaultException e) {
            throw handleBeehiveApiFaultException(Operation.INVOKE_BEEHIVE4J, e, logger);
        }
        String invitation_id = getNodeAsText(
                invtCreateResponse.getBody().getJson(), "collabId", "id");
        return getInvitation(invitation_id);
    }

    private String getDefaultCalendar() throws BbhelperException {
        MyWorkspaceInvoker invoker = context.getInvoker(BeehiveApiDefinitions.TYPEDEF_MY_WORKSPACE);
        ResponseEntity<BeehiveResponse> response;
        try {
            long begin = System.currentTimeMillis();
            response = invoker.invoke();
            long finish = System.currentTimeMillis();
            // beehive4jがExceptionを上げないときは、正常な結果が返ったときとみなしてよい
            logger.info(new Beehive4jInvocationMessage(
                    Result.SUCCESS, invoker.getClass().getName(), finish - begin));
        } catch (BeehiveApiFaultException e) {
            throw handleBeehiveApiFaultException(Operation.INVOKE_BEEHIVE4J, e, logger);
        }
        BeehiveResponse body = response.getBody();
        if (body == null) {
            return null;
        }
        return getNodeAsText(body.getJson(), "defaultCalendar", "collabId", "id");
    }

    /**
     * 指定したIDの会議の情報を取得する
     *
     * @param invitation_id 会議ID
     * @return 会議IDで指定した会議の情報
     * @throws BbhelperException Beehive APIの呼出しに失敗した場合
     */
    Invitation getInvitation(String invitation_id) throws BbhelperException {
        InvtReadInvoker invtReadInvoker =
                context.getInvoker(BeehiveApiDefinitions.TYPEDEF_INVT_READ);
        invtReadInvoker.setPathValue(invitation_id);
        ResponseEntity<BeehiveResponse> invtReadResponse;
        try {
            long begin = System.currentTimeMillis();
            invtReadResponse = invtReadInvoker.invoke();
            long finish = System.currentTimeMillis();
            // beehive4jがExceptionを上げないときは、正常な結果が返ったときとみなしてよい
            logger.info(new Beehive4jInvocationMessage(
                    Result.SUCCESS, invtReadInvoker.getClass().getName(), finish - begin));
        } catch (BeehiveApiFaultException e) {
            throw handleBeehiveApiFaultException(Operation.INVOKE_BEEHIVE4J, e, logger);
        }
        return parseInvtReadResponse(invtReadResponse.getBody().getJson());
    }

    private Invitation parseInvtReadResponse(JsonNode node) {
        if (!"meeting".equals(getNodeAsText(node, "beeType"))) {
            throw new IllegalStateException("Beehive returned an unexpected data type object.");
        }
        String organizerName = getNodeAsText(node, "organizer", "name");
        String organizerAddress = getNodeAsText(node, "organizer", "address");
        Person organizer;
        if (organizerName == null || organizerName.isEmpty()) {
            organizer = null;
        } else {
            organizer = new Person(organizerName, organizerAddress);
        }
        Iterator<JsonNode> invteeIterator = node.get("participants").iterator();
        String resourceId = null;
        while (invteeIterator.hasNext()) {
            JsonNode n = invteeIterator.next();
            String beeType = getNodeAsText(n, "participant", "beeType");
            if ("bookableResource".equals(beeType)) {
                resourceId = getNodeAsText(n, "participant", "collabId", "id");
            }
        }
        Invitation invitation = new Invitation(
                node.get("name").asText(),
                node.get("collabId").get("id").asText(),
                resourceId,
                organizer,
                ZonedDateTime.parse(
                        node.get("start").asText(),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                ZonedDateTime.parse(
                        node.get("end").asText(),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return invitation;
    }

    private String getNodeAsText(JsonNode node, String... names) {
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

    /**
     * 指定したIDの会議の情報を削除する
     *
     * @param invitation_id 会議ID
     * @throws BbhelperException Beehive APIの呼出しに失敗した場合
     */
    void deleteInvitation(String invitation_id) throws BbhelperException {
        InvtDeleteInvoker invtDeleteInvoker =
                context.getInvoker(BeehiveApiDefinitions.TYPEDEF_INVT_DELETE);
        invtDeleteInvoker.setPathValue(invitation_id);
        ResponseEntity<BeehiveResponse> invtDeleteResponse;
        try {
            long begin = System.currentTimeMillis();
            invtDeleteResponse = invtDeleteInvoker.invoke();
            long finish = System.currentTimeMillis();
            // beehive4jがExceptionを上げないときは、正常な結果が返ったときとみなしてよい
            logger.info(new Beehive4jInvocationMessage(
                    Result.SUCCESS, invtDeleteInvoker.getClass().getName(), finish - begin));
        } catch (BeehiveApiFaultException e) {
            throw handleBeehiveApiFaultException(Operation.INVOKE_BEEHIVE4J, e, logger);
        }
    }

    private static BbhelperException handleBeehiveApiFaultException(
            Operation operation, BeehiveApiFaultException e, BbhelperLogger logger) {
        BbhelperException bbhe;
        if (e instanceof BeehiveUnauthorizedException) {
            bbhe = new BbhelperBeehiveContextExpiredException(e);
            logger.info(new ErrorMessage(operation, bbhe));
        } else {
            bbhe = new BbhelperBeehive4jException(e);
            logger.error(new ErrorMessage(operation, bbhe));
        }
        return bbhe;
    }

}
