package com.czc.artjsj.okhttp.callback;

import com.czc.artjsj.json.KJsonSingleton;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public abstract class ObjectCallback<T> extends Callback<T> {

    @SuppressWarnings("unchecked")
    @Override
    public T parseNetworkResponse(Response response) throws IOException {
        Class<T> clzss = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String strBody = "";
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            strBody = responseBody.string();
        }
        return KJsonSingleton.getBean(strBody, clzss);
    }

}
