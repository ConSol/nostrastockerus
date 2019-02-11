package de.consol.labs.microprofilearticle.common.health;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;

@Health
@ApplicationScoped
@LogInputsOutputsAndExceptions
public class ServerHealth implements HealthCheck {

    private static final String HEALTH_CHECK_NAME = "ApplicationServerHealth";
    private static final String DATA_KEY = "default server";

    @Override
    public HealthCheckResponse call() {
        if (!System.getProperty("wlp.server.name").equals("defaultServer")) {
            return HealthCheckResponse.named(HEALTH_CHECK_NAME)
                    .withData(DATA_KEY, "not available")
                    .down()
                    .build();
        }
        return HealthCheckResponse.named(HEALTH_CHECK_NAME)
                .withData(DATA_KEY, "available")
                .up()
                .build();
    }
}
