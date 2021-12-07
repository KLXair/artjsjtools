package com.czc.artjsj.autoconfigure;

import com.czc.artjsj.json.KJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author CZC
 * @Description Jackson配置
 */
@Configuration
public class KJsonAutoConfiguration {

    //设置了@EnableWebMvc的话将会导致Spring下Jackson默认配置失效，另外继承WebMvcConfigurationSupport重写配置
    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper kJsonConfig(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        KJson.kJsonConfig(objectMapper);
        return objectMapper;
    }
}
