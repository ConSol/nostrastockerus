package de.consol.labs.microprofilearticle.stats.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_prophecy_revelation")
@NamedQueries({
        @NamedQuery(
                name = ProphecyRevelationEntity.Q_PROPHECY_REVELATION_FIND_BY_IDS,
                query = "SELECT p FROM ProphecyRevelationEntity p" +
                        " WHERE p.prophecyId in :prophecyIds"
        )
})
public class ProphecyRevelationEntity {

    public static final String Q_PROPHECY_REVELATION_FIND_BY_IDS = "Q_PROPHECY_REVELATION_FIND_BY_IDS";

    @Id
    @Column(name = "prophecy_id", nullable = false)
    private Long prophecyId;

    @Column(name = "stock_real_value", nullable = false, precision = 12, scale = 2)
    private BigDecimal stockRealValue;

    @Column(name = "is_fulfilled", nullable = false)
    private Boolean isFulfilled;

    public Long getProphecyId() {
        return prophecyId;
    }

    public ProphecyRevelationEntity setProphecyId(final Long prophecyId) {
        this.prophecyId = prophecyId;
        return this;
    }

    public BigDecimal getStockRealValue() {
        return stockRealValue;
    }

    public ProphecyRevelationEntity setStockRealValue(final BigDecimal stockRealValue) {
        this.stockRealValue = stockRealValue;
        return this;
    }

    public Boolean getFulfilled() {
        return isFulfilled;
    }

    public ProphecyRevelationEntity setFulfilled(final Boolean fulfilled) {
        isFulfilled = fulfilled;
        return this;
    }

    @Override
    public String toString() {
        return "ProphecyRevelationEntity{" +
                "prophecyId=" + prophecyId +
                ", stockRealValue=" + stockRealValue +
                ", isFulfilled=" + isFulfilled +
                '}';
    }
}
