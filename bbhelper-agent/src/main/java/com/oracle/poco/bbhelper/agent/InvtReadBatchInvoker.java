package com.oracle.poco.bbhelper.agent;

import org.springframework.http.HttpMethod;

final class InvtReadBatchInvoker extends BeehiveInvoker {

    private static String ENDPOINT = 
            Constants.BEEHIVE_API_ROOT + "invt/read";

    InvtReadBatchInvoker(BeehiveCredential credential) {
        super(credential, ENDPOINT, HttpMethod.POST);
    }

    @Override
    protected boolean isPrepared() {
        BeehiveApiPayload payload = getBody();
        if (payload == null) {
            return false;
        }
        if (!(payload instanceof BeeIdListPayload)) {
            return false;
        }
        return true;
    }

}
