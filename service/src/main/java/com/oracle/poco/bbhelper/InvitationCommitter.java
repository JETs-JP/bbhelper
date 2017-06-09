package com.oracle.poco.bbhelper;

import javax.validation.constraints.NotNull;
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
    private String name;
    /**
     * 会議室のId。beehive上の識別子
     */
    @NotNull
    private String resource_id;
    /**
     * 会議の開始日時
     */
    @NotNull
    private ZonedDateTime start;
    /**
     * 会議の終了日時
     */
    @NotNull
    private ZonedDateTime end;

    public InvitationCommitter() {
        super();
    }

    public InvitationCommitter(
            String name, String resource_id, ZonedDateTime start, ZonedDateTime end) {
        super();
        this.name = name;
        this.resource_id = resource_id;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
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

}
