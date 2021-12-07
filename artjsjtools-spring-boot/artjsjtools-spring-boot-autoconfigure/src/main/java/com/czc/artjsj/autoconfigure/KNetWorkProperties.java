package com.czc.artjsj.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "czc.knetwork")
public class KNetWorkProperties {
    private boolean retryOnConnFail;
    private long connectTimeout;
    private long readTimeout;
    private long writeTimeout;
    private long callTimeout;
    private int maxIdleConnections;
    private long keepAliveDuration;
    private boolean sslSkipVerify;

    String proxyHost;
    int proxyPort;
    String proxyUser;
    String proxyPwd;

    public KNetWorkProperties() {
        super();
    }

    public boolean isRetryOnConnFail() {
        return retryOnConnFail;
    }

    public void setRetryOnConnFail(boolean retryOnConnFail) {
        this.retryOnConnFail = retryOnConnFail;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public long getCallTimeout() {
        return callTimeout;
    }

    public void setCallTimeout(long callTimeout) {
        this.callTimeout = callTimeout;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    public long getKeepAliveDuration() {
        return keepAliveDuration;
    }

    public void setKeepAliveDuration(long keepAliveDuration) {
        this.keepAliveDuration = keepAliveDuration;
    }

    public boolean isSslSkipVerify() {
        return sslSkipVerify;
    }

    public void setSslSkipVerify(boolean sslSkipVerify) {
        this.sslSkipVerify = sslSkipVerify;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPwd() {
        return proxyPwd;
    }

    public void setProxyPwd(String proxyPwd) {
        this.proxyPwd = proxyPwd;
    }

    @Override
    public String toString() {
        return "KNetWorkProperties{" +
                "retryOnConnFail=" + retryOnConnFail +
                ", connectTimeout=" + connectTimeout +
                ", readTimeout=" + readTimeout +
                ", writeTimeout=" + writeTimeout +
                ", callTimeout=" + callTimeout +
                ", maxIdleConnections=" + maxIdleConnections +
                ", keepAliveDuration=" + keepAliveDuration +
                ", sslSkipVerify=" + sslSkipVerify +
                ", proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyUser='" + proxyUser + '\'' +
                ", proxyPwd='" + proxyPwd + '\'' +
                '}';
    }
}