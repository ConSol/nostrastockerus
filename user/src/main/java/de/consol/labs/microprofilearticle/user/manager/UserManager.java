package de.consol.labs.microprofilearticle.user.manager;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.user.dao.UserDao;
import de.consol.labs.microprofilearticle.user.mapper.UserMapper;
import de.consol.labs.microprofilearticle.user.model.User;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@ApplicationScoped
@LogInputsOutputsAndExceptions
public class UserManager {

    @Inject
    private UserDao userDao;

    @Inject
    private UserMapper userMapper;

    @Timed(name = "execution_time_of_find_user")
    public Optional<User> findUser(final String name) {
        return userDao.find(name).map(userMapper::entity2Model);
    }

    @Timed(name = "execution_time_of_create_user")
    public User createUser(final String user, final String email, final String createdBy) {
        return userMapper.entity2Model(userDao.create(user, email, createdBy));
    }
}
