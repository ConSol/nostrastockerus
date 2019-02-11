package de.consol.labs.microprofilearticle.stats.integration.stockapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("stock")
public interface StockClient {
    @GET
    @Path("{stockName}/chart/date/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    ChartEntry[] getChart(
            @PathParam("stockName") final String stockName,
            @PathParam("date") final String date
    );
}
