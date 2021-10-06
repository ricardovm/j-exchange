package dev.ricardovm.jexchange.domain

import dev.ricardovm.jexchange.defaultRates
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import io.smallrye.mutiny.Uni
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.CsvFileSource
import java.math.BigDecimal
import java.math.RoundingMode

@ExtendWith(MockKExtension::class)
class ExchangeServiceTest {

    lateinit var instance: ExchangeService

    @MockK
    lateinit var transactionRepository: TransactionRepository

    @MockK
    lateinit var exchangeInformationService: ExchangeInformationService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        this.instance = ExchangeService(
            transactionRepository = this.transactionRepository,
            exchangeInformationService = exchangeInformationService
        )
    }

    @ParameterizedTest(name = "Scenario: {0} | {1}{2} == {4}{3}")
    @CsvFileSource(resources = ["/scenarios/conversions.csv"], numLinesToSkip = 1)
    fun `currency conversion`(params: ArgumentsAccessor) {
        val entitySlot = slot<Transaction>()
        every { transactionRepository.persist(capture(entitySlot)) } answers {
            val entity = entitySlot.captured
            entity.id = 1
            Uni.createFrom().item(entity)
        }

        every { exchangeInformationService.getRates() } returns Uni.createFrom().item(defaultRates)

        val expectedResult = BigDecimal(params.getString(4)).setScale(3, RoundingMode.HALF_EVEN)
        val expectedRate = BigDecimal(params.getString(5)).setScale(3, RoundingMode.HALF_EVEN)
        val expectedUser = "user"

        val transaction = instance.convert(
            amount = BigDecimal(params.getString(1)),
            baseCurrency = Currency.valueOf(params.getString(2)),
            targetCurrency = Currency.valueOf(params.getString(3)),
            userId = "user"
        ).await().indefinitely()

        val result = transaction.result.setScale(3, RoundingMode.HALF_EVEN)
        val rate = transaction.exchangeRate.setScale(3, RoundingMode.HALF_EVEN)
        val userId = transaction.userId

        assertEquals(expectedResult, result)
        assertEquals(expectedRate, rate)
        assertEquals(expectedUser, userId)
    }

    @Test
    fun `find transactions by user`() {
        every { transactionRepository.findByUserId(any()) } returns Uni.createFrom().item(listOf())

        val expectedUser = "user"

        transactionRepository.findByUserId("user")

        verify { transactionRepository.findByUserId(expectedUser) }
    }
}