package uk.gov.hmcts.reform.adoption.adoptioncase.model.ccd.raw;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

import java.math.BigDecimal;

public class BigDecimalSerializer extends ValueSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator jsonGenerator, SerializationContext serializers) {
        jsonGenerator.writeString(value.toString());
    }
}
