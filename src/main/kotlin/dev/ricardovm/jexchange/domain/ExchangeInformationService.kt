package dev.ricardovm.jexchange.domain

import io.smallrye.mutiny.Uni
import java.math.BigDecimal

interface ExchangeInformationService {

    fun getRates(): Uni<Map<String, BigDecimal>>
}