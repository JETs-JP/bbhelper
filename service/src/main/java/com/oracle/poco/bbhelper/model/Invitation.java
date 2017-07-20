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

    @JsonCreator
    public Invitation(@JsonProperty("name") String name,
                      @JsonProperty("invitation_id") String invitation_id,
                      @JsonProperty("resource_id") String resource_id,
                      @JsonProperty("organizer") Person organizer,
                      @JsonProperty("start") ZonedDateTime start,
                      @JsonProperty("end") ZonedDateTime end) {
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invitation that = (Invitation) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (!getInvitation_id().equals(that.getInvitation_id())) return false;
        if (getResource_id() != null ? !getResource_id().equals(that.getResource_id()) : that.getResource_id() != null)
            return false;
        if (getOrganizer() != null ? !getOrganizer().equals(that.getOrganizer()) : that.getOrganizer() != null)
            return false;
        if (!getStart().equals(that.getStart())) return false;
        return getEnd().equals(that.getEnd());
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + getInvitation_id().hashCode();
        result = 31 * result + (getResource_id() != null ? getResource_id().hashCode() : 0);
        result = 31 * result + (getOrganizer() != null ? getOrganizer().hashCode() : 0);
        result = 31 * result + getStart().hashCode();
        result = 31 * result + getEnd().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Invitation{" +
                "name='" + name + '\'' +
                ", invitation_id='" + invitation_id + '\'' +
                ", resource_id='" + resource_id + '\'' +
                ", organizer=" + organizer +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

}