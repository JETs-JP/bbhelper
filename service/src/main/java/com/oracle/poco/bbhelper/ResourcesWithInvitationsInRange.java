package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperInternalServerErrorException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Resource;

public class ResourcesWithInvitationsInRange {

    /**
     * このオブジェクトが含むことができる会議の開始時刻
     */
    private final ZonedDateTime fromdate;
    /**
     * このオブジェクトが含むことができる会議の終了時刻
     */
    private final ZonedDateTime todate;

    private final Map<String, ResourceWithInvitations> resourcesWithInvitations =
            new HashMap<String, ResourceWithInvitations>();

    /**
     * コンストラクタ
     * 
     * @param fromdate このオブジェクトが含むことができる会議の開始時刻
     * @param todate このオブジェクトが含むことができる会議の終了時刻
     * @param floor
     */
    public ResourcesWithInvitationsInRange(
            ZonedDateTime fromdate, ZonedDateTime todate, FloorCategory floor) {
        super();
        if (fromdate == null || todate == null) {
            throw new NullPointerException("Date range is not set.");
        }
        if (fromdate.compareTo(todate) >= 0) {
            throw new IllegalArgumentException("The fromdate is later than the todate.");
        }
        this.fromdate = fromdate;
        this.todate = todate;
        Map<String, Resource> resources =
                ResourceCache.getInstance().getCache(floor);
        for (Entry<String, Resource> resource : resources.entrySet()) {
            resourcesWithInvitations.put(resource.getKey(),
                    new ResourceWithInvitations(resource.getValue()));
        }
    }

    /**
     * このオブジェクトが含むことができる会議の開始時刻を返却します
     * 
     * @return このオブジェクトが含むことができる会議の開始時刻
     */
    public ZonedDateTime getFromdate() {
        return fromdate;
    }

    /**
     * このオブジェクトが含むことができる会議の終了時刻を返却します
     * 
     * @return このオブジェクトが含むことができる会議の終了時刻
     */
    public ZonedDateTime getTodate() {
        return todate;
    }

    public Collection<ResourceWithInvitations> getResources() {
        return resourcesWithInvitations.values();
    }

    public void addInvitation(Invitation invitation) throws BbhelperException {
        String resource_id = invitation.getResource_id();
        if (resource_id == null || resource_id.length() == 0) {
            return;
        }
        ZonedDateTime start = invitation.getStart();
        if (start.compareTo(todate) >= 0 || start.isEqual(todate)) {
            throw new BbhelperInternalServerErrorException(
                    ErrorDescription.INVITATION_OUT_OF_RANGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ZonedDateTime end = invitation.getEnd();
        if (end.compareTo(fromdate) <= 0 || end.isEqual(fromdate)) {
            throw new BbhelperInternalServerErrorException(
                    ErrorDescription.INVITATION_OUT_OF_RANGE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResourceWithInvitations resourceWithInvitation =
                resourcesWithInvitations.get(resource_id);
        if (resourceWithInvitation == null) {
            throw new BbhelperInternalServerErrorException(
                    ErrorDescription.INVITATION_OUT_OF_FLOOR_CATEGORY,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        resourceWithInvitation.getInvitations().add(invitation);
    }

    private final class ResourceWithInvitations extends Resource {

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private final Set<Invitation> invitations = new HashSet<Invitation>();

        private ResourceWithInvitations(Resource resource) {
            super(resource.getName(), resource.getResource_id(),
                    resource.getCalendar_id(), resource.getLocation(),
                    resource.getCapacity(), resource.getLink(),
                    resource.getFacility());
        }

        public final Set<Invitation> getInvitations() {
            return invitations;
        }
    }

}
