package dev.ricardovm.jexchange.domain

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
    var amount: BigDecimal,
    var result: BigDecimal,
    @Column(name = "target_currency")
    var targetCurrency: Currency,
    @Column(name = "exchange_rate")
    var exchangeRate: BigDecimal,
    var timestamp: Instant
)
