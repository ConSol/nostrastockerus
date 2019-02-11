package de.consol.labs.microprofilearticle.user.model;

import de.consol.labs.microprofilearticle.common.openapi.DataFormat;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;

@Schema(description = "User data.")
public class User {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;

    private static final int MIN_EMAIL_LENGTH = 5;
    private static final int MAX_EMAIL_LENGTH = 100;

    @Schema(
            description = "User name.",
            required = true,
            minLength = MIN_NAME_LENGTH,
            maxLength = MAX_NAME_LENGTH,
            example = "bob"
    )
    @NotBlank
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String name;

    @Schema(
            description = "User email.",
            required = true,
            format = DataFormat.EMAIL,
            minLength = MIN_EMAIL_LENGTH,
            maxLength = MAX_EMAIL_LENGTH,
            example = "bob@test.org"
    )
    @Email
    @NotBlank
    @Size(min = MIN_EMAIL_LENGTH, max = MAX_EMAIL_LENGTH)
    private String email;

    @Schema(
            description = "Timestamp when the user was created.",
            type = SchemaType.STRING,
            implementation = String.class,
            format = DataFormat.TIMESTAMP,
            example = "\"2019-03-21T14:30:44.406Z\""
    )
    private Instant createdAt;

    @Schema(
            description = "User which created this user.",
            minLength = MIN_NAME_LENGTH,
            maxLength = MAX_NAME_LENGTH,
            example = "admin"
    )
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String createdBy;

    public String getName() {
        return name;
    }

    public User setName(final String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(final String email) {
        this.email = email;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public User setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}
