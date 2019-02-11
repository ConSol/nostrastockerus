package de.consol.labs.microprofilearticle.common.persistence;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.Instant;

@Converter
@LogInputsOutputsAndExceptions(loggingLevel = "TRACE")
public class Instant2TimestampAttributeConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }

    @Override
    public Instant convertToEntityAttribute(final Timestamp timestamp) {
        return timestamp == null ? null : Instant.ofEpochMilli(timestamp.getTime());
    }
}
