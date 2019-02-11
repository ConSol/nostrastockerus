package de.consol.labs.microprofilearticle.common.rest;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import org.apache.logging.log4j.util.Strings;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.ParamConverter;
import java.time.Instant;

@ApplicationScoped
@LogInputsOutputsAndExceptions(loggingLevel = LogInputsOutputsAndExceptions.LOGGING_LEVEL_TRACE)
public class Instant2StringParamConverter implements ParamConverter<Instant> {

    @Override
    public Instant fromString(final String s) {
        if (Strings.isNotBlank(s)) {
            return Instant.parse(s);
        }
        return null;
    }

    @Override
    public String toString(final Instant instant) {
        return instant != null ? instant.toString() : null;
    }
}
