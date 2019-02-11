package de.consol.labs.microprofilearticle.stats.manager;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.prophecy.generatedclient.model.Prophecy;
import de.consol.labs.microprofilearticle.stats.Config;
import de.consol.labs.microprofilearticle.stats.dao.ProphecyRevelationDao;
import de.consol.labs.microprofilearticle.stats.entity.ProphecyRevelationEntity;
import de.consol.labs.microprofilearticle.stats.integration.prophecy.ProphecyApi;
import de.consol.labs.microprofilearticle.stats.integration.stockapi.ChartEntry;
import de.consol.labs.microprofilearticle.stats.integration.stockapi.StockApi;
import de.consol.labs.microprofilearticle.stats.integration.user.UserApi;
import de.consol.labs.microprofilearticle.stats.mapper.ProphecyRevelationMapper;
import de.consol.labs.microprofilearticle.stats.model.UserInfo;
import de.consol.labs.microprofilearticle.stats.model.UserStats;
import de.consol.labs.microprofilearticle.user.generatedclient.model.User;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.consol.labs.microprofilearticle.prophecy.generatedclient.model.Prophecy.ProphecyTypeEnum.BEAR;
import static de.consol.labs.microprofilearticle.prophecy.generatedclient.model.Prophecy.ProphecyTypeEnum.BULL;

@Transactional
@ApplicationScoped
@LogInputsOutputsAndExceptions
public class StatsManager {

    @Inject
    private Logger logger;

    @Inject
    private Config config;

    @Inject
    private UserApi userApi;

    @Inject
    private ProphecyApi prophecyApi;

    @Inject
    private StockApi stockApi;

    @Inject
    private ProphecyRevelationMapper prophecyRevelationMapper;

    @Inject
    private ProphecyRevelationDao prophecyRevelationDao;

    public void checkProphecies() {
        getProphecies().forEach(p -> {
            try {
                checkProphecy(p);
            } catch (final RuntimeException e) {
                logger.error("failed to check prophecy", e);
            }
        });
    }

    public ProphecyCheckResult checkProphecy(final Long prophecyId) {
        final Optional<ProphecyRevelationEntity> existingCheck = prophecyRevelationDao.find(prophecyId);
        if (existingCheck.isPresent()) {
            logger.debug("the prophecy has already been checked: {}", existingCheck.get());
            return new ProphecyCheckResult(
                    ProphecyCheckResult.Outcome.CHECK_DONE,
                    prophecyRevelationMapper.entity2Model(existingCheck.get())
            );
        }
        final Optional<Prophecy> p = prophecyApi.getProphecyInformation(prophecyId);
        if (!p.isPresent()) {
            logger.debug("could not find prophecy with ID {}", prophecyId);
            return new ProphecyCheckResult(ProphecyCheckResult.Outcome.PROPHECY_DOES_NOT_EXIST);
        }
        return checkProphecy(p.get());

    }

    private List<Prophecy> getProphecies() {
        final Instant to = Instant.now();
        final Instant from = to.minus(config.getTimeWindowLengthForPropheciesCheck());
        return prophecyApi.findPropheciesByExpectedAt(from, to);
    }

    private ProphecyCheckResult checkProphecy(final Prophecy prophecy) {
        logger.debug("checking prophecy {}", prophecy);
        if (OffsetDateTime.now().compareTo(prophecy.getExpectedAt()) < 0) {
            logger.debug("prophecy must be checked at {} or later", prophecy.getExpectedAt());
            return new ProphecyCheckResult(ProphecyCheckResult.Outcome.TOO_EARLY);
        }
        final List<ChartEntry> chart = stockApi.getStockChart(
                prophecy.getStockName(),
                prophecy.getExpectedAt()
                        .atZoneSameInstant(config.getStockMarketApiZone())
                        .toLocalDate()
        );
        final BigDecimal realValue = getStockValue(prophecy.getExpectedAt().toInstant(), chart);
        final BigDecimal expectedValue = prophecy.getStockExpectedValue();
        final boolean realValueDoesNotExceedExpectedOne = realValue.compareTo(expectedValue) <= 0;
        final boolean realValueIsAtLeastAsExpectedOneOrGreater = realValue.compareTo(expectedValue) >= 0;
        final boolean isProphecyFulfilled =
                (BEAR.equals(prophecy.getProphecyType()) && realValueDoesNotExceedExpectedOne)
                        || (BULL.equals(prophecy.getProphecyType()) && realValueIsAtLeastAsExpectedOneOrGreater);
        logger.debug(
                "prophecy ID {}, expected at {}, expected value is {}, type is {}, real value is {}, prophecy {} fulfilled",
                prophecy.getId(),
                prophecy.getExpectedAt(),
                prophecy.getStockExpectedValue(),
                prophecy.getProphecyType(),
                realValue,
                isProphecyFulfilled ? "is" : "is not"
        );
        final ProphecyRevelationEntity entity = prophecyRevelationDao.create(
                new ProphecyRevelationEntity()
                        .setProphecyId(prophecy.getId())
                        .setStockRealValue(realValue)
                        .setFulfilled(isProphecyFulfilled)
        );
        logger.debug("checked prophecy: {}", entity);
        return new ProphecyCheckResult(
                ProphecyCheckResult.Outcome.CHECK_DONE,
                prophecyRevelationMapper.entity2Model(entity)
        );
    }

