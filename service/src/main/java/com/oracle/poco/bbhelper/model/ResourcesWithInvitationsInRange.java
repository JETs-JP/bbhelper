package com.oracle.poco.bbhelper.model;

import java.time.ZonedDateTime;
import java.util.Collection;

public class ResourcesWithInvitationsInRange {

    ZonedDateTime fromdate;
    ZonedDateTime todate;
    Collection<ResourceWithInvitations> resources;

    public ResourcesWithInvitationsInRange(
            ZonedDateTime fromdate, ZonedDateTime todate,
            Collection<ResourceWithInvitations> resources) {
        super();
        this.fromdate = fromdate;
        this.todate = todate;
        this.resources = resources;
    }

    public ZonedDateTime getFromdate() {
        return fromdate;
    }

    public ZonedDateTime getTodate() {
        return todate;
    }

    public Collection<ResourceWithInvitations> getResources() {
        return resources;
    }

}
