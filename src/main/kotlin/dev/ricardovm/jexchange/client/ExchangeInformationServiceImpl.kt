package dev.ricardovm.jexchange.client

import dev.ricardovm.jexchange.client.exchangeratesapi.ExchangeRatesAPIClient
import dev.ricardovm.jexchange.domain.ExchangeInformationService
import dev.ricardovm.jexchange.domain.JExchangeException
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ExchangeInformationServiceImpl @Inject constructor(
    @RestClient val exchangeRatesAPIClient: ExchangeRatesAPIClient,
    @ConfigProperty(name = "app.exchangesratesapi.access-key") val accessKey: String,
) : ExchangeInformationService {

    override fun getRates(): Uni<Map<String, BigDecimal>> {
        Log.info("Requesting rates from exchange information service")

        return exchangeRatesAPIClient.getRates(base = "EUR", accessKey = accessKey)
            .map { it.rates }
            .onFailure().transform { e ->
                JExchangeException(
                    userMessage = "Failed to query currency exchange rates service. Please, try again later.",
                    cause = e
                )
            }
    }
}