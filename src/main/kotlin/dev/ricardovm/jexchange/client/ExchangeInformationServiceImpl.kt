package dev.ricardovm.jexchange.client

import dev.ricardovm.jexchange.client.exchangeratesapi.ExchangeRatesAPIClient
import dev.ricardovm.jexchange.domain.ExchangeInformationService
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.logging.Logger
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ExchangeInformationServiceImpl(
    @RestClient val exchangeRatesAPIClient: ExchangeRatesAPIClient,
    @ConfigProperty(name = "app.exchangesratesapi.access-key") val accessKey: String,
) : ExchangeInformationService {

    companion object {
        private val LOG: Logger = Logger.getLogger(ExchangeInformationService::class.java)
    }

    override fun getRates(): Uni<Map<String, BigDecimal>> {
        LOG.info("Requesting rates from exchange information service")

        return exchangeRatesAPIClient.getRates(base = "EUR", accessKey = accessKey)
            .map { it.rates }
    }
}