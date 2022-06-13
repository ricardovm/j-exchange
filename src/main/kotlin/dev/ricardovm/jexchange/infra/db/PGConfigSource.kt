package dev.ricardovm.jexchange.infra.db

import org.eclipse.microprofile.config.spi.ConfigSource
import org.testcontainers.containers.PostgreSQLContainer

class PGConfigSource : ConfigSource {

    companion object {
        @JvmStatic
        val postgreSQLContainer: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:14")
            .apply {
                withDatabaseName("postgres")
                withUsername("postgres")
                withPassword("postgres")
                start()
            }

        var props: MutableMap<String, String> = HashMap()

        fun load() {
            if (props.isEmpty()) {
                val reactiveUrl = "postgresql://localhost:${postgreSQLContainer.firstMappedPort}/postgres"

                props["quarkus.datasource.username"] = postgreSQLContainer.username
                props["quarkus.datasource.password"] = postgreSQLContainer.password
                props["quarkus.datasource.reactive.url"] = reactiveUrl
            }
        }
    }

    init {
        load()
    }

    override fun getName(): String = javaClass.simpleName

    override fun getValue(key: String?): String? = props[key]

    override fun getProperties(): MutableMap<String, String> = props

    override fun getPropertyNames(): MutableSet<String> = props.keys
}