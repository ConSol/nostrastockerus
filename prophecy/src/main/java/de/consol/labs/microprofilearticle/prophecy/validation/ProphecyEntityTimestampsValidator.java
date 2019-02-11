package de.consol.labs.microprofilearticle.prophecy.validation;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.Config;
import de.consol.labs.microprofilearticle.prophecy.entity.ProphecyEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.Instant;

@ApplicationScoped
@LogInputsOutputsAndExceptions(loggingLevel = LogInputsOutputsAndExceptions.LOGGING_LEVEL_TRACE)
public class ProphecyEntityTimestampsValidator implements ConstraintValidator<ValidateProphecyTimestamps, ProphecyEntity> {

    @Inject
    private Config config;

    @Override
    public void initialize(final ValidateProphecyTimestamps constraintAnnotation) {
    }

    @Override
    public boolean isValid(final ProphecyEntity value, final ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        final Instant createdAt = value.getCreatedAt();
        final Instant expectedAt = value.getExpectedAt();
        if (createdAt == null || expectedAt == null) {
            return false;
        }
        final Duration diff = Duration.between(createdAt, expectedAt);
        if (diff.compareTo(config.getMinDifferenceBetweenExpectedAtAndCreatedAt()) < 0) {
            return false;
        }
        if (diff.compareTo(config.getMaxDifferenceBetweenExpectedAtAndCreatedAt()) > 0) {
            return false;
        }
        return true;
    }
}
