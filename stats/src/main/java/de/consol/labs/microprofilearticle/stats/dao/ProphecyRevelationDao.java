package de.consol.labs.microprofilearticle.stats.dao;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.stats.entity.ProphecyRevelationEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@LogInputsOutputsAndExceptions(loggingLevel = LogInputsOutputsAndExceptions.LOGGING_LEVEL_TRACE)
public class ProphecyRevelationDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ProphecyRevelationEntity create(final ProphecyRevelationEntity stats) {
        entityManager.persist(stats);
        return stats;
    }

    public Optional<ProphecyRevelationEntity> find(final long prophecyId) {
        return Optional.ofNullable(entityManager.find(ProphecyRevelationEntity.class, prophecyId));
    }

    public List<ProphecyRevelationEntity> findByIds(final List<Long> prophecyIds) {
        return entityManager
                .createNamedQuery(ProphecyRevelationEntity.Q_PROPHECY_REVELATION_FIND_BY_IDS, ProphecyRevelationEntity.class)
                .setParameter("prophecyIds", prophecyIds)
                .getResultList();
    }
}
