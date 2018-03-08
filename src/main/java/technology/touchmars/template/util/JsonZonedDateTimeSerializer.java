package technology.touchmars.template.util;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JsonZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    // yyyy-MM-dd'T'HH:mm:ss.SSSZ
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public void serialize(ZonedDateTime date, JsonGenerator generator, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        synchronized (dateFormat) {
            String formatted = dateFormat.format(date);
            generator.writeString(formatted);
        }
    }

    @Override
    public Class<ZonedDateTime> handledType(){
        return ZonedDateTime.class;
    }

}