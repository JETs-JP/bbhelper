package com.oracle.poco.bbhelper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource {

    private String name;
    @JsonProperty("resource_id")
    private String resourceId;
    @JsonProperty("calendar_id")
    private String calendarId;
    private String location;
    private int capacity;
    private String link;
    private Facility facility;

    public Resource() {
        super();
    }

    public Resource(String name, String resourceId, String calendarId,
                    String location, int capacity, String link, Facility facility) {
        super();
        this.name = name;
        this.resourceId = resourceId;
        this.calendarId = calendarId;
        this.location = location;
        this.capacity = capacity;
        this.link = link;
        this.facility = facility;
    }

    public final String getName() {
        return name;
    }

    public final String getResourceId() {
        return resourceId;
    }

    public final String getCalendarId() {
        return calendarId;
    }

    public final String getLocation() {
        return location;
    }

    public final int getCapacity() {
        return capacity;
    }

    public final String getLink() {
        return link;
    }

    public final Facility getFacility() {
        return facility;
    }

    @Override
    public String toString() {
        return "Resource [name=" + name + ", resource_id=" + resourceId + ", calendar_id=" + calendarId
                + ", location=" + location + ", capacity=" + capacity + ", link=" + link + ", facility=" + facility
                + "]";
    }

}
