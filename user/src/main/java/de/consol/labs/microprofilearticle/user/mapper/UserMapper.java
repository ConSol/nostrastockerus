package de.consol.labs.microprofilearticle.user.mapper;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.user.entity.UserEntity;
import de.consol.labs.microprofilearticle.user.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@LogInputsOutputsAndExceptions(loggingLevel = LogInputsOutputsAndExceptions.LOGGING_LEVEL_TRACE)
public class UserMapper {

    public User entity2Model(final UserEntity entity) {
        return new User()
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setCreatedAt(entity.getCreatedAt())
                .setCreatedBy(entity.getCreatedBy());
    }
}
