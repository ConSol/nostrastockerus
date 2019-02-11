package de.consol.labs.microprofilearticle.stats;

import de.consol.labs.microprofilearticle.common.config.AbstractConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.ZoneId;

@ApplicationScoped
public class Config extends AbstractConfig {

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.stats.config.stockMarketApiBaseUrl",
            defaultValue = "https://api.iextrading.com/1.0/"
    )
    private String stockMarketApiBaseUrl;
    private URL stockMarketApiBase;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.stats.config.stockMarketApiZoneName",
            defaultValue = "America/New_York"
    )
    private String stockMarketApiZoneName;
    private ZoneId stockMarketApiZone;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.stats.config.timeWindowLengthSecondsForPropheciesCheck",
            defaultValue = "86400" // 1 day
    )
    private Long timeWindowLengthSecondsForPropheciesCheck;
    private Duration timeWindowLengthForPropheciesCheck;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.stats.config.userServiceBasePath",
            defaultValue = "https://user/"
    )
    private String userServiceBasePath;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.stats.config.prophecyServiceBasePath",
            defaultValue = "https://prophecy/"
    )
    private String prophecyServiceBasePath;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.stats.config.stockServiceBasePath",
            defaultValue = "https://api.iextrading.com/1.0/"
    )
    private String stockServiceBasePath;

    @Override
    protected void parseConfiguration() {
        try {
            stockMarketApiBase = new URL(stockMarketApiBaseUrl);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("malformed URL config parameter", e);
        }
        stockMarketApiZone = ZoneId.of(stockMarketApiZoneName);
        timeWindowLengthForPropheciesCheck = Duration.ofSeconds(timeWindowLengthSecondsForPropheciesCheck);
    }

    public URL getStockMarketApiBase() {
        return stockMarketApiBase;
    }

    public ZoneId getStockMarketApiZone() {
        return stockMarketApiZone;
    }

    public Duration getTimeWindowLengthForPropheciesCheck() {
        return timeWindowLengthForPropheciesCheck;
    }

    public String getUserServiceBasePath() {
        return userServiceBasePath;
    }

    public String getProphecyServiceBasePath() {
        return prophecyServiceBasePath;
    }

    public String getStockServiceBasePath() {
        return stockServiceBasePath;
    }
}
