package de.consol.labs.microprofilearticle.prophecy.dao;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.entity.VoteEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
@LogInputsOutputsAndExceptions
public class VoteDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void create(final VoteEntity voteEntity) {
        entityManager.persist(voteEntity);
    }

    public void remove(final VoteEntity voteEntity) {
        entityManager.remove(voteEntity);
    }
}
