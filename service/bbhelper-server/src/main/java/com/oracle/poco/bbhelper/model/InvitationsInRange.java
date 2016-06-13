package com.oracle.poco.bbhelper.model;

import java.time.ZonedDateTime;
import java.util.Collection;

public class InvitationsInRange {

    ZonedDateTime fromdate;
    ZonedDateTime todate;
    Collection<Resource> resources;

    public InvitationsInRange(ZonedDateTime fromdate, ZonedDateTime todate,
            Collection<Resource> resources) {
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

    public Collection<Resource> getResources() {
        return resources;
    }

}
