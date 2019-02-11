package de.consol.labs.microprofilearticle.stats.integration.stockapi;

import java.math.BigDecimal;

public class ChartEntry {

    private String date;
    private String minute;
    private BigDecimal close;

    public String getDate() {
        return date;
    }

    public ChartEntry setDate(final String date) {
        this.date = date;
        return this;
    }

    public String getMinute() {
        return minute;
    }

    public ChartEntry setMinute(final String minute) {
        this.minute = minute;
        return this;
    }

    public BigDecimal getClose() {
        return close;
    }

    public ChartEntry setClose(final BigDecimal close) {
        this.close = close;
        return this;
    }

    @Override
    public String toString() {
        return "ChartEntry{" +
                "date='" + date + '\'' +
                ", minute='" + minute + '\'' +
                ", close=" + close +
                '}';
    }
}
