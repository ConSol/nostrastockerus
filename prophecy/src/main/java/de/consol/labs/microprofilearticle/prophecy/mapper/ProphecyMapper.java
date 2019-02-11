package de.consol.labs.microprofilearticle.prophecy.mapper;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.entity.ProphecyEntity;
import de.consol.labs.microprofilearticle.prophecy.entity.VoteEntity;
import de.consol.labs.microprofilearticle.prophecy.entity.VoteType;
import de.consol.labs.microprofilearticle.prophecy.model.Prophecy;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
@LogInputsOutputsAndExceptions(loggingLevel = LogInputsOutputsAndExceptions.LOGGING_LEVEL_TRACE)
public class ProphecyMapper {

    public ProphecyEntity model2Entity(final Prophecy model) {
        return new ProphecyEntity()
                .setId(model.getId())
                .setCreatedBy(model.getCreatedBy())
                .setCreatedAt(model.getCreatedAt())
                .setStockName(model.getStockName())
                .setStockExpectedValue(model.getStockExpectedValue())
                .setProphecyType(model.getProphecyType())
                .setExpectedAt(model.getExpectedAt());
    }

    public Prophecy entity2Model(final ProphecyEntity entity) {
        final Map<VoteType, List<VoteEntity>> groupedVotes = entity.getVotes()
                .stream()
                .collect(Collectors.groupingBy(VoteEntity::getVoteType));
        final int votesFor = groupedVotes.getOrDefault(VoteType.FOR, Collections.emptyList()).size();
        final int votesAgainst = groupedVotes.getOrDefault(VoteType.AGAINST, Collections.emptyList()).size();
        return new Prophecy()
                .setId(entity.getId())
                .setCreatedBy(entity.getCreatedBy())
                .setCreatedAt(entity.getCreatedAt())
                .setStockName(entity.getStockName())
                .setStockExpectedValue(entity.getStockExpectedValue())
                .setProphecyType(entity.getProphecyType())
                .setExpectedAt(entity.getExpectedAt())
                .setVotesFor(votesFor)
                .setVotesAgainst(votesAgainst);
    }
}
