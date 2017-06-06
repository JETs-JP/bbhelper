package com.oracle.poco.bbhelper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource {

    private final String name;
    private final String resourceId;
    private final String calendarId;
    private final String location;
    private final int capacity;
    private final String link;
    private final Facility facility;

    @JsonCreator
    public Resource(@JsonProperty("name") String name,
                    @JsonProperty("resource_id") String resourceId,
                    @JsonProperty("calendar_id") String calendarId,
                    @JsonProperty("location") String location,
                    @JsonProperty("capacity") int capacity, @JsonProperty("link") String link,
                    @JsonProperty("facility") Facility facility) {
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
