package de.consol.labs.microprofilearticle.stats.mapper;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.stats.entity.ProphecyRevelationEntity;
import de.consol.labs.microprofilearticle.stats.model.ProphecyRevelation;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@LogInputsOutputsAndExceptions(loggingLevel = LogInputsOutputsAndExceptions.LOGGING_LEVEL_TRACE)
public class ProphecyRevelationMapper {

    public ProphecyRevelationEntity model2Entity(final ProphecyRevelation model) {
        return new ProphecyRevelationEntity()
                .setProphecyId(model.getProphecyId())
                .setStockRealValue(model.getStockRealValue())
                .setFulfilled(model.getFulfilled());
    }

    public ProphecyRevelation entity2Model(final ProphecyRevelationEntity entity) {
        return new ProphecyRevelation()
                .setProphecyId(entity.getProphecyId())
                .setStockRealValue(entity.getStockRealValue())
                .setFulfilled(entity.getFulfilled());
    }
}
