package dev.ricardovm.jexchange.domain

import java.math.BigDecimal
import java.time.Instant
import javax.persistence.*

@Entity
@SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq")
class Transaction(
    @Id
    @GeneratedValue(generator = "transaction_seq")
    var id: Long? = null,
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
    var timestamp: Instant = Instant.now()
)
