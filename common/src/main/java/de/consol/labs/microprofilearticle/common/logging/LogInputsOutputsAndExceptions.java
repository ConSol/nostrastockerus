package de.consol.labs.microprofilearticle.common.logging;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({METHOD, TYPE})
public @interface LogInputsOutputsAndExceptions {

    String LOGGING_LEVEL_FATAL = "FATAL";
    String LOGGING_LEVEL_ERROR = "ERROR";
    String LOGGING_LEVEL_WARN = "WARN";
    String LOGGING_LEVEL_INFO = "INFO";
    String LOGGING_LEVEL_DEBUG = "DEBUG";
    String LOGGING_LEVEL_TRACE = "TRACE";

    boolean useTargetClassLogger() default true;

    String loggingLevel() default LOGGING_LEVEL_DEBUG;

    boolean logExceptions() default true;

    String exceptionsLoggingLevel() default LOGGING_LEVEL_ERROR;
}
