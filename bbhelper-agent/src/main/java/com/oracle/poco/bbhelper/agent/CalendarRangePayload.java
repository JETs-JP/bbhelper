package com.oracle.poco.bbhelper.agent;

final class CalendarRangePayload  implements BeehiveApiPayload {

    private final String beeType = "calendarRangeQuery";
    private final BeeIdPayload beeId;
    private final String start;
    private final String end;

    CalendarRangePayload(BeeIdPayload beeId, String start, String end) {
        super();
        this.beeId = beeId;
        this.start = start;
        this.end = end;
    }

    public String getBeeType() {
        return beeType;
    }

    public BeeIdPayload getCalendar() {
        return beeId;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

}
