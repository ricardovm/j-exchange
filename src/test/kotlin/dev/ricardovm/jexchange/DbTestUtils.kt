package dev.ricardovm.jexchange

import io.vertx.mutiny.pgclient.PgPool
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class DbTestUtils {

    @Inject
    lateinit var client: PgPool

    fun truncateTable(name: String) {
        client.query("TRUNCATE $name;").executeAndAwait()
    }
}