package dev.ricardovm.jexchange.domain

import dev.ricardovm.jexchange.ExchangeRatesAPIWiremock
import io.quarkus.test.TestTransaction
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(ExchangeRatesAPIWiremock::class)
class ExchangeServiceIT {

    @Inject
    lateinit var repository: TransactionRepository

    @Inject
    lateinit var instance: ExchangeService

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
}