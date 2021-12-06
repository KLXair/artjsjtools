package com.czc.artjsj.okhttp;

import com.czc.artjsj.log.L;
import com.czc.artjsj.okhttp.builder.*;
import com.czc.artjsj.okhttp.callback.Callback;
import com.czc.artjsj.okhttp.https.HttpsUtils;
import com.czc.artjsj.okhttp.request.RequestCall;
import com.czc.artjsj.utils.TextUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    public static final long DEFAULT_MILLISECONDS = 10000;
    private static volatile OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private final OkHttpClient.Builder okHttpClientBuilder;

    private OkHttpUtils() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        // cookie enabled，不需要使用cookieJar，通过header或者提交参数里就好
        // okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // 这里先不做校验
                return true;
            }
        });
        mOkHttpClient = okHttpClientBuilder.build();
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public void setOkHttpClient(OkHttpClient mOkHttpClient) {
        this.mOkHttpClient = mOkHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return okHttpClientBuilder;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static OtherRequestBuilder head() {
        return new OtherRequestBuilder(METHOD.HEAD);
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void execute(final RequestCall requestCall, Callback<?> callback) {
        L.e("{method:" + requestCall.getRequest().method() + ", detail:"
                + requestCall.getOkHttpRequest().toString() + "}");

        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback<?> finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("IOException异常getMessage:" + e.getMessage());
                sendFailResultCallback(call, e, finalCallback);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                int code = response.code();
                if (code >= 400 && code <= 599) {
                    String responseBody = "";
                    try {
                        responseBody = response.body().string();
                        if (!TextUtils.isEmpty(responseBody)) {
                            sendFailResultCallback(call, new RuntimeException(responseBody), finalCallback);
                        } else {
                            sendFailResultCallback(call, new RuntimeException("{\"status\":" + code + ",\"msg\": \"无具体错误信息\" }"), finalCallback);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.e("生成錯誤信息返回出錯，code=" + code + "；responseBody=" + responseBody, e);
                    }
                    return;
                }

                try {
                    Object obj = finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(obj, finalCallback);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback);
                    L.e("返回成功信息出錯：" + e.getMessage());
                }
            }
        });
    }

    public void sendFailResultCallback(final Call call, final Exception e, final Callback<?> callback) {
        if (callback == null)
            return;
        callback.onError(call, e);
        callback.onAfter();
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null)
            return;
        callback.onResponse(object);
        callback.onAfter();
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    private static SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            L.e(e);
        }
        return null;
    }

    private static X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public static void sslSkipVerify(boolean sslSkipVerify) {
        if (sslSkipVerify)
            getInstance().mOkHttpClient = getInstance().okHttpClientBuilder
                    .sslSocketFactory(Objects.requireNonNull(sslSocketFactory()), x509TrustManager()).build();
    }

    public static void connectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit) {
        getInstance().mOkHttpClient = getInstance().okHttpClientBuilder
                .connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, timeUnit)).build();//连接池
    }

    public static void setCertificates(InputStream... certificates) {
        getInstance().mOkHttpClient = getInstance().okHttpClientBuilder
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null)).build();
    }

    public static void setHostNameVerifier(HostnameVerifier hostNameVerifier) {
        getInstance().mOkHttpClient = getInstance().okHttpClientBuilder.hostnameVerifier(hostNameVerifier).build();
    }

    public static void setConnectTimeout(int timeout, TimeUnit units) {
        getInstance().mOkHttpClient = getInstance().okHttpClientBuilder.connectTimeout(timeout, units).build();
    }

    /**
     * 默认情况下，Retrofit 的默认超时时间如下：
     * Connection timeout: 10 秒
     * Read timeout: 10 秒
     * Write timeout: 10 秒
     * Call timeout: 0 秒 （代表没有超时）
     *
     * @param retryOnConnFail retryOnConnectionFailure当连接失败，尝试重连
     * @param connectTimeout  connectionTimeout 应该是最有意思的一种超时设置了。 他指的是从客户端发出一个请求开始到客户端与服务器端完成 TCP 的 3 次握手建立连接的这段时间。
     *                        换句话说，如果 Retrofit 在指定的时间内无法与服务器端建立连接，那么 Retrofit 就认为这次请求失败。
     *                        比如，当你的用户可能会在网络状态不佳的情况下与你的服务器进行通信，那么你需要增大这个数字。
     * @param writeTimeout    写入超时（writeTimeout）是跟readTimeout 相对应的反方向的数据传输。他检查的是客户端向服务器端发送数据的速率。
     *                        当然，跟 readTimeout的计时器类似，每个 byte 发送成功之后这个计时器都会被重置。
     *                        如果某个byte 的数据传输时间超过了配置的写入超时时间，Retrofit 就会认为这个请求是失败的。
     *                        译者注：注意这个并不是说在指定的时间（比如 15 秒）内需要把所有的数据都发送到服务器端，而是指相邻的两个字节之间的发送时间不能超过指定的时间（15 秒）。
     * @param readTimeout     读取超时（readTimeout）指的是这段时间区间：从连接建立成功开始，Retrofit 就会监测每个字节的数据传输的速率。
     *                        如果其中某自己距离上一字节传输成功的时间大于指定的 readTimeout 了，Retrofit 就会认为这个请求是失败的。
     *                        这个时间计数器会在读取到每个 byte 之后归零重新开始计时。所以如果你的响应当中有 120 个 bytes 需要传输到客户端，
     *                        而每个 byte 的传输都需要 5 秒，这种情况下尽管完全传输需要 600 秒，但不会触发 readTimeout（30 秒）error。
     *                        另外，readTimeout 的触发不仅限于服务器端的处理能力，也有可能是由于糟糕的网络状态引起。
     *                        译者注：注意这个并不是说在指定的时间（比如 30 秒）内需要把响应内容完全接收，而是指相邻的两个字节之间的接收时间不能超过指定的时间（ 30 秒）。
     * @param callTimeout     这部分内容是从这里看到的： https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/-builder/call-timeout/
     *                        The call timeout spans the entire call: resolving DNS, connecting, writing the request body, server processing,
     *                        and reading the response body. If the call requires redirects or retries all must complete within one timeout period.
     *                        Call timeout 的计时器横跨整个请求，从 DNS 解析，连接建立，发送数据到服务器，服务器端处理，然后发送响应到客户端，直到客户端完全读取响应内容。
     *                        如果这个请求需要重定向或重试，这些过程都必须在指定的 callTimeout 时间区间内完成。如果不能完成 Retrofit 就会认为请求失败。
     *                        这个 callTimeout 的默认值为 0 代表不考虑请求的超时。
     *                        译者注: 当你的应用内需要限定 App 在某个指定的时间内得到响应结果，如果在指定时间内未能得到就认为是超时的话，那么你应该用callTimeout.
     * @param units           超时单位
     */
    public static void setRetryOnConnFailAndTimeout(boolean retryOnConnFail, long connectTimeout,
                                                    long readTimeout, long writeTimeout, long callTimeout, TimeUnit units) {
        // 这里额外说明下，超时还可以设置pingInterval，通过跟源码我们可以看到,这个值只有http2和webSocket中有使用，如果设置了这个值会定时的向服务器发送一个消息来保持长连接。
        // 所以在写websocket时是完全可以只用设置这个值来保持长连接的.客户端在发送ping消息时服务端会相应的返回pong消息来进行回应.
        // 同时okhttp也实现了pong,服务端在发起ping的时候客户端会通过pong来进行回应,即:在进行长连接时,客户端不需要进行只需要服务端进行定时ping也是可以保持长连接的.
        // 接下来就开始讲和我们密切相关的readTimeout和writeTimeout了,当然也是最复杂的.其中最重要的还是readTimeout,我们先看writeTimeout
        getInstance().mOkHttpClient = getInstance().okHttpClientBuilder.retryOnConnectionFailure(retryOnConnFail)
                .connectTimeout(connectTimeout, units).readTimeout(readTimeout, units).writeTimeout(writeTimeout, units).callTimeout(callTimeout, units)
                .build();
    }

    // 设置okhttp全局请求代理（注意，如果请求自己另外设置了connectTimeout|readTimeOut|writeTimeOut等build的操作，则不会使用全局代理请求，因为自己设置了一个全新的build）
    public static void setProxy(String hostName, int port, final String userName, final String password) {
        if (TextUtils.isEmpty(hostName)) {
            getInstance().mOkHttpClient = getInstance().okHttpClientBuilder.proxy(null).build();
        } else {
            getInstance().okHttpClientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port)));
            getInstance().mOkHttpClient = getInstance().okHttpClientBuilder.proxyAuthenticator(new okhttp3.Authenticator() {
                @Override
                public Request authenticate(@Nullable Route route, @NotNull Response response) {
                    //设置代理服务器账号密码
                    String credential = Credentials.basic(userName, password);
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                }
            }).build();
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}
