package dev.ricardovm.jexchange.infra.db

import io.quarkus.runtime.StartupEvent
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.flywaydb.core.Flyway
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
class FlywayMigrations(
    @ConfigProperty(name = "quarkus.datasource.reactive.url")
    val datasourceUrl: String,
    @ConfigProperty(name = "quarkus.datasource.username")
    val datasourceUsername: String,
    @ConfigProperty(name = "quarkus.datasource.password")
    val datasourcePassword: String
) {

    fun runFlywayMigration(@Observes event: StartupEvent) {
        val jdbcUrl = "jdbc:" + datasourceUrl.replace("vertx-reactive:", "")
        val flyway = Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(jdbcUrl, datasourceUsername, datasourcePassword)
            .load()

        flyway.migrate()
    }
}