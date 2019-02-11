package de.consol.labs.microprofilearticle.stats.integration.prophecy;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.ApiClient;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.ApiException;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.api.DefaultApi;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.model.Prophecy;
import de.consol.labs.microprofilearticle.stats.Config;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RequestScoped
@LogInputsOutputsAndExceptions
public class ProphecyApi {

    @Inject
    private Config config;

    @Inject
    private JsonWebToken jwt;

    public List<Prophecy> findPropheciesByExpectedAt(final Instant expectedFrom, final Instant expectedTo) {
        try {
            return getApi().findPropheciesByExpectedAt(
                    expectedFrom.atOffset(ZoneOffset.UTC),
                    expectedTo.atOffset(ZoneOffset.UTC)
            );
        } catch (final ApiException e) {
            throw new RuntimeException("failed to retrieve prophecies", e);
        }
    }

    public Optional<Prophecy> getProphecyInformation(final long prophecyId) {
        try {
            return Optional.of(getApi().getProphecyInformation(prophecyId));
        } catch (final ApiException e) {
            if (Response.Status.NOT_FOUND.getStatusCode() == e.getCode()) {
                return Optional.empty();
            }
            throw new RuntimeException("failed to retrieve prophecy with ID" + prophecyId, e);
        }
    }

    @Retry
    public List<Prophecy> getPropheciesCreatedByUser(final String userName) {
        try {
            return getApi().getPropheciesCreatedByUser(userName);
        } catch (final ApiException e) {
            throw new RuntimeException("failed to retrieve prophecies created by user " + userName, e);
        }
    }

    private DefaultApi getApi() {
        final ApiClient client = new ApiClient();
        client.setBasePath(config.getProphecyServiceBasePath());
        client.addDefaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwt.getRawToken()));
        return new PatchedDefaultApi(client);
    }
}
