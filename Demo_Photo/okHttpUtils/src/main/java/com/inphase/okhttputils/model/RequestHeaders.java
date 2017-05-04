package com.inphase.okhttputils.model;

import java.util.concurrent.ConcurrentHashMap;

public class RequestHeaders {

    public ConcurrentHashMap<String, String> headersMap;

    private void init() {
        headersMap = new ConcurrentHashMap<>();
    }

    public RequestHeaders() {
        init();
    }

    public RequestHeaders(String key, String value) {
        init();
        put(key, value);
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            headersMap.put(key, value);
        }
    }

    public void put(RequestHeaders headers) {
        if (headers != null) {
            if (headers.headersMap != null && !headers.headersMap.isEmpty())
                headersMap.putAll(headers.headersMap);
        }
    }
}
