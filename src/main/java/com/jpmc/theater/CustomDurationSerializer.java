package com.jpmc.theater;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Duration;

/**
 * Custom serializer for Duration so that we can print human readable movie duration for JSon
 */
public class CustomDurationSerializer extends StdSerializer<Duration> {

    public CustomDurationSerializer() {
        this(null);
    }

    public CustomDurationSerializer(Class<Duration> t) {
        super(t);
    }

    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeRaw(Utility.humanReadableFormat(duration));
        jsonGenerator.writeEndObject();
    }
}
