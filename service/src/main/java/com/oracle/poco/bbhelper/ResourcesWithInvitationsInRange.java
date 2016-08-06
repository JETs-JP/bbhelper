package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperInternalServerErrorException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.model.Invitation;

import com.oracle.poco.bbhelper.model.Resource;

public class ResourcesWithInvitationsInRange {

    private final ZonedDateTime fromdate;
    private final ZonedDateTime todate;

    private Map<String, ResourceWithInvitations> resources =
            new HashMap<String, ResourceWithInvitations>();

    // TODO このオブジェクトはファクトリーから取得するようにする
    public ResourcesWithInvitationsInRange(
            ZonedDateTime fromdate, ZonedDateTime todate) {
        super();
        if (fromdate == null || todate == null) {
            throw new NullPointerException();
        }
        this.fromdate = fromdate;
        this.todate = todate;
    }

    public ZonedDateTime getFromdate() {
        return fromdate;
    }

    public ZonedDateTime getTodate() {
        return todate;
    }

    public Collection<ResourceWithInvitations> getResources() {
        return resources.values();
    }

    public void addInvitation(Invitation invitation) throws BbhelperException {
        String resource_id = invitation.getResource_id();
        if (resource_id == null || resource_id.length() == 0) {
            return;
        }
        ZonedDateTime start = invitation.getStart();
        if (start.compareTo(todate) >= 0 || start.isEqual(todate)) {
            throw new BbhelperInternalServerErrorException(
                    ErrorDescription.INVITATION_OUT_OF_RANGE);
        }
        ZonedDateTime end = invitation.getEnd();
        if (end.compareTo(fromdate) <= 0 || end.isEqual(fromdate)) {
            throw new BbhelperInternalServerErrorException(
                    ErrorDescription.INVITATION_OUT_OF_RANGE);
        }

        ResourceWithInvitations resource = resources.get(resource_id);
        if (resource == null) {
            resource = new ResourceWithInvitations(
                    ResourceCache.getInstance().getResource(resource_id),
                    new ArrayList<Invitation>());
            resources.put(resource_id, resource);
        }
        resource.getInvitations().add(invitation);
    }

    public final class ResourceWithInvitations extends Resource {

        private final List<Invitation> invitations;

        private ResourceWithInvitations(Resource resource,
                List<Invitation> invitations) {
            super(resource.getName(), resource.getResource_id(),
                    resource.getCalendar_id(), resource.getLocation(),
                    resource.getCapacity(), resource.getLink(),
                    resource.getFacility());
            this.invitations = invitations;
        }

        public final List<Invitation> getInvitations() {
            return invitations;
        }
    }

}
