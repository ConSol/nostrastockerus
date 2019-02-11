package de.consol.labs.microprofilearticle.prophecy.manager;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.Config;
import de.consol.labs.microprofilearticle.prophecy.dao.ProphecyDao;
import de.consol.labs.microprofilearticle.prophecy.dao.VoteDao;
import de.consol.labs.microprofilearticle.prophecy.entity.ProphecyEntity;
import de.consol.labs.microprofilearticle.prophecy.entity.VoteEntity;
import de.consol.labs.microprofilearticle.prophecy.entity.VoteType;
import de.consol.labs.microprofilearticle.prophecy.model.ClientError;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;

@Transactional
@ApplicationScoped
@LogInputsOutputsAndExceptions
public class VoteManager {

    @Inject
    private Logger logger;

    @Inject
    private Config config;

    @Inject
    private ProphecyDao prophecyDao;

    @Inject
    private VoteDao voteDao;

    public Optional<ClientError> voteFor(final long prophecyId, final String userName) {
        return doVoting(prophecyId, userName, VoteType.FOR);
    }

    public Optional<ClientError> voteAgainst(final long prophecyId, final String userName) {
        return doVoting(prophecyId, userName, VoteType.AGAINST);
    }

    public Optional<ClientError> abstain(final long prophecyId, final String userName) {
        final Consumer<ProphecyEntity> doNothing = p -> {
        };
        return tryProcessExistingVote(prophecyId, userName, voteDao::remove, doNothing);
    }

    private Optional<ClientError> doVoting(final long prophecyId, final String userName, final VoteType voteType) {
        return tryProcessExistingVote(prophecyId, userName, existingVote -> existingVote.setVoteType(voteType), p -> {
            final VoteEntity newVote = new VoteEntity()
                    .setCreatedBy(userName)
                    .setCreatedAt(Instant.now())
                    .setVoteType(voteType)
                    .setProphecy(p);
            voteDao.create(newVote);
        });
    }

    private Optional<ClientError> tryProcessExistingVote(
            final long prophecyId,
            final String userName,
            final Consumer<VoteEntity> onVoteExists,
            final Consumer<ProphecyEntity> onVoteIsAbsent
    ) {
        final VotingPossibilityCheckResult vpcr = checkVoteChangeIsPossible(prophecyId, userName);
        if (vpcr.getClientError().isPresent()) {
            return vpcr.getClientError();
        }
        final ProphecyEntity prophecy = vpcr.getProphecy();
        final Optional<VoteEntity> existingVote = findExistingVote(prophecy, userName);
        if (existingVote.isPresent()) {
            onVoteExists.accept(existingVote.get());
        } else {
            onVoteIsAbsent.accept(prophecy);
        }
        return Optional.empty();
    }

    private Optional<VoteEntity> findExistingVote(final ProphecyEntity prophecy, final String userName) {
        return prophecy.getVotes().stream().filter(v -> v.getCreatedBy().equals(userName)).findFirst();
    }

    private VotingPossibilityCheckResult checkVoteChangeIsPossible(
            final long prophecyId,
            final String userName
    ) {
        final Optional<ProphecyEntity> p = prophecyDao.find(prophecyId);
        if (!p.isPresent()) {
            return new VotingPossibilityCheckResult(null, new ClientError().setMessage("the prophecy does not exist"));
        }
        final ProphecyEntity prophecy = p.get();
        if (prophecy.getCreatedBy().equals(userName)) {
            return new VotingPossibilityCheckResult(prophecy, new ClientError().setMessage("the creator of a prophecy cannot vote for nor against it"));
        }
        final Instant now = Instant.now();
        if (now.compareTo(prophecy.getExpectedAt()) >= 0) {
            return new VotingPossibilityCheckResult(prophecy, new ClientError().setMessage("the prophesy lies in the past"));
        }
        final Instant start = prophecy.getCreatedAt();
        final Instant finish = prophecy.getExpectedAt();
        final Duration duration = Duration.between(start, finish);
        final Duration votingDuration = duration
                .multipliedBy(config.getRelativeVotingDurationPercentage())
                .dividedBy(100);
        final Instant endOfVoting = start.plus(votingDuration);
        logger.debug("now = {}", now);
        logger.debug("start = {}", start);
        logger.debug("finish = {}", finish);
        logger.debug("duration = {}", duration);
        logger.debug("relativeVotingDurationPercentage = {}", config.getRelativeVotingDurationPercentage());
        logger.debug("votingDuration = {}", votingDuration);
        logger.debug("endOfVoting = {}", endOfVoting);
        if (now.compareTo(endOfVoting) >= 0) {
            return new VotingPossibilityCheckResult(prophecy, new ClientError().setMessage("voting for the prophecy period is ended"));
        }
        return new VotingPossibilityCheckResult(prophecy, null);
    }

    private static class VotingPossibilityCheckResult {

        private final ProphecyEntity prophecy;
        private final Optional<ClientError> clientError;

        private VotingPossibilityCheckResult(final ProphecyEntity prophecy, final ClientError clientError) {
            this.prophecy = prophecy;
            this.clientError = Optional.ofNullable(clientError);
        }

        public ProphecyEntity getProphecy() {
            return prophecy;
        }

        public Optional<ClientError> getClientError() {
            return clientError;
        }
    }
}