package com.czc.artjsj.json;

import com.czc.artjsj.log.L;
import com.czc.artjsj.utils.TextUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CZC Json工具
 */
public class KJson {
    /**
     * 返回一个通用的ObjectMapper对象
     *
     * @return ObjectMapper
     */
    public static ObjectMapper createGeneral() {
        ObjectMapper objectMapper = new ObjectMapper();
        kJsonConfig(objectMapper);
        return objectMapper;
    }

    /**
     * ObjectMapper在KJson使用下的默认配置
     */
    public static void kJsonConfig(ObjectMapper objectMapper) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM-dd HH:mm:ss");

        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        // JsonInclude.Include.ALWAYS //默认
        // JsonInclude.Include.NON_DEFAULT 属性为默认值不序列化
        // JsonInclude.Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
        // JsonInclude.Include.NON_NULL 属性为NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 设置解析Date时候的格式化
        objectMapper.setDateFormat(sdf);

        // 设置属性命名策略,对应jackson下PropertyNamingStrategy中的常量值，SNAKE_CASE-返回的json驼峰式转下划线，json body下划线传到后端自动转驼峰式
        // objectMapper.setPropertyNamingStrategy();

        // 常用，全局设置pojo或被@JsonInclude注解的属性的序列化方式
        // 如NON_NULL不为空的属性才会序列化,具体属性可看JsonInclude.Include
        // objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);

        // 设置TimeZone，不设置的话默认UTC
        // objectMapper.setTimeZone(TimeZone.getDefault());


        // 常规默认,枚举类SerializationFeature中的枚举属性为key，值为boolean设置jackson序列化特性,具体key请看SerializationFeature源码
        // serialization:
        // WRITE_DATES_AS_TIMESTAMPS: true # 返回的java.util.date转换成timestamp
        // FAIL_ON_EMPTY_BEANS: true # 对象为空时是否报错，默认true
        // 枚举类DeserializationFeature中的枚举属性为key，值为boolean设置jackson反序列化特性,具体key请看DeserializationFeature源码
        // FAIL_ON_UNKNOWN_PROPERTIES: false，常用,json中含pojo不存在属性时是否失败报错,默认true

        //枚举类MapperFeature中的枚举属性为key，值为boolean设置jackson ObjectMapper特性
        // ObjectMapper在jackson中负责json的读写、json与pojo的互转、json tree的互转,具体特性请看MapperFeature,常规默认即可
        // 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性
        // USE_GETTERS_AS_SETTERS: true #默认false

        // 枚举类JsonParser.Feature枚举类中的枚举属性为key，值为boolean设置jackson JsonParser特性
        // JsonParser在jackson中负责json内容的读取,具体特性请看JsonParser.Feature，一般无需设置默认即可
        // LLOW_SINGLE_QUOTES: true # 是否允许出现单引号,默认false

        // DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES常用，json中含pojo不存在属性时是否失败报错,默认true
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许出现特殊字符和转义符
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许出现单引号
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 字段保留，将null值转为""
//        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
//            @Override
//            public void serialize(Object o, JsonGenerator jsonGenerator,
//                                  SerializerProvider serializerProvider)
//                    throws IOException {
////                jsonGenerator.writeString("");//将null值转为""
//            }
//        });
        // 为mapper注册一个带有SerializerModifier的Factory，此modifier主要做的事情为：当序列化类型为array，list、set时，当值为空时，序列化成[]
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new KBeanSerializer()));
    }

    /**
     * 将Object转化成双引号的json
     *
     * @param been 需要转换的对象
     * @return String
     */
    public static String getJson(Object been) {
        if (been != null) {
            try {
                return KJson.createGeneral().writeValueAsString(been);
            } catch (JsonProcessingException e) {
                L.e(e);
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 将Object转化成双引号的json
     *
     * @param been 需要转换的对象
     * @return String
     */
    public static String getJson(ObjectMapper mapper, Object been) {
        if (been != null) {
            try {
                return mapper.writeValueAsString(been);
            } catch (JsonProcessingException e) {
                L.e(e);
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clzss      映射对象
     * @return JavaBean
     */
    public static <T> T getBean(String jsonString, Class<T> clzss) {
        return getBean(KJson.createGeneral(), jsonString, clzss);
    }

    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clzss      映射对象
     * @return JavaBean
     */
    public static <T> T getBean(ObjectMapper mapper, String jsonString, Class<T> clzss) {
        try {
            if (TextUtils.isEmpty(jsonString))
                return clzss.newInstance();
            T bean = mapper.readValue(jsonString, clzss);
            if (bean == null)
                return clzss.newInstance();
            return bean;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            L.e(e);
            try {
                return clzss.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
                L.e(ex);
                return null;
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            L.e(e);
            return null;
        }
    }

    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clzss      映射对象
     * @return JavaBean
     */
    public static <T> List<T> getListBean(String jsonString, Class<T[]> clzss) {
        return getListBean(KJson.createGeneral(), jsonString, clzss);
    }

    /**
     * 将Json数据转化为JavaBean
     *
     * @param jsonString 需要转成bean对象的json字符串
     * @param clzss      映射对象
     * @return JavaBean
     */
    public static <T> List<T> getListBean(ObjectMapper mapper, String jsonString, Class<T[]> clzss) {
        if (TextUtils.isEmpty(jsonString))
            return new ArrayList<>();
        try {
            return Arrays.asList(mapper.readValue(jsonString, clzss));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            L.e(e);
            return new ArrayList<>();
        }
    }
}
