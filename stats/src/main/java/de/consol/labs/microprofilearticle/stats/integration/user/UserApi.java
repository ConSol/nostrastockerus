package de.consol.labs.microprofilearticle.stats.integration.user;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.stats.Config;
import de.consol.labs.microprofilearticle.user.generatedclient.ApiClient;
import de.consol.labs.microprofilearticle.user.generatedclient.ApiException;
import de.consol.labs.microprofilearticle.user.generatedclient.api.DefaultApi;
import de.consol.labs.microprofilearticle.user.generatedclient.model.User;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Optional;

@RequestScoped
@LogInputsOutputsAndExceptions
public class UserApi {

    @Inject
    private Config config;

    @Inject
    private JsonWebToken jwt;

    public Optional<User> getUser(final String userName) {
        try {
            return Optional.of(getApi().getUser(userName));
        } catch (final ApiException e) {
            if (Response.Status.NOT_FOUND.getStatusCode() == e.getCode()) {
                return Optional.empty();
            }
            throw new RuntimeException("failed to get user", e);
        }
    }

    private DefaultApi getApi() {
        final ApiClient client = new ApiClient();
        client.setBasePath(config.getUserServiceBasePath());
        client.addDefaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwt.getRawToken()));
        return new DefaultApi(client);
    }
}
