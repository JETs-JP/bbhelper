package com.oracle.poco.bbhelper.core;

import java.time.ZonedDateTime;
import java.util.Collection;

public class ConflictedInvitations {

    ZonedDateTime fromdate;
    ZonedDateTime todate;
    Collection<BookableResource> resources;

    public ConflictedInvitations(ZonedDateTime fromdate, ZonedDateTime todate,
            Collection<BookableResource> resources) {
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

    public Collection<BookableResource> getResources() {
        return resources;
    }

}
