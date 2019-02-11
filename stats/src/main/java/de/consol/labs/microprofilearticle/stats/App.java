package de.consol.labs.microprofilearticle.stats;

import de.consol.labs.microprofilearticle.common.rest.ParametersConverterProvider;
import de.consol.labs.microprofilearticle.stats.api.StatsResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class App extends Application {

    /**
     * Fix bug with invalid JSON serialization of Instant.
     * 3rd party dependency jersey-media-json-jackson
     * overwrites the ObjectMapper and the new ObjectMapper
     * does not handle Instant correctly.
     *
     * This method states explicitly which components comprise the web app.
     * Other components on the classpath will not be added.
     */
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(StatsResource.class);
        classes.add(ParametersConverterProvider.class);
        return Collections.unmodifiableSet(classes);
    }
}
