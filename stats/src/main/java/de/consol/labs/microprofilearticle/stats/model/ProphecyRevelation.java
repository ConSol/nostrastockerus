package de.consol.labs.microprofilearticle.stats.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Information whether a prophecy fulfilled or not.")
public class ProphecyRevelation {

    @Schema(
            description = "Prophecy ID.",
            required = true,
            example = "12"
    )
    @NotNull
    private Long prophecyId;

    @Schema(
            description = "Real value of the prophecy's stock.",
            required = true,
            example = "12.5"
    )
    @NotNull
    private BigDecimal stockRealValue;

    @Schema(
            description = "Revelation of the prophecy indicating whether it has been fulfilled or not.",
            required = true,
            example = "true"
    )
    @NotNull
    private Boolean isFulfilled;

    public Long getProphecyId() {
        return prophecyId;
    }

    public ProphecyRevelation setProphecyId(final Long prophecyId) {
        this.prophecyId = prophecyId;
        return this;
    }

    public BigDecimal getStockRealValue() {
        return stockRealValue;
    }

    public ProphecyRevelation setStockRealValue(final BigDecimal stockRealValue) {
        this.stockRealValue = stockRealValue;
        return this;
    }

    public Boolean getFulfilled() {
        return isFulfilled;
    }

    public ProphecyRevelation setFulfilled(final Boolean fulfilled) {
        isFulfilled = fulfilled;
        return this;
    }
}
