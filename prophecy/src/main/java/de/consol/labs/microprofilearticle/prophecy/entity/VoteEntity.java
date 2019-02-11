package de.consol.labs.microprofilearticle.prophecy.entity;

import de.consol.labs.microprofilearticle.common.persistence.Instant2TimestampAttributeConverter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "t_vote",
        indexes = {
                @Index(name = VoteEntity.IDX_VOTE_CREATED_BY, columnList = "created_by"),
                @Index(name = VoteEntity.IDX_VOTE_CREATED_AT, columnList = "created_at"),
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"created_by", "prophecy_id"})
        }
)
public class VoteEntity {

    public static final String IDX_VOTE_CREATED_BY = "IDX_VOTE_CREATED_BY";
    public static final String IDX_VOTE_CREATED_AT = "IDX_VOTE_CREATED_AT";

    private static final String SEQUENCE_NAME = "seq_vote_id";

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

    @Column(name = "vote_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VoteType voteType;

    @ManyToOne(
            optional = false,
            cascade = {CascadeType.ALL}
    )
    @JoinColumn(
            name = "prophecy_id",
            nullable = false
    )
    private ProphecyEntity prophecy;

    public Long getId() {
        return id;
    }

    public VoteEntity setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public VoteEntity setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public VoteEntity setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public VoteEntity setVoteType(final VoteType voteType) {
        this.voteType = voteType;
        return this;
    }

    public ProphecyEntity getProphecy() {
        return prophecy;
    }

    public VoteEntity setProphecy(final ProphecyEntity prophecy) {
        this.prophecy = prophecy;
        return this;
    }
}
