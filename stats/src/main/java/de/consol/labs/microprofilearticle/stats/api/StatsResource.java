package de.consol.labs.microprofilearticle.stats.api;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.common.openapi.AppSecurityScheme;
import de.consol.labs.microprofilearticle.common.openapi.ClientError;
import de.consol.labs.microprofilearticle.stats.manager.ProphecyCheckResult;
import de.consol.labs.microprofilearticle.stats.manager.StatsManager;
import de.consol.labs.microprofilearticle.stats.model.ProphecyRevelation;
import de.consol.labs.microprofilearticle.stats.model.UserStats;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("stats")
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = AppSecurityScheme.JWT,
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
})
@LogInputsOutputsAndExceptions
public class StatsResource {

    @Inject
    private JsonWebToken jwt;

    @Inject
    private StatsManager statsManager;

    @POST
    @Path("check")
    @Operation(
            operationId = "checkProphecies",
            summary = "Trigger checking of prophecies.",
            description = "Trigger checking of prophecies."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "204",
                            description = "Check has been done."
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response checkProphecies() {
        statsManager.checkProphecies();
        return Response.noContent().build();
    }

    @POST
    @Path("check/{prophecyId}")
    @Operation(
            operationId = "checkProphecy",
            summary = "Check the prophecy.",
            description = "Check the prophecy."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "409",
                            description = "The prophecy cannot be checked.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    ),
                    @APIResponse(
                            responseCode = "200",
                            description = "The prophecy has been checked.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ProphecyRevelation.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response checkProphecy(
            @PathParam("prophecyId") @NotNull
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Prophecy ID.",
                    required = true
            )
            @Valid final Long prophecyId
    ) {
        final ProphecyCheckResult result = statsManager.checkProphecy(prophecyId);
        final Response response;
        switch (result.getOutcome()) {
            case PROPHECY_DOES_NOT_EXIST:
                response = Response.status(Response.Status.CONFLICT)
                        .entity(new ClientError().setMessage("the prophecy does not exist"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
                break;
            case TOO_EARLY:
                response = Response.status(Response.Status.CONFLICT)
                        .entity(new ClientError().setMessage("it is too early to check the prophecy"))
                        .type(MediaType.APPLICATION_JSON)
                        .build();
                break;
            case CHECK_DONE:
                response = Response.ok(result.getProphecyRevelation())
                        .type(MediaType.APPLICATION_JSON)
                        .build();
                break;
            default:
                throw new IllegalStateException("unknown outcome: " + result.getOutcome());
        }
        return response;
    }

    @GET
    @Path("show")
    @Operation(
            operationId = "showStatsForCurrentUser",
            summary = "Gather stats for current user.",
            description = "Gather stats for current user."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Current user stats.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = UserStats.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response showStatsForCurrentUser() {
        return showStatsForUser(jwt.getName());
    }

    @GET
    @Path("show/{userName}")
    @Operation(
            operationId = "showStatsForUser",
            summary = "Gather stats for a user.",
            description = "Gather stats for a user."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "User stats.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = UserStats.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response showStatsForUser(
            @PathParam("userName") @NotNull
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Name of a user.",
                    required = true
            )
            @Valid final String userName
    ) {
        final UserStats stats = statsManager.getStatsForUser(userName);
        return Response.ok(stats, MediaType.APPLICATION_JSON).build();
    }
}
