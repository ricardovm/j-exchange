package dev.ricardovm.jexchange.api

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class ExchangeResourceIT {

    @Test
    fun `conversion from USD to BRL`() {
        given()
            .`when`().get("/exchange/convert?baseCurrency=USD&targetCurrency=BRL&amount=1.0&userId=user")
            .then()
            .statusCode(200)
            .body("result", `is`(5.526299f))
    }
}
