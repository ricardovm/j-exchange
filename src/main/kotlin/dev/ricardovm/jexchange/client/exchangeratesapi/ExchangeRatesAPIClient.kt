package dev.ricardovm.jexchange.client.exchangeratesapi

import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam


@Path("")
@RegisterRestClient(configKey = "exchange-rates-api")
interface ExchangeRatesAPIClient {

    @GET
    @Path("latest")
    fun getRates(
        @QueryParam("base") base: String,
        @QueryParam("access_key") accessKey: String
    ): Uni<RatesResponse>
}