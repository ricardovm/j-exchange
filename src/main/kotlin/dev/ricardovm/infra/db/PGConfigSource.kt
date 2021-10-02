package dev.ricardovm.infra.db

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.eclipse.microprofile.config.spi.ConfigSource

class PGConfigSource : ConfigSource {

    companion object {
        @JvmStatic
        var db: EmbeddedPostgres? = null

        var props: MutableMap<String, String> = HashMap()

        fun load() {
            if (db == null) {
                db = EmbeddedPostgres.start().also {
                    val dbUser = "postgres"
                    val dbPass = "postgres"
                    val url = it.getJdbcUrl(dbUser, dbPass)

                    props["quarkus.datasource.username"] = dbUser
                    props["quarkus.datasource.password"] = dbPass
                    props["quarkus.datasource.jdbc.url"] = url
                }
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