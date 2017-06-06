package com.oracle.poco.bbhelper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Facility {

    private final String wall;
    private final String door;
    private final String projector;
    private final String phone;
    private final String whiteboard;

    @JsonCreator
    public Facility(@JsonProperty("wall") String wall, @JsonProperty("door") String door,
                    @JsonProperty("projector") String projector, @JsonProperty("phone") String phone,
                    @JsonProperty("whiteboard") String whiteboard) {
        super();
        this.wall = wall;
        this.door = door;
        this.projector = projector;
        this.phone = phone;
        this.whiteboard = whiteboard;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getWall() {
        return wall;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getDoor() {
        return door;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getProjector() {
        return projector;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getPhone() {
        return phone;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getWhiteboard() {
        return whiteboard;
    }

}
