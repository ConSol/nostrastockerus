package de.consol.labs.microprofilearticle.user.entity;

import de.consol.labs.microprofilearticle.common.persistence.Instant2TimestampAttributeConverter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "t_user",
        indexes = {
                @Index(name = UserEntity.IDX_USER_EMAIL, columnList = "email", unique = true),
                @Index(name = UserEntity.IDX_USER_CREATED_AT, columnList = "created_at"),
                @Index(name = UserEntity.IDX_USER_CREATED_BY, columnList = "created_by"),
        }
)
public class UserEntity {

    public static final String IDX_USER_EMAIL = "IDX_USER_EMAIL";
    public static final String IDX_USER_CREATED_AT = "IDX_USER_CREATED_AT";
    public static final String IDX_USER_CREATED_BY = "IDX_USER_CREATED_BY";

    @Id
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = Instant2TimestampAttributeConverter.class)
    private Instant createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    public String getName() {
        return name;
    }

    public UserEntity setName(final String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(final String email) {
        this.email = email;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public UserEntity setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public UserEntity setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}
