package com.oracle.poco.bbhelper.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

public final class Invitation {

    /**
     * 会議の名前
     */
    @NotNull
    private final String name;
    /**
     * 会議のId。beehive上の識別子
     */
    private final String invitation_id;
    /**
     * 会議室のId。beehive上の識別子
     */
    @NotNull
    private final String resource_id;
    /**
     * 会議の主催者
     */
    private final Person organizer;
    /**
     * 会議の開始日時
     */
    @NotNull
    private final ZonedDateTime start;
    /**
     * 会議の終了日時
     */
    @NotNull
    private final ZonedDateTime end;

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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getName() {
        return name;
    }

    public String getInvitation_id() {
        return invitation_id;
    }

    public String getResource_id() {
        return resource_id;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Person getOrganizer() {
        return organizer;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
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