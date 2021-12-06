package com.czc.artjsj.okhttp.request;

import com.czc.artjsj.json.KJsonSingleton;
import com.czc.artjsj.okhttp.OkHttpUtils;
import com.czc.artjsj.okhttp.callback.Callback;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RequestCall {
    private final OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }


    public void generateCall(Callback<?> callback) {
        request = generateRequest(callback);
        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            call = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build().newCall(request);
        } else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
    }

    private Request generateRequest(Callback<?> callback) {
        return okHttpRequest.generateRequest(callback);
    }

    public void execute(Callback<?> callback) {
        generateCall(callback);
        if (callback != null) {
            callback.onBefore(request);
        }
        OkHttpUtils.getInstance().execute(this, callback);
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Response execute() throws IOException {
        generateCall(null);
        return call.execute();
    }

    public <T> T execute(Class<T> clzss) throws IOException {
        generateCall(null);
        Response response = call.execute();
        String strBody = "";
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            strBody = responseBody.string();
        }
        return KJsonSingleton.getBean(strBody, clzss);
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }


}
