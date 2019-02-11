package de.consol.labs.microprofilearticle.prophecy.dao;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.entity.ProphecyEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@LogInputsOutputsAndExceptions
public class ProphecyDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ProphecyEntity create(final ProphecyEntity prophecy) {
        entityManager.persist(prophecy);
        return prophecy;
    }

    public Optional<ProphecyEntity> find(final long id) {
        return Optional.ofNullable(entityManager.find(ProphecyEntity.class, id));
    }

    public List<ProphecyEntity> findProphecies(final Instant expectedFrom, final Instant expectedTo) {
        return entityManager.createNamedQuery(ProphecyEntity.Q_PROPHECY_FIND_IN_TIME_WINDOW, ProphecyEntity.class)
                .setParameter("expectedFrom", expectedFrom)
                .setParameter("expectedTo", expectedTo)
                .getResultList();
    }

    public List<ProphecyEntity> getPropheciesCreatedByUser(final String userName) {
        return entityManager.createNamedQuery(ProphecyEntity.Q_PROPHECY_FIND_BY_CREATED_BY, ProphecyEntity.class)
                .setParameter("userName", userName)
                .getResultList();
    }
}
