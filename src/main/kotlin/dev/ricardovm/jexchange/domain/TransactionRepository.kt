package dev.ricardovm.jexchange.domain

import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class TransactionRepository : PanacheRepository<Transaction> {

    fun findByUserId(userId: String): Uni<List<Transaction>> =
        list("userId", Sort.by("id", Sort.Direction.Descending), userId)
}