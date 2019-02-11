package de.consol.labs.microprofilearticle.common.config;

import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;

public abstract class AbstractConfig {

    @Inject
    protected Logger logger;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.common.config.isLogConfigurationAfterReading",
            defaultValue = "true"
    )
    private Boolean isLogConfigurationAfterReading;

    @PostConstruct
    protected void init() {
        logger.info("reading configuration");
        parseConfiguration();
        logger.info("configuration has been read");
        if (isLogConfigurationAfterReading) {
            logger.info("configuration: {}", JsonbBuilder.create().toJson(this));
        }
    }

    protected void parseConfiguration() {
    }
}
