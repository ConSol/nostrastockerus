package de.consol.labs.microprofilearticle.stats.integration.stockapi;

import de.consol.labs.microprofilearticle.common.logging.LogInputsOutputsAndExceptions;
import de.consol.labs.microprofilearticle.stats.Config;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestScoped
@LogInputsOutputsAndExceptions
public class StockApi {

    @Inject
    private Config config;

    @Bulkhead
    public List<ChartEntry> getStockChart(final String stockName, final LocalDate date) {
        final ChartEntry[] chart = getClient().getChart(stockName, DateTimeFormatter.BASIC_ISO_DATE.format(date));
        return new ArrayList<>(Arrays.asList(chart));
    }

    private StockClient getClient() {
        return RestClientBuilder.newBuilder()
                .baseUrl(config.getStockMarketApiBase())
                .build(StockClient.class);
    }
}
