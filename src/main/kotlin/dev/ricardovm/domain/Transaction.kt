package dev.ricardovm.domain

import java.math.BigDecimal
import java.time.Instant
import javax.persistence.*

@Entity
class Transaction(
    @Id
    var id: Long?,
    @Column(name="user_id")
    var userId: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "base_currency")
    var baseCurrency: Currency,
    var value: BigDecimal,
    @Column(name = "target_value")
    var targetValue: BigDecimal,
    @Column(name = "target_currency")
    var targetCurrency: Currency,
    @Column(name = "conversion_rate")
    var conversionRate: BigDecimal,
    var timestamp: Instant
)
