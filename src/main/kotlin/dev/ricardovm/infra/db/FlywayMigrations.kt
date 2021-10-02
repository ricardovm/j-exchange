package dev.ricardovm.infra.db

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
        val flyway = Flyway.configure()
            .dataSource("jdbc:$datasourceUrl", datasourceUsername, datasourcePassword)
            .load()

        flyway.migrate()
    }
}