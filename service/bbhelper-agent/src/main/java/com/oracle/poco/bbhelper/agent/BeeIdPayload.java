package com.oracle.poco.bbhelper.agent;

final class BeeIdPayload implements BeehiveApiPayload {

    private final String beeType = "beeId";
    private final String id;

    public BeeIdPayload(String id) {
        super();
        this.id = id;
    }

    public String getBeeType() {
        return beeType;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "BeeIdPayload [beeType=" + beeType + ", id=" + id + "]";
    }

}