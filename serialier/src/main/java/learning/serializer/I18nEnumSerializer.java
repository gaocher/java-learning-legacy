package learning.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import learning.EnumMessageSourceHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by isaac on 23/02/2017.
 */
public class I18nEnumSerializer extends StdSerializer<I18nEnum> {

    private ObjectMapper objectMapper = new ObjectMapper();
    public I18nEnumSerializer(){
        this(I18nEnum.class);
    }
    protected I18nEnumSerializer(Class<I18nEnum> t) {
        super(t);
    }


    @Override
    public void serialize(I18nEnum value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Locale locale = LocaleContextHolder.getLocale();
        MessageSource messageSource = EnumMessageSourceHolder.getInstance();
        String name = ((Enum)value).name();
        String className = value.getClass().getCanonicalName();
        String key = className+"."+name;
        String message = messageSource.getMessage(key, null, locale);
        Payload payload = new Payload(name, message,className);
        objectMapper.writeValue(gen,payload);
    }
}
