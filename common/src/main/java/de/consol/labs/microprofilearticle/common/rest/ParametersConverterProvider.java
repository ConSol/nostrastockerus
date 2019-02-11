package de.consol.labs.microprofilearticle.common.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;

@Provider
@ApplicationScoped
public class ParametersConverterProvider implements ParamConverterProvider {

    @Inject
    private Instant2StringParamConverter instant2StringParamConverter;

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> aClass, final Type type, final Annotation[] annotations) {
        if (aClass.isAssignableFrom(Instant.class)) {
            return (ParamConverter<T>) instant2StringParamConverter;
        }
        return null;
    }
}
