package uk.gov.hmcts.reform.adoption.adoptioncase.model.ccd.raw;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ser.std.StdSerializer;
import tools.jackson.databind.SerializationContext;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String LOCALDATETIME_ISO8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializationContext serializers) {
        jsonGenerator.writeString(value.format(DateTimeFormatter.ofPattern(LOCALDATETIME_ISO8601_FORMAT_STRING)));
    }
}
