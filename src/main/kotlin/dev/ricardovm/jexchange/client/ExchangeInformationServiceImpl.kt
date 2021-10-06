package dev.ricardovm.jexchange.client

import dev.ricardovm.jexchange.client.exchangeratesapi.ExchangeRatesAPIClient
import dev.ricardovm.jexchange.domain.ExchangeInformationService
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ExchangeInformationServiceImpl(
    @RestClient val exchangeRatesAPIClient: ExchangeRatesAPIClient,
    @ConfigProperty(name = "app.exchangesratesapi.access-key") val accessKey: String,
) : ExchangeInformationService {

    override fun getRates(): Uni<Map<String, BigDecimal>> =
        exchangeRatesAPIClient.getRates(accessKey = accessKey)
            .map { it.rates }
}