package dev.ricardovm.jexchange.api

import dev.ricardovm.jexchange.domain.ExchangeService
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import javax.inject.Inject
import javax.validation.constraints.NotBlank
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.MediaType

@Path("transactions")
@Tag(name = "Transactions Resource", description = "Endpoit to query transactions")
class TransactionsResource @Inject constructor(
    val exchangeService: ExchangeService
) {

    @GET
    @Path("{userId}")
    @Operation(summary = "Get all transactions made by a specific user in reverse order")
    @APIResponse(
        responseCode = "200",
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = org.eclipse.microprofile.openapi.annotations.media.Schema(
                implementation = TransactionDTO::class,
                type = SchemaType.ARRAY
            )
        )]
    )
    fun getTransactions(
        @NotBlank
        @PathParam("userId") userId: String
    ): Uni<List<TransactionDTO>> {
        return exchangeService.findTransactionsByUserId(userId = userId)
            .map { list -> list.map { it.toDTO() } }
    }
}