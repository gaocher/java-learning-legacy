package learning.serializer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by isaac on 23/02/2017.
 */
@JsonSerialize(using = I18nEnumSerializer.class)
@JsonDeserialize(using = I18nEnumDeserializer.class)
public interface I18nEnum {
}
