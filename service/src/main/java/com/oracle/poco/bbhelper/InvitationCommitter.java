package com.oracle.poco.bbhelper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * Created by hhayakaw on 2017/06/09.
 */
@EffectiveDuration(fromdate = "start", todate = "end")
public class InvitationCommitter {
    /**
     * 会議の名前
     */
    @NotNull
    @Size(min = 1)
    private final String name;
    /**
     * 会議室のId。beehive上の識別子
     */
    @NotNull
    @Size(min = 1)
    @EffectiveResourceId
    private final String resourceId;
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
    public InvitationCommitter(
            @JsonProperty("name") String name,
            @JsonProperty("resource_id") String resourceId,
            @JsonProperty("start") ZonedDateTime start,
            @JsonProperty("end") ZonedDateTime end) {
        super();
        this.name = name;
        this.resourceId = resourceId;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("resource_id")
    public String getResourceId() {
        return resourceId;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

}
