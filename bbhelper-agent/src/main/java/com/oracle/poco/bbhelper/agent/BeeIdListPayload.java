package com.oracle.poco.bbhelper.agent;

import java.util.List;

final class BeeIdListPayload implements BeehiveApiPayload {

    private final String beeType = "beeIdList";
    private final List<BeeIdPayload> beeId;
    
    public BeeIdListPayload(List<BeeIdPayload> beeId) {
        super();
        this.beeId = beeId;
    }

    public String getBeeType() {
        return beeType;
    }

    public List<BeeIdPayload> getBeeId() {
        return beeId;
    }

    @Override
    public String toString() {
        return "BeeIdListPayload [beeType=" + beeType + ", beeId=" + beeId + "]";
    }

}