package technology.touchmars.template.util;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonDateSerializer extends JsonSerializer<Date> {

    // 2017-10-30T01:02:08.000Z
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // yyyy-MM-dd'T'HH:mm:ss.SSSZ
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Override
    public void serialize(Date date, JsonGenerator generator, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        synchronized (dateFormat) {
            String formatted = dateFormat.format(date);
            generator.writeString(formatted);
        }
    }

    @Override
    public Class<Date> handledType(){
        return Date.class;
    }

}