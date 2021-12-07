package com.czc.artjsj.autoconfigure;

import com.czc.artjsj.okhttp.OkHttpUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class KNetWorkAutoConfiguration {
    private final KNetWorkProperties kNetWorkProperties;

    public KNetWorkAutoConfiguration(KNetWorkProperties kNetWorkProperties) {
        this.kNetWorkProperties = kNetWorkProperties;
    }

    @Bean
    public void kNetWorkConfig() {
        boolean retryOnConnFail = kNetWorkProperties.isRetryOnConnFail();
        long connectTimeout = kNetWorkProperties.getConnectTimeout() == 0 ? 20000 : kNetWorkProperties.getConnectTimeout();
        long readTimeout = kNetWorkProperties.getReadTimeout() == 0 ? 20000 : kNetWorkProperties.getReadTimeout();
        long writeTimeout = kNetWorkProperties.getWriteTimeout() == 0 ? 20000 : kNetWorkProperties.getWriteTimeout();
        long callTimeout = kNetWorkProperties.getCallTimeout() == 0 ? 60000 : kNetWorkProperties.getCallTimeout();
        OkHttpUtils.setRetryOnConnFailAndTimeout(retryOnConnFail, connectTimeout,
                readTimeout, writeTimeout, callTimeout, TimeUnit.MILLISECONDS);

        int maxIdleConnections = kNetWorkProperties.getMaxIdleConnections() == 0 ? 1500 : kNetWorkProperties.getMaxIdleConnections();
        long keepAliveDuration = kNetWorkProperties.getKeepAliveDuration() == 0 ? 5L : kNetWorkProperties.getKeepAliveDuration();
        OkHttpUtils.connectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.MINUTES);

        boolean sslSkipVerify = kNetWorkProperties.isSslSkipVerify();
        OkHttpUtils.sslSkipVerify(sslSkipVerify);

        String hostName = kNetWorkProperties.getProxyHost();
        int port = kNetWorkProperties.getProxyPort() == 0 ? 80 : kNetWorkProperties.getProxyPort();
        String userName = kNetWorkProperties.getProxyUser();
        String password = kNetWorkProperties.getProxyPwd();
        OkHttpUtils.setProxy(hostName, port, userName, password);
    }
}