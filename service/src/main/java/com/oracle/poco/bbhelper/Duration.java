package com.oracle.poco.bbhelper;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Created by hhayakaw on 2017/06/08.
 */
@EffectiveDuration(fromdate = "fromdate", todate = "todate")
public class Duration {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime fromdate;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime todate;

    public Duration() {
        super();
    }

    public void setFromdate(ZonedDateTime fromdate) {
        this.fromdate = fromdate;
    }

    public void setTodate(ZonedDateTime todate) {
        this.todate = todate;
    }

    public ZonedDateTime getFromdate() {
        return fromdate;
    }

    public ZonedDateTime getTodate() {
        return todate;
    }

}
