package com.czc.artjsj.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class KJsonSerializer extends JsonSerializer<Object> {
    KBeanSerializer.KSerializerType kSerializerType;

    public KJsonSerializer(KBeanSerializer.KSerializerType serializerType) {
        this.kSerializerType = serializerType;
    }

    public KBeanSerializer.KSerializerType getkSerializerType() {
        return kSerializerType;
    }

    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            /*
            // String fieldName = jgen.getOutputContext().getCurrentName();
            // L.e(fieldName + "-type=" + kSerializerType);
             */
            if (kSerializerType == KBeanSerializer.KSerializerType.MAP
                    || kSerializerType == KBeanSerializer.KSerializerType.OTHER) {
                /*
                obj/map/bean/其他类型对象都返回{}
                 */
                jgen.writeStartObject();
                jgen.writeEndObject();
            } else if (kSerializerType == KBeanSerializer.KSerializerType.STR) {
                jgen.writeString("");
            } else if (kSerializerType == KBeanSerializer.KSerializerType.NUM) {
                jgen.writeNumber(0);
            } else if (kSerializerType == KBeanSerializer.KSerializerType.BOOL) {
                jgen.writeBoolean(false);
            } else if (kSerializerType == KBeanSerializer.KSerializerType.CHAR) {
                jgen.writeObject('\u0000');
            } else if (kSerializerType == KBeanSerializer.KSerializerType.ARR) {
                /*
                列表型null值返回[]
                 */
                jgen.writeStartArray();
                jgen.writeEndArray();
            } else {
                jgen.writeNull();
            }
        }

    }
}
