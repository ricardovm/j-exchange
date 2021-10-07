package dev.ricardovm.jexchange.api

import dev.ricardovm.jexchange.domain.Currency
import dev.ricardovm.jexchange.domain.ExchangeService
import io.smallrye.mutiny.Uni
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam

@Path("exchange")
class ExchangeResource(
    val exchangeService: ExchangeService
) {

    @GET
    @Path("convert")
    fun convert(
        @Size(min = 3, max = 3, message = "Currency code should have three letters")
        @QueryParam("baseCurrency") baseCurrency: String,
        @Size(min = 3, max = 3, message = "Currency code should have three letters")
        @QueryParam("targetCurrency") targetCurrency: String,
        @QueryParam("amount") amount: BigDecimal,
        @NotBlank
        @QueryParam("userId") userId: String
    ): Uni<TransactionDTO> {
        return exchangeService.convert(
            baseCurrency = Currency.valueOf(baseCurrency),
            targetCurrency = Currency.valueOf(targetCurrency),
            amount = amount,
            userId = userId
        ).map { it.toDTO() }
    }
}