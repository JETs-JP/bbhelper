package com.oracle.poco.bbhelper.model;

public class Facility {

    private String wall;
    private String door;
    private String projector;
    private String phone;
    private String whiteboard;

    public Facility() {}

    public Facility(String wall, String door, String projector, String phone,
            String whiteboard) {
        super();
        this.wall = wall;
        this.door = door;
        this.projector = projector;
        this.phone = phone;
        this.whiteboard = whiteboard;
    }

    public static Facility deepClone(Facility origin) {
        if (origin == null) {
            return null;
        }
        return new Facility(
                ((origin.getWall() != null) ? new String(origin.getWall()) : null),
                ((origin.getDoor() != null) ? new String(origin.getDoor()) : null),
                ((origin.getProjector() != null) ? new String(origin.getProjector()) : null),
                ((origin.getPhone() != null) ? new String(origin.getPhone()) : null),
                ((origin.getWhiteboard() != null) ? new String(origin.getWhiteboard()) : null)
            );
    }

    public String getWall() {
        return wall;
    }
    public void setWall(String wall) {
        this.wall = wall;
    }
    public String getDoor() {
        return door;
    }
    public void setDoor(String door) {
        this.door = door;
    }
    public String getProjector() {
        return projector;
    }
    public void setProjector(String projector) {
        this.projector = projector;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getWhiteboard() {
        return whiteboard;
    }
    public void setWhiteboard(String whiteboard) {
        this.whiteboard = whiteboard;
    }

}
