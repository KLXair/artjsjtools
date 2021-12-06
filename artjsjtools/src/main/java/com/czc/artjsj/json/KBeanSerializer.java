package com.czc.artjsj.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author CZC 序列化解析NULL时设置默认值使用
 */
public class KBeanSerializer extends BeanSerializerModifier {

    enum KSerializerType {
        STR, NUM, BOOL, CHAR, ARR, MAP, OTHER
    }

    List<KJsonSerializer> listKJsonSerializer = new ArrayList<>();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        beanProperties.forEach(writer -> {
            // 判断字段的类型
            KSerializerType kSerializerType;
            if (isStringType(writer))
                kSerializerType = KSerializerType.STR;
            else if (isNumType(writer))
                kSerializerType = KSerializerType.NUM;
            else if (isBooleanType(writer))
                kSerializerType = KSerializerType.BOOL;
            else if (isCharacter(writer))
                kSerializerType = KSerializerType.CHAR;
            else if (isArrayType(writer))
                kSerializerType = KSerializerType.ARR;
            else if (isMapType(writer))
                kSerializerType = KSerializerType.MAP;
            else
                kSerializerType = KSerializerType.OTHER;

            for (KJsonSerializer kJsonSerializer : listKJsonSerializer)
                if (kSerializerType.equals(kJsonSerializer.getkSerializerType())) {
                    writer.assignNullSerializer(kJsonSerializer);
                    return;
                }

            KJsonSerializer kJsonSerializer = new KJsonSerializer(kSerializerType);
            listKJsonSerializer.add(kJsonSerializer);
            writer.assignNullSerializer(kJsonSerializer);
        });
        return beanProperties;
    }

    //用不到，基础数据类型有默认值，判断是否是为：boolean, byte, char, short, int, long, float, double 等原始类型。
    private boolean isPrimitiveType(BeanPropertyWriter writer) {
        return writer.getType().getRawClass().isPrimitive();
    }

    // 判断是否是string类型
    private boolean isStringType(BeanPropertyWriter writer) {
        return CharSequence.class.isAssignableFrom(writer.getType().getRawClass());
    }

    // 判断是否是为：Short, Int, Long, Float, Double, Byte等数值类型。
    private boolean isNumType(BeanPropertyWriter writer) {
        return Number.class.isAssignableFrom(writer.getType().getRawClass());
    }

    // 判断是否是为：Boolean类型。
    private boolean isBooleanType(BeanPropertyWriter writer) {
        return writer.getType().getRawClass().equals(Boolean.class);
    }

    // 判断是否是为：Character等数值类型。
    private boolean isCharacter(BeanPropertyWriter writer) {
        return writer.getType().getRawClass().equals(Character.class);
    }

    // 判断是否是集合类型
    protected boolean isArrayType(BeanPropertyWriter writer) {
        return Collection.class.isAssignableFrom(writer.getType().getRawClass());
    }

    // 判断是否是为：Map 类型。
    private boolean isMapType(BeanPropertyWriter writer) {
        return Map.class.isAssignableFrom(writer.getType().getRawClass());
    }
}