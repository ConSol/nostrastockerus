package de.consol.labs.microprofilearticle.user.api;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.common.openapi.AppSecurityScheme;
import de.consol.labs.microprofilearticle.common.openapi.ClientError;
import de.consol.labs.microprofilearticle.user.Config;
import de.consol.labs.microprofilearticle.user.manager.TokenManager;
import de.consol.labs.microprofilearticle.user.manager.UserManager;
import de.consol.labs.microprofilearticle.user.model.CreateTokenRequest;
import de.consol.labs.microprofilearticle.user.model.User;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

import static de.consol.labs.microprofilearticle.common.rbac.SecurityRole.*;

@RequestScoped
@Path("token")
@LogInputsOutputsAndExceptions
public class TokenResource {

    private static final String ADMIN_USER_NAME = "admin";
    private static final String ADMIN_USER_EMAIL = "admin@admin.com";

    @Inject
    private Logger logger;

    @Inject
    private Config config;

    @Inject
    private UserManager userManager;

    @Inject
    private TokenManager tokenManager;

    @GET
    @Path(ADMIN_USER_NAME)
    @PermitAll
    @Operation(
            summary = "Generate admin token",
            description = "Generate admin token and save it locally to a file" +
                    ". Only the one who has access to the server (i.e. admin) is able to access the generated token"
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "409",
                            description = "Admin token already exists.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    ),
                    @APIResponse(
                            responseCode = "204",
                            description = "Admin token has been created."
                    )
            }
    )
    public Response createAdminToken() {
        logger.debug("admin token file path: {}", config.getAdminTokenPath());
        final File file = new File(config.getAdminTokenPath());
        final boolean isFileExist = file.exists() && !file.isDirectory();
        if (isFileExist) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ClientError().setMessage(ClientErrorMessage.ADMIN_TOKEN_ALREADY_EXIST))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        final User admin = ensureAdminUserExists();
        final String adminToken = generateAdminToken(admin);
        writeAdminToken(adminToken, file);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({ADMIN, TOKEN_CREATOR})
    @Operation(
            summary = "Generate a JWT token.",
            description = "Generate JWT token with a given validity duration" +
                    " for a given user and grant him given roles."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "409",
                            description = "The given user does not exist.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    ),
                    @APIResponse(
                            responseCode = "200",
                            description = "The token has been created.",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN)
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response create(
            @RequestBody(
                    description = "Information needed for creating a token.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CreateTokenRequest.class)
                    )
            )
            @Valid final CreateTokenRequest createTokenRequest
    ) {
        final Optional<User> user = userManager.findUser(createTokenRequest.getUserName());
        if (!user.isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ClientError().setMessage(ClientErrorMessage.USER_NOT_FOUND))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        final String token = tokenManager.createToken(createTokenRequest, user.get());
        return Response.ok(token).type(MediaType.TEXT_PLAIN).build();
    }

    private User ensureAdminUserExists() {
        final Optional<User> existingUser = userManager.findUser(ADMIN_USER_NAME);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        final User newUser = userManager.createUser(ADMIN_USER_NAME, ADMIN_USER_EMAIL, null);
        logger.debug("created admin user");
        return newUser;
    }

    private String generateAdminToken(final User admin) {
        final CreateTokenRequest createTokenRequest = new CreateTokenRequest()
                .setUserName(ADMIN_USER_NAME)
                .setRoles(Collections.singleton(ADMIN))
                .setValidityDurationMillis(Duration.ofDays(1000).toMillis());
        return tokenManager.createToken(createTokenRequest, admin);
    }

    private void writeAdminToken(final String adminToken, final File outputFile) {
        try (final PrintWriter out = new PrintWriter(outputFile)) {
            out.println(adminToken);
        } catch (final FileNotFoundException e) {
            throw new IllegalStateException("failed to create admin token", e);
        }
        logger.debug("admin token has been created");
    }

    private static final class ClientErrorMessage {

        static final String ADMIN_TOKEN_ALREADY_EXIST = "admin token already exists";
        static final String USER_NOT_FOUND = "user does not exist";

        private ClientErrorMessage() {
        }
    }
}
