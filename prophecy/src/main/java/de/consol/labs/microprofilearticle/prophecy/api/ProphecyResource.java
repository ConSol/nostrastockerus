package de.consol.labs.microprofilearticle.prophecy.api;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.common.openapi.AppSecurityScheme;
import de.consol.labs.microprofilearticle.prophecy.Config;
import de.consol.labs.microprofilearticle.prophecy.manager.ProphecyManager;
import de.consol.labs.microprofilearticle.prophecy.model.ClientError;
import de.consol.labs.microprofilearticle.prophecy.model.CreateProphecyRequest;
import de.consol.labs.microprofilearticle.prophecy.model.DataFormat;
import de.consol.labs.microprofilearticle.prophecy.model.Prophecy;
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
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static de.consol.labs.microprofilearticle.common.rbac.SecurityRole.*;

@RequestScoped
@Path("prophecy")
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = AppSecurityScheme.JWT,
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
})
@LogInputsOutputsAndExceptions
public class ProphecyResource {

    @Inject
    private Config config;

    @Inject
    private JsonWebToken jwt;

    @Inject
    private ProphecyManager prophecyManager;

    private final Random random = new Random();

    @POST
    @RolesAllowed({ADMIN, PROPHECY_CREATOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createProphecy",
            summary = "Create a new prophecy.",
            description = "Create a new prophecy."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "201",
                            description = "Prophecy has been created.",
                            headers = {
                                    @Header(
                                            name = HttpHeaders.LOCATION,
                                            description = "The URL under which the created prophecy's data can be obtained.",
                                            required = true,
                                            schema = @Schema(type = SchemaType.STRING, format = DataFormat.URI)
                                    )
                            }
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response createProphecy(
            @RequestBody(
                    description = "Prophecy data.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CreateProphecyRequest.class)
                    )
            )
            @Valid final CreateProphecyRequest createProphecyRequest
    ) {
        final Prophecy prophesy = new Prophecy()
                .setCreatedBy(jwt.getName())
                .setCreatedAt(Instant.now())
                .setStockName(createProphecyRequest.getStockName())
                .setStockExpectedValue(createProphecyRequest.getStockExpectedValue())
                .setProphecyType(createProphecyRequest.getProphecyType())
                .setExpectedAt(createProphecyRequest.getExpectedAt());
        final long id = prophecyManager.createProphecy(prophesy);
        final URI uri = UriBuilder.fromResource(ProphecyResource.class).path("{id}").build(id);
        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({ADMIN, PROPHECY_CREATOR, PROPHECY_READER})
    @Operation(
            operationId = "getProphecyInformation",
            summary = "Get prophecy information.",
            description = "Retrieve prophecy information."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "404",
                            description = "The prophecy does not exist.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    ),
                    @APIResponse(
                            responseCode = "200",
                            description = "Prophecy information.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = Prophecy.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response getProphecy(
            @PathParam("id")
            @NotNull
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Prophecy ID.",
                    required = true
            )
            @Valid final Long id
    ) {
        final Optional<Prophecy> prophecy = prophecyManager.findProphecy(id);
        if (!prophecy.isPresent()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(new ClientError().setMessage(ClientErrorMessage.PROPHECY_NOT_FOUND))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.ok(prophecy)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @RolesAllowed({ADMIN, PROPHECY_CREATOR, PROPHECY_READER})
    @Operation(
            operationId = "findPropheciesByExpectedAt",
            summary = "Find prophecies in the given time window.",
            description = "Find prophecies in the given time window."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Found prophecies.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            type = SchemaType.ARRAY,
                                            implementation = Prophecy.class
                                    )
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response getProphecies(
            @QueryParam("expectedFrom")
            @NotNull
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "Earliest time point when a prophecy is expected to fulfill.",
                    required = true,
                    schema = @Schema(
                            type = SchemaType.STRING,
                            implementation = String.class,
                            format = DataFormat.TIMESTAMP,
                            example = "\"2019-01-21T14:30:44.406Z\""
                    )
            )
            @Valid final Instant expectedFrom,
            @QueryParam("expectedTo")
            @NotNull
            @Parameter(
                    in = ParameterIn.QUERY,
                    description = "Latest time point when a prophecy is expected to fulfill.",
                    required = true,
                    schema = @Schema(
                            type = SchemaType.STRING,
                            implementation = String.class,
                            format = DataFormat.TIMESTAMP,
                            example = "\"2019-03-21T15:30:44.406Z\""
                    )
            )
            @Valid final Instant expectedTo
    ) {
        final List<Prophecy> prophecies = prophecyManager.findProphecies(expectedFrom, expectedTo);
        return Response.ok(prophecies)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @GET
    @Path("createdBy/{userName}")
    @RolesAllowed({ADMIN, PROPHECY_CREATOR, PROPHECY_READER})
    @Operation(
            operationId = "getPropheciesCreatedByUser",
            summary = "Get prophecies created by user.",
            description = "Retrieve prophecies created by user."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Prophecies created by the user.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(
                                            type = SchemaType.ARRAY,
                                            implementation = Prophecy.class
                                    )
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response getPropheciesCreatedByUser(
            @PathParam("userName")
            @NotBlank
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "User name.",
                    required = true
            )
            @Valid final String userName
    ) {
        if (random.nextDouble() < 0.5) {
            throw new RuntimeException("test failure");
        }
        return Response.ok(prophecyManager.getPropheciesCreatedByUser(userName))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private static final class ClientErrorMessage {

        static final String PROPHECY_NOT_FOUND = "prophecy does not exist";

        private ClientErrorMessage() {
        }
    }
}
