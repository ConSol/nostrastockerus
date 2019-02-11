package de.consol.labs.microprofilearticle.prophecy.model;

import de.consol.labs.microprofilearticle.prophecy.entity.ProphecyType;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Prophecy information.")
public class Prophecy {

    @Schema(
            description = "Prophecy ID.",
            required = true,
            example = "25"
    )
    private Long id;

    @Schema(
            description = "Name of the user which has created the prophecy.",
            required = true,
            minLength = 3,
            maxLength = 100,
            example = "bob"
    )
    private String createdBy;

    @Schema(
            description = "Timestamp when the prophecy was created.",
            required = true,
            type = SchemaType.STRING,
            implementation = String.class,
            format = DataFormat.TIMESTAMP,
            example = "\"2019-03-21T14:30:44.406Z\""
    )
    private Instant createdAt;

    @Schema(
            description = "Stock name.",
            required = true,
            minLength = 1,
            maxLength = 100,
            example = "GOOG"
    )
    private String stockName;

    @Schema(
            description = "Expected future stock value.",
            required = true,
            example = "120.45",
            minimum = "0.0"
    )
    @Min(0)
    private BigDecimal stockExpectedValue;

    @Schema(
            description = "Type of prophecy.",
            required = true,
            implementation = ProphecyType.class,
            example = "BULL"
    )
    private ProphecyType prophecyType;

    @Schema(
            description = "Moment in time when the prophecy is expected to be fulfilled.",
            required = true,
            type = SchemaType.STRING,
            implementation = String.class,
            format = DataFormat.TIMESTAMP,
            example = "\"2019-03-21T14:30:44.406Z\""
    )
    private Instant expectedAt;

    @Schema(
            description = "Number of votes for the prophecy.",
            required = true,
            minimum = "0",
            example = "10"
    )
    @NotNull
    @Min(0)
    private Integer votesFor;

    @Schema(
            description = "Number of votes against the prophecy.",
            required = true,
            minimum = "0",
            example = "5"
    )
    @NotNull
    @Min(0)
    private Integer votesAgainst;

    public Long getId() {
        return id;
    }

    public Prophecy setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Prophecy setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Prophecy setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getStockName() {
        return stockName;
    }

    public Prophecy setStockName(final String stockName) {
        this.stockName = stockName;
        return this;
    }

    public BigDecimal getStockExpectedValue() {
        return stockExpectedValue;
    }

    public Prophecy setStockExpectedValue(final BigDecimal stockExpectedValue) {
        this.stockExpectedValue = stockExpectedValue;
        return this;
    }

    public ProphecyType getProphecyType() {
        return prophecyType;
    }

    public Prophecy setProphecyType(final ProphecyType prophecyType) {
        this.prophecyType = prophecyType;
        return this;
    }

    public Instant getExpectedAt() {
        return expectedAt;
    }

    public Prophecy setExpectedAt(final Instant expectedAt) {
        this.expectedAt = expectedAt;
        return this;
    }

    public Integer getVotesFor() {
        return votesFor;
    }

    public Prophecy setVotesFor(final Integer votesFor) {
        this.votesFor = votesFor;
        return this;
    }

    public Integer getVotesAgainst() {
        return votesAgainst;
    }

    public Prophecy setVotesAgainst(final Integer votesAgainst) {
        this.votesAgainst = votesAgainst;
        return this;
    }
}
