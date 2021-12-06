package com.czc.artjsj.okhttp.builder;


import com.czc.artjsj.okhttp.OkHttpUtils;
import com.czc.artjsj.okhttp.request.OtherRequest;
import com.czc.artjsj.okhttp.request.RequestCall;
import okhttp3.RequestBody;

import java.util.LinkedHashMap;
import java.util.Map;

public class OtherRequestBuilder extends OkHttpRequestBuilder {
    private RequestBody requestBody;
    private final String method;
    private String content;

    public OtherRequestBuilder(String method) {
        super();
        this.method = method;
    }

    @Override
    public RequestCall build() {
        if (params != null && method.equals(OkHttpUtils.METHOD.DELETE)) {
            url = appendParams(url, params);
        }

        return new OtherRequest(requestBody, content, method, url, tag, params, headers).build();
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public OtherRequestBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public OtherRequestBuilder requestBody(String content) {
        this.content = content;
        return this;
    }

    @Override
    public OtherRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public OtherRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public OtherRequestBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public OtherRequestBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public OtherRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public OtherRequestBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
