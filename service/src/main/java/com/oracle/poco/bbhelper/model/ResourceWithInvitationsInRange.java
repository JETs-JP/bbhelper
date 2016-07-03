package com.oracle.poco.bbhelper.model;

import java.util.ArrayList;
import java.util.List;

public final class ResourceWithInvitationsInRange {

    private String name;
    private String resource_id;
    private String calendar_id;
    private String location;
    private int capacity;
    private String link;
    private Facility facility;
    private List<Invitation> invitations = new ArrayList<Invitation>();

    public ResourceWithInvitationsInRange() {
        super();
    }

    public ResourceWithInvitationsInRange(String name, String resource_id,
            String calendar_id, String location, int capacity, String link,
            Facility facility, List<Invitation> invitations) {
        super();
        this.name = name;
        this.resource_id = resource_id;
        this.calendar_id = calendar_id;
        this.location = location;
        this.capacity = capacity;
        this.link = link;
        this.facility = facility;
        this.invitations = invitations;
    }

    public static ResourceWithInvitationsInRange deepClone(
            ResourceWithInvitationsInRange origin) {
        if (origin == null) {
            return null;
        }
        return new ResourceWithInvitationsInRange(
                ((origin.getName() != null) ? origin.getName() : null),
                ((origin.getResource_id() != null) ? origin.getResource_id() : null),
                ((origin.getCalendar_id() != null) ? origin.getCalendar_id() : null),
                ((origin.getLocation() != null) ? origin.getLocation() : null),
                origin.getCapacity(),
                ((origin.getLink() != null) ? origin.getLink() : null),
                Facility.deepClone(origin.getFacility()),
                new ArrayList<Invitation>(origin.getInvitations()));
    }

    public String getName() {
        return name;
    }

    public String getResource_id() {
        return resource_id;
    }

    public String getCalendar_id() {
        return calendar_id;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLink() {
        return link;
    }

    public Facility getFacility() {
        return facility;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }

    @Override
    public String toString() {
        return "BookableResource [name=" + name + ", resource_id=" + resource_id + ", calendar_id=" + calendar_id
                + ", location=" + location + ", capacity=" + capacity + ", link=" + link + ", facility=" + facility
                + "]";
    }

}