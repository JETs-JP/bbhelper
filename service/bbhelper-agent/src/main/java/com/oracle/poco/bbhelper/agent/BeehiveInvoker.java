package com.oracle.poco.bbhelper.agent;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

abstract class BeehiveInvoker {

    protected static final String BEEHIVE_API_ROOT =
            "https://stbeehive.oracle.com/comb/v1/d/";

    private final String endpoint;

    private final HttpMethod method;

    private HttpHeaders headers = new HttpHeaders();

    private Map<String, String> urlQueries = new HashMap<String, String>();

    private BeehiveApiPayload payload;

    BeehiveInvoker(
            BeehiveCredential credential, String endpoint, HttpMethod method) {
        this.endpoint = endpoint;
        this.method = method;
        setDefaultHeaders(credential);
        setDefaultUrlQueries(credential);
    }

    JsonNode invoke() throws JsonProcessingException, IOException {
        if (!isPrepared()) {
            // TODO: 例外を定義する
            throw new IllegalStateException();
        }
        // header, body
        HttpEntity<BeehiveApiPayload> entity =
                new HttpEntity<BeehiveApiPayload>(payload, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.exchange(
                endpoint + makeUrlQueryString(), method, entity, String.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(result.getBody());
    }

    // TODO なんかスケルトンパターンぽくない
    protected void addHeader(Map<String, String> headers) {
        this.headers.setAll(headers);
    }

    // TODO なんかスケルトンパターンぽくない
    protected void addUrlQuery(Map<String, String> queries) {
        this.urlQueries.putAll(queries);
    }

    // TODO なんかスケルトンパターンぽくない
    protected void setPayload(BeehiveApiPayload payload) {
        this.payload = payload;
    }

    protected BeehiveApiPayload getBody() {
        return this.payload;
    }

    abstract protected boolean isPrepared();

    private void setDefaultHeaders(BeehiveCredential credential) {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> cookie = new HashMap<String, String>(1);
        cookie.put(HttpHeaders.COOKIE, credential.getSession());
        addHeader(cookie);
    }

    private void setDefaultUrlQueries(BeehiveCredential credential) {
        Map<String, String> csrf = new HashMap<String, String>(1);
        csrf.put("anticsrf", credential.getAnticsrf());
        addUrlQuery(csrf);
    }
 
    private String makeUrlQueryString() {
        if (urlQueries == null || urlQueries.isEmpty()) {
            return "";
        }
        StringBuffer bf = new StringBuffer("?");
        Set<Entry<String, String>> entries = urlQueries.entrySet();
        int i = 0;
        for (Entry<String, String> entry : entries) {
            if (++i > 0) {
                bf.append("&");
            }
            bf.append(entry.getKey() + "=" + entry.getValue());
        }
        return bf.toString();
    }

}
