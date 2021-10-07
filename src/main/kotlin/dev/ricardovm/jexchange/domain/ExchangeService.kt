package dev.ricardovm.jexchange.domain

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import org.jboss.logging.Logger
import java.math.BigDecimal
import java.math.RoundingMode
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class ExchangeService(
    val transactionRepository: TransactionRepository,
    val exchangeInformationService: ExchangeInformationService
) {

    companion object {
        private val LOG: Logger = Logger.getLogger(ExchangeService::class.java)
    }

    @ReactiveTransactional
    fun convert(
        amount: BigDecimal,
        baseCurrency: Currency,
        targetCurrency: Currency,
        userId: String
    ): Uni<Transaction> {
        LOG.infov("Converting {0} {1} to {2} (userId: {3})", amount, baseCurrency, targetCurrency, userId)

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
        LOG.infov("Listing all transactions of user {0}", userId)

        return transactionRepository.findByUserId(userId)
            .onFailure().transform { e ->
                JExchangeException(
                    userMessage = "Failed to load user's transactions. Please, try again later.",
                    cause = e
                )
            }
    }
}