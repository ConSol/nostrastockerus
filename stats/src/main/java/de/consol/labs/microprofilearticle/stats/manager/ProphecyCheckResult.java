package de.consol.labs.microprofilearticle.stats.manager;

import de.consol.labs.microprofilearticle.stats.model.ProphecyRevelation;

import javax.validation.constraints.NotNull;

public class ProphecyCheckResult {

    @NotNull
    private final Outcome outcome;

    private final ProphecyRevelation prophecyRevelation;

    public ProphecyCheckResult(final Outcome outcome) {
        this.outcome = outcome;
        this.prophecyRevelation = null;
    }

    public ProphecyCheckResult(final Outcome outcome, final ProphecyRevelation prophecyRevelation) {
        this.outcome = outcome;
        this.prophecyRevelation = prophecyRevelation;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public ProphecyRevelation getProphecyRevelation() {
        return prophecyRevelation;
    }

    public enum Outcome {
        PROPHECY_DOES_NOT_EXIST,
        TOO_EARLY,
        CHECK_DONE
    }
}
