package de.consol.labs.microprofilearticle.prophecy;

import de.consol.labs.microprofilearticle.common.config.AbstractConfig;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;

@ApplicationScoped
public class Config extends AbstractConfig {

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.prophecy.config.minDifferenceBetweenExpectedAtAndCreatedAtSeconds",
            defaultValue = "604800" // 7 days
    )
    private Long minDifferenceBetweenExpectedAtAndCreatedAtSeconds;
    private Duration minDifferenceBetweenExpectedAtAndCreatedAt;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.prophecy.config.maxDifferenceBetweenExpectedAtAndCreatedAtSeconds",
            defaultValue = "8640000" // 100 days
    )
    private Long maxDifferenceBetweenExpectedAtAndCreatedAtSeconds;
    private Duration maxDifferenceBetweenExpectedAtAndCreatedAt;

    @Inject
    @ConfigProperty(
            name = "de.consol.labs.microprofilearticle.prophecy.config.relativeVotingDurationPercentage",
            defaultValue = "65"
    )
    private Long relativeVotingDurationPercentage;

    @Override
    protected void parseConfiguration() {
        minDifferenceBetweenExpectedAtAndCreatedAt = Duration.ofSeconds(minDifferenceBetweenExpectedAtAndCreatedAtSeconds);
        maxDifferenceBetweenExpectedAtAndCreatedAt = Duration.ofSeconds(maxDifferenceBetweenExpectedAtAndCreatedAtSeconds);
    }

    public Duration getMinDifferenceBetweenExpectedAtAndCreatedAt() {
        return minDifferenceBetweenExpectedAtAndCreatedAt;
    }

    public Duration getMaxDifferenceBetweenExpectedAtAndCreatedAt() {
        return maxDifferenceBetweenExpectedAtAndCreatedAt;
    }

    public long getRelativeVotingDurationPercentage() {
        return relativeVotingDurationPercentage;
    }
}
