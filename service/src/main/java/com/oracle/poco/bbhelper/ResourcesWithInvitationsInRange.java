package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Resource;

// TODO Builderパターンを適用
public class ResourcesWithInvitationsInRange {
    /**
     * このオブジェクトが含むことができる会議の開始時刻
     */
    private final ZonedDateTime fromdate;
    /**
     * このオブジェクトが含むことができる会議の終了時刻
     */
    private final ZonedDateTime todate;
    /**
     * 予約済み会議情報付きの会議室情報の一覧
     */
    private final Map<String, ResourceWithInvitations> resourcesWithInvitations = new HashMap<>();

    /**
     * コンストラクタ
     * 
     * @param fromdate  このオブジェクトが含むことができる会議の開始時刻
     * @param todate    このオブジェクトが含むことができる会議の終了時刻
     * @param resources このオブジェクトが含むことができる会議室
     */
    public ResourcesWithInvitationsInRange(
            ZonedDateTime fromdate, ZonedDateTime todate, Collection<Resource> resources) {
        super();
        if (fromdate == null || todate == null) {
            throw new NullPointerException("Date range is not set.");
        }
        if (fromdate.compareTo(todate) >= 0) {
            throw new IllegalArgumentException("The fromdate is later than the todate.");
        }
        if (resources == null) {
            throw new NullPointerException("No resources specified.");
        }
        if (resources.isEmpty()) {
            throw new IllegalArgumentException("No resources specified.");
        }
        this.fromdate = fromdate;
        this.todate = todate;
        for (Resource resource : resources) {
            resourcesWithInvitations.put(resource.getResourceId(),
                    new ResourceWithInvitations(resource));
        }
    }

    /**
     * このオブジェクトが含むことができる会議の開始時刻を返却する
     * 
     * @return このオブジェクトが含むことができる会議の開始時刻
     */
    public ZonedDateTime getFromdate() {
        return fromdate;
    }

    /**
     * このオブジェクトが含むことができる会議の終了時刻を返却する
     * 
     * @return このオブジェクトが含むことができる会議の終了時刻
     */
    public ZonedDateTime getTodate() {
        return todate;
    }

    /**
     * 予約済み会議情報付きの会議室情報のリストを返却する
     *
     * @return 予約済み会議情報付きの会議室情報のリスト
     */
    public Collection<ResourceWithInvitations> getResources() {
        return resourcesWithInvitations.values();
    }

    /**
     * 予約済み会議情報を追加する
     * 予約済み会議情報が、このオブジェクトが含むことができないものの場合、なにもせずに終了する
     *
     * @param invitation 予約済み会議室情報
     */
    public void addInvitation(Invitation invitation) throws BbhelperException {
        String resource_id = invitation.getResource_id();
        if (resource_id == null || resource_id.length() == 0) {
            throw new IllegalArgumentException("Specified invitation has no resource id.");
        }
        ZonedDateTime start = invitation.getStart();
        if (start.compareTo(todate) >= 0 || start.isEqual(todate)) {
            return;
        }
        ZonedDateTime end = invitation.getEnd();
        if (end.compareTo(fromdate) <= 0 || end.isEqual(fromdate)) {
            return;
        }
        ResourceWithInvitations resourceWithInvitations =
                resourcesWithInvitations.get(resource_id);
        if (resourceWithInvitations == null) {
            return;
        }
        resourceWithInvitations.getInvitations().add(invitation);
    }

    /**
     * 予約済み会議情報を含む会議室情報
     */
    public final class ResourceWithInvitations extends Resource {
        /**
         * 予約済み会議情報のセット
         */
        private final Set<Invitation> invitations = new HashSet<>();

        /**
         * コンストラクタ
         *
         * @param resource 会議室情報
         */
        private ResourceWithInvitations(Resource resource) {
            super(resource.getName(), resource.getResourceId(),
                    resource.getCalendarId(), resource.getLocation(),
                    resource.getCapacity(), resource.getLink(),
                    resource.getFacility());
        }

        /**
         * 予約済み会議情報のセットを取得する
         *
         * @return 予約済み会議情報のセット
         */
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public final Set<Invitation> getInvitations() {
            return invitations;
        }
    }

}
