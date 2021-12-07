package com.czc.artjsj.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author CZC Json工具
 */
@Component
public class KJsonMapper {

    private final ObjectMapper objectMapper;

    public KJsonMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 将Object转化成双引号的json
     *
     * @param been 需要转换的对象
     * @return String
     */
    public String getJson(Object been) {
        return KJson.getJson(objectMapper, been);
    }

    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clazz      映射对象
     * @return JavaBean
     */
    public <T> T getBean(String jsonString, Class<T> clazz) {
        return KJson.getBean(objectMapper, jsonString, clazz);
    }

    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clazz      映射对象
     * @return JavaBean
     */
    public <T> List<T> getListBean(String jsonString, Class<T[]> clazz) {
        return KJson.getListBean(objectMapper, jsonString, clazz);
    }

}
