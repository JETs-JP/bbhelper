package com.oracle.poco.bbhelper.model;

import java.time.ZonedDateTime;

// TODO: 開始日時と終了日時が不整合を起こすようなスケジュールを排除したい
// TODO: invitaion_idをユーザーが明示的に設定するのを防止したい
public final class Invitation {

    private String name;
    private String invitation_id;
    private String resource_id;
    private Person organizer;
    private ZonedDateTime start;
    private ZonedDateTime end;

    public Invitation() {
        super();
    }

    public Invitation(String name, String invitation_id, String resource_id,
            Person organizer, ZonedDateTime start, ZonedDateTime end) {
        super();
        this.name = name;
        this.invitation_id = invitation_id;
        this.resource_id = resource_id;
        this.organizer = organizer;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvitation_id() {
        return invitation_id;
    }

    public void setInvitation_id(String invitation_id) {
        this.invitation_id = invitation_id;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public Person getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Person organizer) {
        this.organizer = organizer;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((invitation_id == null) ? 0 : invitation_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Invitation other = (Invitation) obj;
        if (invitation_id == null) {
            if (other.invitation_id != null)
                return false;
        } else if (!invitation_id.equals(other.invitation_id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Invitation [name=" + name + ", invitation_id=" + invitation_id + ", resource_id=" + resource_id
                + ", organizer=" + organizer + ", start=" + start + ", end=" + end + "]";
    }

}