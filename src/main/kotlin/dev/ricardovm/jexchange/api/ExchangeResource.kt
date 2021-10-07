package dev.ricardovm.jexchange.api

import dev.ricardovm.jexchange.domain.Currency
import dev.ricardovm.jexchange.domain.ExchangeService
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("exchange")
@Tag(name = "Exchange Resource", description = "Exchange and conversion related operations")
class ExchangeResource(
    val exchangeService: ExchangeService
) {

    @GET
    @Path("convert")
    @Operation(summary = "Convert an amount from a currency to other (available: BRL, EUR, JPY and USD)")
    @APIResponse(
        responseCode = "200",
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = Schema(
                implementation = TransactionDTO::class,
                type = SchemaType.OBJECT
            )
        )]
    )
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