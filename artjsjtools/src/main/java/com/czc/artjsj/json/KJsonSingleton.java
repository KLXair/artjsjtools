package com.czc.artjsj.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author CZC Json工具
 */
public class KJsonSingleton {

    private static volatile KJsonSingleton mJosnUtil = null;
    private ObjectMapper mapper = null;

    private static KJsonSingleton getInstance() {
        if (mJosnUtil == null) {
            synchronized (KJsonSingleton.class) {
                if (mJosnUtil == null) {
                    mJosnUtil = new KJsonSingleton();
                    ObjectMapper objectMapper = new ObjectMapper();
                    KJson.kJsonConfig(objectMapper);
                    mJosnUtil.mapper = objectMapper;
                }
            }
        }
        return mJosnUtil;
    }

    /**
     * 将Object转化成双引号的json（遇到=、&等特殊字符串的时候不会将其转义，
     * 需要保持转义使用getJson，disableHtmlEscaping=false）
     *
     * @param been 需要转换的对象
     * @return String
     */
    public static synchronized String getJson(Object been) {
        return KJson.getJson(getInstance().mapper, been);
    }


    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clss       映射对象
     * @return JavaBean
     */
    public static synchronized <T> T getBean(String jsonString, Class<T> clss) {
        return KJson.getBean(getInstance().mapper, jsonString, clss);
    }

    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clss       映射对象
     * @return JavaBean
     */
    public static synchronized <T> List<T> getListBean(String jsonString, Class<T[]> clss) {
        return KJson.getListBean(getInstance().mapper, jsonString, clss);
    }
}
