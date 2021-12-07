package com.czc.artjsj.autoconfigure;

import com.czc.artjsj.ArtJsjCommNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CZC
 * @Description Jackson配置
 */
@Configuration
public class ArtJsjAutoConfiguration {
    private final ArtJsjProperties artJsjProperties;

    public ArtJsjAutoConfiguration(ArtJsjProperties artJsjProperties) {
        this.artJsjProperties = artJsjProperties;
    }

    @Bean
    public void artJsjConfig() {
        ArtJsjCommNames.debug = artJsjProperties.isDebug();
    }
}
