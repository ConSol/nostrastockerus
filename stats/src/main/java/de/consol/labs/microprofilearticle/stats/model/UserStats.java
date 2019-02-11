package de.consol.labs.microprofilearticle.stats.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "User statistics.")
public class UserStats {

    @Schema(
            description = "User name.",
            required = true,
            minLength = 3,
            maxLength = 100,
            example = "bob"
    )
    @NotBlank
    @Size(min = 3, max = 100)
    private String userName;

    @Schema(
            description = "Number of prophecies created.",
            required = true,
            minimum = "0",
            example = "120"
    )
    @NotNull
    @Min(0)
    private Long numberOfPropheciesCreated = 0L;

    @Schema(
            description = "Number of prophecies that has been checked.",
            required = true,
            minimum = "0",
            example = "100"
    )
    @NotNull
    @Min(0)
    private Long numberOfPropheciesChecked = 0L;

    @Schema(
            description = "Number of prophecies (out of those that has been checked) that has been fulfilled.",
            required = true,
            minimum = "0",
            example = "42"
    )
    @NotNull
    @Min(0)
    private Long numberOfPropheciesFulfilled = 0L;

    @Schema(
            description = "Precision of the user = number of prophecies fulfilled / number of checked prophecies.",
            minimum = "0.0",
            maximum = "1.0",
            example = "0.42"
    )
    @Min(0)
    @Max(1)
    private BigDecimal precision;

    @Schema(
            description = "User information.",
            required = true,
            implementation = UserInfo.class
    )
    @NotNull
    private UserInfo userInfo;

    public String getUserName() {
        return userName;
    }

    public UserStats setUserName(final String userName) {
        this.userName = userName;
        return this;
    }

    public Long getNumberOfPropheciesCreated() {
        return numberOfPropheciesCreated;
    }

    public UserStats setNumberOfPropheciesCreated(final Long numberOfPropheciesCreated) {
        this.numberOfPropheciesCreated = numberOfPropheciesCreated;
        return this;
    }

    public Long getNumberOfPropheciesChecked() {
        return numberOfPropheciesChecked;
    }

    public UserStats setNumberOfPropheciesChecked(final Long numberOfPropheciesChecked) {
        this.numberOfPropheciesChecked = numberOfPropheciesChecked;
        return this;
    }

    public Long getNumberOfPropheciesFulfilled() {
        return numberOfPropheciesFulfilled;
    }

    public UserStats setNumberOfPropheciesFulfilled(final Long numberOfPropheciesFulfilled) {
        this.numberOfPropheciesFulfilled = numberOfPropheciesFulfilled;
        return this;
    }

    public BigDecimal getPrecision() {
        return precision;
    }

    public UserStats setPrecision(final BigDecimal precision) {
        this.precision = precision;
        return this;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public UserStats setUserInfo(final UserInfo userInfo) {
        this.userInfo = userInfo;
        return this;
    }
}
