package de.consol.labs.microprofilearticle.prophecy.manager;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.dao.ProphecyDao;
import de.consol.labs.microprofilearticle.prophecy.entity.ProphecyEntity;
import de.consol.labs.microprofilearticle.prophecy.mapper.ProphecyMapper;
import de.consol.labs.microprofilearticle.prophecy.model.Prophecy;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@ApplicationScoped
@LogInputsOutputsAndExceptions
public class ProphecyManager {

    @Inject
    private ProphecyMapper prophecyMapper;

    @Inject
    private ProphecyDao prophecyDao;

    @Timed(name = "execution_time_of_create_prophecy")
    public long createProphecy(final Prophecy model) {
        final ProphecyEntity entity = prophecyDao.create(prophecyMapper.model2Entity(model));
        return entity.getId();
    }

    @Timed(name = "execution_time_of_find_prophecy")
    public Optional<Prophecy> findProphecy(final long id) {
        return prophecyDao.find(id).map(prophecyMapper::entity2Model);
    }

    @Timed(name = "execution_time_of_find_prophecies")
    public List<Prophecy> findProphecies(final Instant expectedFrom, final Instant expectedTo) {
        return prophecyDao.findProphecies(expectedFrom, expectedTo)
                .stream()
                .map(prophecyMapper::entity2Model)
                .collect(Collectors.toList());
    }

    @Timed(name = "execution_time_of_find_prophecies_created_by_user")
    public List<Prophecy> getPropheciesCreatedByUser(final String userName) {
        return prophecyDao.getPropheciesCreatedByUser(userName)
                .stream()
                .map(prophecyMapper::entity2Model)
                .collect(Collectors.toList());
    }
}
