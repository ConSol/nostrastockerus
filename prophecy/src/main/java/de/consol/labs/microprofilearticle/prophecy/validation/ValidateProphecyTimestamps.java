package de.consol.labs.microprofilearticle.prophecy.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {ProphecyEntityTimestampsValidator.class})
public @interface ValidateProphecyTimestamps {

    String message() default "Wrong timestamp parameters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
