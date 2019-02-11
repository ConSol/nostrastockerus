package de.consol.labs.microprofilearticle.prophecy.entity;

import de.consol.labs.microprofilearticle.common.persistence.Instant2TimestampAttributeConverter;
import de.consol.labs.microprofilearticle.prophecy.validation.ValidateProphecyTimestamps;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "t_prophecy",
        indexes = {
                @Index(name = ProphecyEntity.IDX_PROPHECY_CREATED_BY, columnList = "created_by"),
                @Index(name = ProphecyEntity.IDX_PROPHECY_CREATED_AT, columnList = "created_at"),
                @Index(name = ProphecyEntity.IDX_PROPHECY_STOCK_NAME, columnList = "stock_name"),
                @Index(name = ProphecyEntity.IDX_PROPHECY_EXPECTED_AT, columnList = "expected_at")
        }
)
@NamedQueries({
        @NamedQuery(
                name = ProphecyEntity.Q_PROPHECY_FIND_IN_TIME_WINDOW,
                query = "SELECT p FROM ProphecyEntity p" +
                        " WHERE :expectedFrom <= p.expectedAt AND p.expectedAt <= :expectedTo"
        ),
        @NamedQuery(
                name = ProphecyEntity.Q_PROPHECY_FIND_BY_CREATED_BY,
                query = "SELECT p FROM ProphecyEntity p WHERE p.createdBy = :userName"
        )
})
@ValidateProphecyTimestamps
public class ProphecyEntity {

    public static final String IDX_PROPHECY_CREATED_BY = "IDX_PROPHECY_CREATED_BY";
    public static final String IDX_PROPHECY_CREATED_AT = "IDX_PROPHECY_CREATED_AT";
    public static final String IDX_PROPHECY_STOCK_NAME = "IDX_PROPHECY_STOCK_NAME";
    public static final String IDX_PROPHECY_EXPECTED_AT = "IDX_PROPHECY_EXPECTED_AT";

    public static final String Q_PROPHECY_FIND_IN_TIME_WINDOW = "Q_PROPHECY_FIND_IN_TIME_WINDOW";
    public static final String Q_PROPHECY_FIND_BY_CREATED_BY = "Q_PROPHECY_FIND_BY_CREATED_BY";

    private static final String SEQUENCE_NAME = "seq_prophecy_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 10)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "created_by", length = 100, nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = Instant2TimestampAttributeConverter.class)
    private Instant createdAt;

    @Column(name = "stock_name", length = 100, nullable = false)
    private String stockName;

    @Column(name = "stock_expected_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal stockExpectedValue;

    @Column(name = "prophecy_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProphecyType prophecyType;

    @Column(name = "expected_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = Instant2TimestampAttributeConverter.class)
    private Instant expectedAt;

    @OneToMany(
            mappedBy = "prophecy",
            cascade = {CascadeType.ALL}
    )
    private List<VoteEntity> votes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public ProphecyEntity setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ProphecyEntity setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public ProphecyEntity setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getStockName() {
        return stockName;
    }

    public ProphecyEntity setStockName(final String stockName) {
        this.stockName = stockName;
        return this;
    }

    public BigDecimal getStockExpectedValue() {
        return stockExpectedValue;
    }

    public ProphecyEntity setStockExpectedValue(final BigDecimal stockExpectedValue) {
        this.stockExpectedValue = stockExpectedValue;
        return this;
    }

    public ProphecyType getProphecyType() {
        return prophecyType;
    }

    public ProphecyEntity setProphecyType(final ProphecyType prophecyType) {
        this.prophecyType = prophecyType;
        return this;
    }

    public Instant getExpectedAt() {
        return expectedAt;
    }

    public ProphecyEntity setExpectedAt(final Instant expectedAt) {
        this.expectedAt = expectedAt;
        return this;
    }

    public List<VoteEntity> getVotes() {
        return votes;
    }

    public ProphecyEntity setVotes(final List<VoteEntity> votes) {
        this.votes = votes;
        return this;
    }
}
