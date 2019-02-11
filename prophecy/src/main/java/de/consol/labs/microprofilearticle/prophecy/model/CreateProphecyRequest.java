package de.consol.labs.microprofilearticle.prophecy.model;

import de.consol.labs.microprofilearticle.prophecy.entity.ProphecyType;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Information needed to create a prophecy.")
public class CreateProphecyRequest {

    @Schema(
            description = "Stock name.",
            required = true,
            minLength = 1,
            maxLength = 100,
            example = "GOOG"
    )
    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private String stockName;

    @Schema(
            description = "Expected future stock value.",
            required = true,
            minimum = "0.0",
            example = "120.45"
    )
    @NotNull
    @Min(0)
    private BigDecimal stockExpectedValue;

    @Schema(
            description = "Type of prophecy.",
            required = true,
            implementation = ProphecyType.class
    )
    @NotNull
    private ProphecyType prophecyType;

    @Schema(
            description = "Moment in time when the prophecy is expected to be fulfilled.",
            required = true,
            type = SchemaType.STRING,
            implementation = String.class,
            format = DataFormat.TIMESTAMP,
            example = "\"2019-03-21T14:30:44.406Z\""
    )
    @NotNull
    private Instant expectedAt;

    public String getStockName() {
        return stockName;
    }

    public CreateProphecyRequest setStockName(final String stockName) {
        this.stockName = stockName;
        return this;
    }

    public BigDecimal getStockExpectedValue() {
        return stockExpectedValue;
    }

    public CreateProphecyRequest setStockExpectedValue(final BigDecimal stockExpectedValue) {
        this.stockExpectedValue = stockExpectedValue;
        return this;
    }

    public ProphecyType getProphecyType() {
        return prophecyType;
    }

    public CreateProphecyRequest setProphecyType(final ProphecyType prophecyType) {
        this.prophecyType = prophecyType;
        return this;
    }

    public Instant getExpectedAt() {
        return expectedAt;
    }

    public CreateProphecyRequest setExpectedAt(final Instant expectedAt) {
        this.expectedAt = expectedAt;
        return this;
    }

    @Override
    public String toString() {
        return "CreateProphecyRequest{" +
                "stockName='" + stockName + '\'' +
                ", stockExpectedValue=" + stockExpectedValue +
                ", prophecyType=" + prophecyType +
                ", expectedAt=" + expectedAt +
                '}';
    }
}
