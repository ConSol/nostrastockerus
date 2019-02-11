package de.consol.labs.microprofilearticle.common.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@LogInputsOutputsAndExceptions
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LogInputsOutputsAndExceptionsInterceptor implements Serializable {

    private static final LogInputsOutputsAndExceptions ANNOTATION_WITH_DEFAULT_SETTINGS
            = LogInputsOutputsAndExceptionsInterceptor.class.getAnnotation(LogInputsOutputsAndExceptions.class);

    @AroundInvoke
    public Object intercept(final InvocationContext ctx) throws Exception {
        final LogInputsOutputsAndExceptions settings = getSettings(ctx);
        final Logger logger = getLogger(ctx, settings);
        final String methodName = getMethodName(ctx.getMethod());
        final Jsonb jsonb = JsonbBuilder.create();
        final String jsonizedParams = jsonizeParameters(ctx.getParameters(), jsonb);
        final String messageStart = String.format("method %s called with params %s", methodName, jsonizedParams);
        final Object result;
        try {
            result = ctx.proceed();
        } catch (final Exception e) {
            if (settings.logExceptions()) {
                logger.log(getLoggingLevel(settings.exceptionsLoggingLevel()), messageStart + " threw exception", e);
            }
            throw e;
        }
        logger.log(getLoggingLevel(settings.loggingLevel()), messageStart + " produced " + jsonize(result, jsonb));
        return result;
    }

    private static LogInputsOutputsAndExceptions getSettings(final InvocationContext ctx) {
        final Method method = ctx.getMethod();
        if (method.getAnnotation(LogInputsOutputsAndExceptions.class) != null) {
            return method.getAnnotation(LogInputsOutputsAndExceptions.class);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.getAnnotation(LogInputsOutputsAndExceptions.class) != null) {
            return declaringClass.getAnnotation(LogInputsOutputsAndExceptions.class);
        }
        return ANNOTATION_WITH_DEFAULT_SETTINGS;
    }

    private static Logger getLogger(final InvocationContext ctx, final LogInputsOutputsAndExceptions settings) {
        return settings.useTargetClassLogger()
                ? LogManager.getLogger(ctx.getMethod().getDeclaringClass())
                : LogManager.getLogger(LogInputsOutputsAndExceptionsInterceptor.class);
    }

    private static String getMethodName(final Method method) {
        return String.format("%s#%s", method.getDeclaringClass().getCanonicalName(), method.getName());
    }

    private static String jsonizeParameters(final Object[] params, final Jsonb jsonb) {
        return (params == null || params.length <= 0)
                ? "<no params>"
                : Arrays.stream(params).map(p -> jsonize(p, jsonb)).collect(Collectors.joining(", "));
    }

    private static String jsonize(final Object o, final Jsonb jsonb) {
        if (o == null) {
            return "null";
        }
        if (o instanceof Optional<?>) {
            final Optional<?> optional = (Optional<?>) o;
            return optional.isPresent() ? String.format("Optional[%s]", jsonize(optional.get(), jsonb)) : optional.toString();
        }
        try {
            return jsonb.toJson(o);
        } catch (final JsonbException e) {
            return "<NA>";
        }
    }

    private static Level getLoggingLevel(final String loggingLevel) {
        try {
            return Level.valueOf(loggingLevel);
        } catch (final NullPointerException | IllegalArgumentException e) {
            return Level.DEBUG;
        }
    }
}
