package dev.ricardovm.jexchange.api

import dev.ricardovm.jexchange.domain.ExchangeService
import io.smallrye.mutiny.Uni
import javax.validation.constraints.NotBlank
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam

@Path("transactions")
class TransactionsResource(
    val exchangeService: ExchangeService
) {

    @GET
    @Path("{userId}")
    fun convert(
        @NotBlank
        @PathParam("userId") userId: String
    ): Uni<List<TransactionDTO>> {
        return exchangeService.findTransactionsByUserId(userId = userId)
            .map { list -> list.map { it.toDTO() } }
    }
}