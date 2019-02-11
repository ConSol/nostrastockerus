package de.consol.labs.microprofilearticle.user.manager;

import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtBuilder;
import com.ibm.websphere.security.jwt.JwtException;
import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.user.Config;
import de.consol.labs.microprofilearticle.user.model.CreateTokenRequest;
import de.consol.labs.microprofilearticle.user.model.User;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.time.Instant;

@ApplicationScoped
@LogInputsOutputsAndExceptions
public class TokenManager {

    private static final String GROUPS_CLAIM = "groups";
    private static final String USER_PRINCIPAL_NAME_CLAIM = "upn";

    @Inject
    private Logger logger;

    @Inject
    private Config config;

    private volatile long lastValidityDurationMillis;

    @Produces
    @Metric(name = "token_validity_duration", unit = MetricUnits.MILLISECONDS)
    @ApplicationScoped
    protected Gauge<Long> getTokenValidityDurationMillis() {
        return () -> lastValidityDurationMillis;
    }

    @Timed(name = "execution_time")
    @Counted(name = "total_invocation_count", monotonic = true)
    public String createToken(final CreateTokenRequest createTokenRequest, final User user) {
        final Instant now = Instant.now();
        final String token;
        try {
            token = JwtBuilder.create("jwtBuilder")
                    .subject(user.getName())
                    .expirationTime(now.plusMillis(createTokenRequest.getValidityDurationMillis()).getEpochSecond())
                    .claim(USER_PRINCIPAL_NAME_CLAIM, user.getName())
                    .claim(GROUPS_CLAIM, createTokenRequest.getRoles())
                    .buildJwt()
                    .compact();
        } catch (final JwtException | InvalidBuilderException | InvalidClaimException e) {
            throw new IllegalStateException("could not create JWT", e);
        }
        lastValidityDurationMillis = createTokenRequest.getValidityDurationMillis();
        return token;
    }
}
