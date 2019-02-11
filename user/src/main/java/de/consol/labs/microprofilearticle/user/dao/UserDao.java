package de.consol.labs.microprofilearticle.user.dao;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.user.entity.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.Optional;

@ApplicationScoped
@LogInputsOutputsAndExceptions
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<UserEntity> find(final String name) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, name));
    }

    public UserEntity create(final String name, final String email, final String createdBy) {
        final UserEntity user = new UserEntity()
                .setName(name)
                .setEmail(email)
                .setCreatedAt(Instant.now())
                .setCreatedBy(createdBy);
        entityManager.persist(user);
        return user;
    }
}
