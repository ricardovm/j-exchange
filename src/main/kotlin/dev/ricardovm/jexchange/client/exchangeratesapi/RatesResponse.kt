package dev.ricardovm.jexchange.client.exchangeratesapi

import dev.ricardovm.jexchange.infra.DTO
import java.math.BigDecimal
import java.time.LocalDate

@DTO
class RatesResponse(
    val success: Boolean,
    val base: String,
    val date: LocalDate,
    val rates: Map<String, BigDecimal>
)