package com.czc.artjsj.okhttp.request;

import com.czc.artjsj.okhttp.OkHttpUtils;
import com.czc.artjsj.okhttp.utils.Exceptions;
import com.czc.artjsj.utils.TextUtils;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

import java.util.Map;

public class OtherRequest extends OkHttpRequest {
    private static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    private RequestBody requestBody;
    private final String method;
    private final String content;

    public OtherRequest(RequestBody requestBody, String content, String method, String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
        this.requestBody = requestBody;
        this.method = method;
        this.content = content;

    }

    @Override
    protected RequestBody buildRequestBody() {
        if (requestBody == null && TextUtils.isEmpty(content) && HttpMethod.requiresRequestBody(method)) {
            Exceptions.illegalArgument("requestBody and content can not be null in method:" + method);
        }

        if (requestBody == null && !TextUtils.isEmpty(content)) {
            requestBody = RequestBody.create(MEDIA_TYPE_PLAIN, content);
        }

        return requestBody;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody) {
        if (method.equals(OkHttpUtils.METHOD.PUT)) {
            builder.put(requestBody);
        } else if (method.equals(OkHttpUtils.METHOD.DELETE)) {
            if (requestBody == null)
                builder.delete();
            else
                builder.delete(requestBody);
        } else if (method.equals(OkHttpUtils.METHOD.HEAD)) {
            builder.head();
        } else if (method.equals(OkHttpUtils.METHOD.PATCH)) {
            builder.patch(requestBody);
        }

        return builder.build();
    }

    @Override
    public String toString() {
        if (!TextUtils.isEmpty(content)) {
            return super.toString() + ", requestBody{content=" + content + "} ";
        }
        return super.toString() + ", requestBody{requestBody=" + requestBody.toString() + "} ";
    }
}
