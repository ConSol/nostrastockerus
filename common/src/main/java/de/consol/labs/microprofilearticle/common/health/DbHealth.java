package de.consol.labs.microprofilearticle.common.health;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
@LogInputsOutputsAndExceptions
public class DbHealth implements HealthCheck {

    private static final String HEALTH_CHECK_NAME = "database";
    private static final String DATA_KEY = "database";

    @Inject
    private DbPinger dbPinger;

    @Override
    public HealthCheckResponse call() {
        return dbPinger.pingDb() ? up() : down();
    }

    private static HealthCheckResponse up() {
        return HealthCheckResponse.named(HEALTH_CHECK_NAME)
                .withData(DATA_KEY, "available")
                .up()
                .build();
    }

    private static HealthCheckResponse down() {
        return HealthCheckResponse.named(HEALTH_CHECK_NAME)
                .withData(DATA_KEY, "not available")
                .down()
                .build();
    }
}
