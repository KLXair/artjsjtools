package com.czc.artjsj.okhttp.callback;

import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response) throws IOException {
        String strBody = "";
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            strBody = responseBody.string();
        }
        return strBody;
    }

}
