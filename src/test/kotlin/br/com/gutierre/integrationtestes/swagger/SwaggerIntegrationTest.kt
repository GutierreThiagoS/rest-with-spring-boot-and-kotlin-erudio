package br.com.gutierre.integrationtestes.swagger

import br.com.gutierre.integrationtestes.TestConfigs
import br.com.gutierre.integrationtestes.testecontainers.AbstractIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest: AbstractIntegrationTest() {

	@Test
	fun showDisplaySwaggerUiPage() {
		val content = RestAssured.given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfigs.SERVER_PORT)
			.`when`()
			.get()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()

		Assertions.assertTrue(content.contains("Swagger UI"))
	}

}
