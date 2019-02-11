package de.consol.labs.microprofilearticle.common.health;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
@LogInputsOutputsAndExceptions
class DbPinger {

    private static final int NUMBER = 1;
    private static final String QUERY = String.format("SELECT %s", NUMBER);

    @Inject
    private Logger logger;

    @PersistenceContext
    protected EntityManager entityManager;

    boolean pingDb() {
        try {
            final Number number = (Number) entityManager.createNativeQuery(QUERY).getSingleResult();
            return number != null && number.intValue() == NUMBER;
        } catch (final Exception e) {
            logger.error("DB ping failed", e);
            return Boolean.FALSE;
        }
    }
}
