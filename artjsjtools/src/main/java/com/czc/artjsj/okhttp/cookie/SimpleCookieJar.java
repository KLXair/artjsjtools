package com.czc.artjsj.okhttp.cookie;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class SimpleCookieJar implements CookieJar {
    private final List<Cookie> allCookies = new ArrayList<>();

    @Override
    public synchronized void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
        allCookies.addAll(cookies);
    }

    @NotNull
    @Override
    public synchronized List<Cookie> loadForRequest(@NotNull HttpUrl url) {
        List<Cookie> result = new ArrayList<>();
        for (Cookie cookie : allCookies) {
            if (cookie.matches(url)) {
                result.add(cookie);
            }
        }
        return result;
    }
}
