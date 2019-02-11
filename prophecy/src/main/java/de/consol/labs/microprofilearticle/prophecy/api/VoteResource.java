package de.consol.labs.microprofilearticle.prophecy.api;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.common.openapi.AppSecurityScheme;
import de.consol.labs.microprofilearticle.prophecy.manager.VoteManager;
import de.consol.labs.microprofilearticle.prophecy.model.ClientError;
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

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static de.consol.labs.microprofilearticle.common.rbac.SecurityRole.*;

@RequestScoped
@Path("vote")
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = AppSecurityScheme.JWT,
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
})
@LogInputsOutputsAndExceptions
public class VoteResource {

    @Inject
    private JsonWebToken jwt;

    @Inject
    private VoteManager voteManager;

    @POST
    @Path("for/{prophecyId}")
    @RolesAllowed({ADMIN, PROPHECY_CREATOR, PROPHECY_READER})
    @Operation(
            summary = "Vote for a prophecy.",
            description = "Vote for a prophecy."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "204",
                            description = "Vote has been accepted."
                    ),
                    @APIResponse(
                            responseCode = "409",
                            description = "Vote cannot be accepted.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response voteForProphecy(
            @PathParam("prophecyId")
            @NotNull
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Prophecy ID.",
                    required = true
            )
            @Valid final Long prophecyId
    ) {
        return interpretVotingResult(voteManager.voteFor(prophecyId, jwt.getName()));
    }

    @POST
    @Path("against/{prophecyId}")
    @RolesAllowed({ADMIN, PROPHECY_CREATOR, PROPHECY_READER})
    @Operation(
            summary = "Vote against a prophecy.",
            description = "Vote against a prophecy."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "204",
                            description = "Vote has been accepted."
                    ),
                    @APIResponse(
                            responseCode = "409",
                            description = "Vote cannot be accepted.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response voteAgainstProphecy(
            @PathParam("prophecyId")
            @NotNull
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Prophecy ID.",
                    required = true
            )
            @Valid final Long prophecyId
    ) {
        return interpretVotingResult(voteManager.voteAgainst(prophecyId, jwt.getName()));
    }

    @DELETE
    @Path("{prophecyId}")
    @RolesAllowed({ADMIN, PROPHECY_CREATOR, PROPHECY_READER})
    @Operation(
            summary = "Delete previously created vote for a prophecy.",
            description = "Delete previously created vote for a prophecy or do nothing if it does not exist."
    )
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "204",
                            description = "Vote has been deleted (if it existed)."
                    ),
                    @APIResponse(
                            responseCode = "409",
                            description = "Vote cannot be deleted.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = ClientError.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = AppSecurityScheme.JWT)
    public Response abstain(
            @PathParam("prophecyId")
            @NotNull
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Prophecy ID.",
                    required = true
            )
            @Valid final Long prophecyId
    ) {
        return interpretVotingResult(voteManager.abstain(prophecyId, jwt.getName()));
    }

    private static Response interpretVotingResult(final Optional<ClientError> votingActionResult) {
        if (!votingActionResult.isPresent()) {
            return Response.noContent().build();
        }
        return Response
                .status(Response.Status.CONFLICT)
                .entity(votingActionResult.get())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
