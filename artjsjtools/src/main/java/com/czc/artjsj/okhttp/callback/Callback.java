package com.czc.artjsj.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class Callback<T> {
    /**
     * Thread
     */
    public void onBefore(Request request) {
    }

    /**
     * Thread
     */
    public void onAfter() {
    }

    /**
     * Thread
     */
    public void inProgress(float progress) {
    }

    /**
     * Thread Pool Thread
     */
    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onError(Call call, Exception e);

    public abstract void onResponse(T response);


    public static Callback<?> CALLBACK_DEFAULT = new Callback<Object>() {

        @Override
        public Object parseNetworkResponse(Response response) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e) {
        }

        @Override
        public void onResponse(Object response) {
        }
    };

}