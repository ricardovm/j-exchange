package dev.ricardovm.jexchange.api

import dev.ricardovm.jexchange.domain.Currency
import dev.ricardovm.jexchange.domain.Transaction
import dev.ricardovm.jexchange.infra.DTO
import java.math.BigDecimal

@DTO
class TransactionDTO(
    var id: Long,
    var userId: String,
    var baseCurrency: Currency,
    var amount: BigDecimal,
    var result: BigDecimal,
    var targetCurrency: Currency,
    var exchangeRate: BigDecimal,
    var timestamp: Long
) {

    companion object {
        fun from(transaction: Transaction) =
            TransactionDTO(
                id = transaction.id ?: 0,
                userId = transaction.userId,
                baseCurrency = transaction.baseCurrency,
                amount = transaction.amount,
                result = transaction.result,
                targetCurrency = transaction.targetCurrency,
                exchangeRate = transaction.exchangeRate,
                timestamp = transaction.timestamp.toEpochMilli()
            )
    }
}

fun Transaction.toDTO(): TransactionDTO = TransactionDTO.from(this)