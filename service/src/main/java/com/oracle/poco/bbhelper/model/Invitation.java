package com.oracle.poco.bbhelper.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class Invitation {

    /**
     * 会議の名前
     */
    @NotNull
    private final String name;
    /**
     * 会議のId。beehive上の識別子
     */
    private final String invitationId;
    /**
     * 会議室のId。beehive上の識別子
     */
    @NotNull
    private final String resourceId;
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

    @JsonCreator
    public Invitation(@JsonProperty("name") String name,
                      @JsonProperty("invitation_id") String invitationId,
                      @JsonProperty("resource_id") String resourceId,
                      @JsonProperty("organizer") Person organizer,
                      @JsonProperty("start") ZonedDateTime start,
                      @JsonProperty("end") ZonedDateTime end) {
        super();
        this.name = name;
        this.invitationId = invitationId;
        this.resourceId = resourceId;
        this.organizer = organizer;
        this.start = start;
        this.end = end;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public String getName() {
        return name;
    }

    @JsonProperty("invitation_id")
    public String getInvitationId() {
        return invitationId;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("resource_id")
    public String getResourceId() {
        return resourceId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invitation that = (Invitation) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (!getInvitationId().equals(that.getInvitationId())) return false;
        if (getResourceId() != null ? !getResourceId().equals(that.getResourceId()) : that.getResourceId() != null)
            return false;
        if (getOrganizer() != null ? !getOrganizer().equals(that.getOrganizer()) : that.getOrganizer() != null)
            return false;
        if (!getStart().equals(that.getStart())) return false;
        return getEnd().equals(that.getEnd());
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + getInvitationId().hashCode();
        result = 31 * result + (getResourceId() != null ? getResourceId().hashCode() : 0);
        result = 31 * result + (getOrganizer() != null ? getOrganizer().hashCode() : 0);
        result = 31 * result + getStart().hashCode();
        result = 31 * result + getEnd().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "name='" + name + '\'' +
                ", invitation_id='" + invitationId + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", organizer=" + organizer +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

}