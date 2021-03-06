package com.czc.artjsj.okhttp.request;

import com.czc.artjsj.okhttp.utils.Exceptions;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

public class PostStringRequest extends OkHttpRequest {
    private static final MediaType MEDIA_TYPE_PLAIN = MediaType
            .parse("text/plain;charset=utf-8");

    private final String content;
    private MediaType mediaType;

    public PostStringRequest(String url, Object tag,
                             Map<String, String> params, Map<String, String> headers,
                             String content, MediaType mediaType) {
        super(url, tag, params, headers);
        this.content = content;
        this.mediaType = mediaType;

        if (this.content == null) {
            Exceptions.illegalArgument("the content can not be null !");
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_PLAIN;
        }

    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, content);
    }

    @Override
    protected Request buildRequest(Request.Builder builder,
                                   RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    public String toString() {
        return super.toString() + ", requestBody{content=" + content + "} ";
    }

}
