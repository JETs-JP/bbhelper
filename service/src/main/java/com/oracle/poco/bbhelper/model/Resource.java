package com.oracle.poco.bbhelper.model;

public class Resource {

    private String name;
    private String resource_id;
    private String calendar_id;
    private String location;
    private int capacity;
    private String link;
    private Facility facility;

    public Resource() {
        super();
    }

    public Resource(String name, String resource_id, String calendar_id,
            String location, int capacity, String link, Facility facility) {
        super();
        this.name = name;
        this.resource_id = resource_id;
        this.calendar_id = calendar_id;
        this.location = location;
        this.capacity = capacity;
        this.link = link;
        this.facility = facility;
    }

    public final String getName() {
        return name;
    }

    public final String getResource_id() {
        return resource_id;
    }

    public final String getCalendar_id() {
        return calendar_id;
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
        return "Resource [name=" + name + ", resource_id=" + resource_id + ", calendar_id=" + calendar_id
                + ", location=" + location + ", capacity=" + capacity + ", link=" + link + ", facility=" + facility
                + "]";
    }

}