    private BigDecimal getStockValue(final Instant expectedAt, final List<ChartEntry> chart) {
        if (chart == null || chart.isEmpty()) {
            throw new IllegalArgumentException("no chart data");
        }
        Duration minDiff = null;
        BigDecimal value = null;
        for (final ChartEntry entry : chart) {
            final LocalDate date = LocalDate.parse(entry.getDate(), DateTimeFormatter.BASIC_ISO_DATE);
            final LocalTime time = LocalTime.parse(entry.getMinute(), DateTimeFormatter.ISO_LOCAL_TIME);
            final LocalDateTime dateTime = LocalDateTime.of(date, time);
            final Instant timePoint = ZonedDateTime.of(dateTime, config.getStockMarketApiZone()).toInstant();
            final Duration diff = Duration.between(expectedAt, timePoint).abs();
            final BigDecimal v = entry.getClose();
            if (minDiff == null || minDiff.compareTo(diff) < 0) {
                minDiff = diff;
                value = v;
                logger.trace("updated value to {}, new difference is {}, chart entry is {}", value, minDiff, entry);
            }
        }
        if (value == null) {
            throw new IllegalArgumentException("chart data does not contain value");
        }
        return value;
    }

    public UserStats getStatsForUser(final String userName) {
        final User user = userApi.getUser(userName).orElseThrow(() -> new RuntimeException("user " + userName + "does not exist"));
        final UserInfo userInfo = new UserInfo()
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setCreatedAt(user.getCreatedAt().toInstant())
                .setCreatedBy(user.getCreatedBy());
        final List<Prophecy> prophecies = prophecyApi.getPropheciesCreatedByUser(userName);
        if (prophecies.isEmpty()) {
            return new UserStats()
                    .setUserInfo(userInfo);
        }
        final long numberOfPropheciesCreated = prophecies.size();
        final List<ProphecyRevelationEntity> revelations = prophecyRevelationDao.findByIds(
                prophecies.stream().map(Prophecy::getId).collect(Collectors.toList())
        );
        if (revelations.isEmpty()) {
            return new UserStats()
                    .setUserInfo(userInfo)
                    .setNumberOfPropheciesCreated(numberOfPropheciesCreated);
        }
        final long numberOfPropheciesChecked = revelations.size();
        final Map<Boolean, List<ProphecyRevelationEntity>> groupedByFulfilled = revelations
                .stream()
                .collect(Collectors.groupingBy(ProphecyRevelationEntity::getFulfilled));
        final long numberOfPropheciesFulfilled = groupedByFulfilled.getOrDefault(Boolean.TRUE, Collections.emptyList()).size();
        return new UserStats()
                .setUserInfo(userInfo)
                .setNumberOfPropheciesCreated(numberOfPropheciesCreated)
                .setNumberOfPropheciesChecked(numberOfPropheciesChecked)
                .setNumberOfPropheciesFulfilled(numberOfPropheciesFulfilled)
                .setPrecision(
                        BigDecimal
                                .valueOf(numberOfPropheciesFulfilled)
                                .divide(
                                        BigDecimal.valueOf(numberOfPropheciesChecked),
                                        RoundingMode.HALF_DOWN
                                )
                );
    }
}
