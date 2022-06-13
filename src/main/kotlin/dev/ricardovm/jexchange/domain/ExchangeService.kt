package dev.ricardovm.jexchange.domain

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.quarkus.logging.Log
import io.smallrye.mutiny.Uni
import java.math.BigDecimal
import java.math.RoundingMode
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class ExchangeService @Inject constructor(
    val transactionRepository: TransactionRepository,
    val exchangeInformationService: ExchangeInformationService
) {

    @ReactiveTransactional
    fun convert(
        amount: BigDecimal,
        baseCurrency: Currency,
        targetCurrency: Currency,
        userId: String
    ): Uni<Transaction> {
        Log.infov("Converting {0} {1} to {2} (userId: {3})", amount, baseCurrency, targetCurrency, userId)

        return exchangeInformationService.getRates()
            .map { rates -> convert(amount, baseCurrency, targetCurrency, rates) }
            .flatMap { result -> persist(amount, baseCurrency, targetCurrency, result, userId) }
    }

    private fun convert(
        amount: BigDecimal,
        baseCurrency: Currency,
        targetCurrency: Currency,
        rates: Map<String, BigDecimal>
    ) =
        amount
            .divide(rates[baseCurrency.name]!!, 6, RoundingMode.HALF_EVEN)
            .multiply(rates[targetCurrency.name]!!)

    private fun persist(
        amount: BigDecimal,
        baseCurrency: Currency,
        targetCurrency: Currency,
        result: BigDecimal,
        userId: String
    ): Uni<Transaction> {
        val transaction = Transaction(
            userId = userId,
            baseCurrency = baseCurrency,
            amount = amount,
            result = result,
            targetCurrency = targetCurrency,
            exchangeRate = result.divide(amount)
        )

        return transactionRepository.persist(transaction)
            .onFailure().transform { e ->
                JExchangeException(
                    userMessage = "Failed to persist this transaction. Please, try again later.",
                    cause = e
                )
            }
    }

    fun findTransactionsByUserId(userId: String): Uni<List<Transaction>> {
        Log.infov("Listing all transactions of user {0}", userId)

        return transactionRepository.findByUserId(userId)
            .onFailure().transform { e ->
                JExchangeException(
                    userMessage = "Failed to load user's transactions. Please, try again later.",
                    cause = e
                )
            }
    }
}