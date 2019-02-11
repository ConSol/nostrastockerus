package de.consol.labs.microprofilearticle.user.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Schema(description = "Information needed for creating a token.")
public class CreateTokenRequest {

    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 100;

    private static final int MIN_VALIDITY_MILLIS = 60000; // 1 minute
    private static final int MAX_VALIDITY_MILLIS = 864000000; // 10 days

    @Schema(
            description = "User name.",
            required = true,
            minLength = MIN_NAME_LENGTH,
            maxLength = MAX_NAME_LENGTH,
            example = "bob"
    )
    @NotBlank
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String userName;

    @Schema(
            description = "User roles.",
            example = "[ \"chef\", \"waiter\", \"bartender\", \"butcher\" ]"
    )
    private Set<String> roles = new HashSet<>();

    @Schema(
            description = "Desired validity duration of the token in milliseconds.",
            defaultValue = "86400000",
            minimum = "60000",
            maximum = "864000000",
            example = "100000"
    )
    @Min(MIN_VALIDITY_MILLIS)
    @Max(MAX_VALIDITY_MILLIS)
    private long validityDurationMillis = Duration.ofDays(1).toMillis();

    public String getUserName() {
        return userName;
    }

    public CreateTokenRequest setUserName(final String userName) {
        this.userName = userName;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public CreateTokenRequest setRoles(final Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public long getValidityDurationMillis() {
        return validityDurationMillis;
    }

    public CreateTokenRequest setValidityDurationMillis(final long validityDurationMillis) {
        this.validityDurationMillis = validityDurationMillis;
        return this;
    }
}
