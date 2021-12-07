package com.czc.artjsj.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "czc.artjsj")
public class ArtJsjProperties {
    private boolean debug;

    public ArtJsjProperties() {
        super();
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    @Override
    public String toString() {
        return "ArtJsjProperties{" +
                "debug=" + debug +
                '}';
    }
}