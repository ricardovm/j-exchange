package dev.ricardovm.jexchange.domain

import dev.ricardovm.jexchange.DbTestUtils
import dev.ricardovm.jexchange.ExchangeRatesAPIWiremock
import io.quarkus.test.TestTransaction
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(ExchangeRatesAPIWiremock::class)
class ExchangeServiceIT {

    @Inject
    lateinit var dbTestUtils: DbTestUtils

    @Inject
    lateinit var repository: TransactionRepository

    @Inject
    lateinit var instance: ExchangeService

    @BeforeEach
    fun setUp() {
        dbTestUtils.truncateTable("transaction")
    }

    @Test
    @TestTransaction
    fun `transaction should be persisted into the database`() {
        val transaction = instance.convert(BigDecimal.ONE, Currency.USD, Currency.BRL, "user")
            .await()
            .indefinitely()

        val persisted = repository.findById(transaction.id)
            .await()
            .indefinitely()

        assertEquals(transaction, persisted)
    }

    @Test
    @TestTransaction
    fun `findByUser should return only that user transactios`() {
        val userTransactions = listOf(
            Transaction(
                userId = "user",
                amount = BigDecimal.ONE,
                baseCurrency = Currency.USD,
                targetCurrency = Currency.USD,
                result = BigDecimal.ONE,
                exchangeRate = BigDecimal.ONE
            ),
            Transaction(
                userId = "user",
                amount = BigDecimal.ONE,
                baseCurrency = Currency.EUR,
                targetCurrency = Currency.EUR,
                result = BigDecimal.ONE,
                exchangeRate = BigDecimal.ONE
            )
        )

        val otherUserTransactions = listOf(
            Transaction(
                userId = "other_user",
                amount = BigDecimal.ONE,
                baseCurrency = Currency.BRL,
                targetCurrency = Currency.BRL,
                result = BigDecimal.ONE,
                exchangeRate = BigDecimal.ONE
            ),
            Transaction(
                userId = "other_user",
                amount = BigDecimal.ONE,
                baseCurrency = Currency.JPY,
                targetCurrency = Currency.JPY,
                result = BigDecimal.ONE,
                exchangeRate = BigDecimal.ONE
            )
        )

        val allUsersTransactions = listOf(userTransactions, otherUserTransactions).flatten()

        repository.persist(allUsersTransactions)
            .chain { items -> repository.flush() }
            .await()
            .indefinitely()

        val persisted = instance.findTransactionsByUserId("user")
            .await()
            .indefinitely()

        assertEquals(2, persisted.size)
        assertEquals(Currency.EUR, persisted[0].baseCurrency)
        assertEquals(Currency.USD, persisted[1].baseCurrency)
    }

}