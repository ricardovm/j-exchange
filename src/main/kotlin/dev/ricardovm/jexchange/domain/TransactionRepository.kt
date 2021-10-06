package dev.ricardovm.jexchange.domain

import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TransactionRepository : PanacheRepository<Transaction>