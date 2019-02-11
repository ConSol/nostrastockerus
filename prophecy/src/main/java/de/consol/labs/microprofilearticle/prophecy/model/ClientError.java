package de.consol.labs.microprofilearticle.prophecy.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(description = "Information returned to client in case of a client error.")
public class ClientError {

    @Schema(
            description = "Error message explaining why HTTP request could not be fulfilled.",
            required = true,
            example = "prophecy not found"
    )
    @NotBlank
    private String message;

    public String getMessage() {
        return message;
    }

    public ClientError setMessage(final String message) {
        this.message = message;
        return this;
    }
}
