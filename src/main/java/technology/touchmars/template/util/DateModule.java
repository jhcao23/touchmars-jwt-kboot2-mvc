package technology.touchmars.template.util;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.ZonedDateTime;

public class DateModule extends SimpleModule {

    public DateModule() {
        addSerializer(new JsonDateSerializer());
        addSerializer(new JsonZonedDateTimeSerializer());
        addDeserializer(ZonedDateTime.class, new JsonZonedDateTimeDeserializer());
    }

}
