package dev.ricardovm.jexchange

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

class ExchangeRatesAPIWiremock : QuarkusTestResourceLifecycleManager {

    private var wireMockServer: WireMockServer? = null

    override fun start(): Map<String, String> {
        val wireMockServer = WireMockServer()
        this.wireMockServer = wireMockServer

        wireMockServer.start()

        WireMock.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/latest"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            "{\n" +
                                    "  \"success\": true,\n" +
                                    "  \"timestamp\": 1633553720,\n" +
                                    "  \"base\": \"EUR\",\n" +
                                    "  \"date\": \"2021-10-06\",\n" +
                                    "  \"rates\": {\n" +
                                    "    \"USD\": 1.15485,\n" +
                                    "    \"EUR\": 1,\n" +
                                    "    \"BRL\": 6.382049,\n" +
                                    "    \"JPY\": 128.591943\n" +
                                    "  }\n" +
                                    "}"
                        )
                )
        )

        return mapOf(
            "exchange-rates-api/mp-rest/url" to wireMockServer.baseUrl()
        )
    }

    override fun stop() {
        wireMockServer?.stop()
    }
}