package learning.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Created by isaac on 24/02/2017.
 */
public class I18nEnumDeserializer extends StdDeserializer<I18nEnum> {

    private ObjectMapper objectMapper = new ObjectMapper();

    public I18nEnumDeserializer(){
        super(I18nEnum.class);
    }

    public I18nEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Payload payload = objectMapper.readValue(p, Payload.class);
        Class<? extends Enum> enumClass = null;
        try {
            enumClass = (Class<? extends Enum>)getClass().getClassLoader().loadClass(payload.getClazz());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (I18nEnum)Enum.valueOf(enumClass,payload.getCode());

    }
}
