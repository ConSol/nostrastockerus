package de.consol.labs.microprofilearticle.user.api;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.common.openapi.AppSecurityScheme;
import de.consol.labs.microprofilearticle.common.openapi.ClientError;
import de.consol.labs.microprofilearticle.common.openapi.DataFormat;
import de.consol.labs.microprofilearticle.user.manager.UserManager;
import de.consol.labs.microprofilearticle.user.model.User;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Optional;

import static de.consol.labs.microprofilearticle.common.rbac.SecurityRole.*;

@RequestScoped
@Path("user")
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = AppSecurityScheme.JWT,
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
})
@LogInputsOutputsAndExceptions
public class UserResource {

    @Inject
    private JsonWebToken jwt;

    @Inject
    private UserManager userManager;

    @GET
    @Path("{name}")
    @RolesAllowed({ADMIN, USER_CREATOR, USER_READER})
    @Operation(
            summary = "Get user information.",
            description = "Retrieve user information."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "404",
                            description = "The user does not exist.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    ),
                    @APIResponse(
                            responseCode = "200",
                            description = "User information.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response getUser(
            @PathParam("name") @NotBlank
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "User name.",
                    required = true
            )
            @Valid final String name
    ) {
        final Optional<User> user = userManager.findUser(name);
        if (!user.isPresent()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ClientError().setMessage(ClientErrorMessage.USER_NOT_FOUND))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.ok(user.get())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @RolesAllowed({ADMIN, USER_CREATOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Create a new user.",
            description = "Create a new user."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "409",
                            description = "The user already exists.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    ),
                    @APIResponse(
                            responseCode = "201",
                            description = "User has been created.",
                            headers = {
                                    @Header(
                                            name = HttpHeaders.LOCATION,
                                            description = "The URL under which the created user's data can be obtained.",
                                            required = true,
                                            schema = @Schema(type = SchemaType.STRING, format = DataFormat.URI)
                                    )
                            }
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response createUser(
            @RequestBody(
                    description = "User data.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = User.class)
                    )
            )
            @Valid final User user
    ) {
        if (userManager.findUser(user.getName()).isPresent()) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(new ClientError().setMessage(ClientErrorMessage.USER_ALREADY_EXIST))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        userManager.createUser(user.getName(), user.getEmail(), jwt.getName());
        final URI uri = UriBuilder.fromResource(UserResource.class).path("{name}").build(user.getName());
        return Response.created(uri).build();
    }

    private static final class ClientErrorMessage {

        static final String USER_NOT_FOUND = "user does not exist";
        static final String USER_ALREADY_EXIST = "user already exists";

        private ClientErrorMessage() {
        }
    }
}
