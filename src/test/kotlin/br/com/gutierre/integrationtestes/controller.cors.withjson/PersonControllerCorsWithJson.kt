package br.com.gutierre.integrationtestes.controller.cors.withjson

import br.com.gutierre.integrationtestes.TestConfigs
import br.com.gutierre.integrationtestes.testecontainers.AbstractIntegrationTest
import br.com.gutierre.integrationtestes.vo.PersonVO
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(Lifecycle.PER_CLASS)
class PersonControllerCorsWithJson: AbstractIntegrationTest() {

	private lateinit var specification: RequestSpecification
	private lateinit var objectMapper: ObjectMapper
	private lateinit var person: PersonVO

	@BeforeAll
	fun setupTests() {
		objectMapper = ObjectMapper()
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		person = PersonVO()

	}

	@Test
	@Order(1)
	fun testCreate() {

		mockPerson()

		specification = RequestSpecBuilder()
				.addHeader(
					TestConfigs.HEADER_PARAM_ORIGIN,
					TestConfigs.ORIGIN_GUTIERRE
				)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(RequestLoggingFilter(LogDetail.ALL))
				.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = RestAssured.given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(person)
			.`when`()
			.post()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()

		val createdPerson = objectMapper.readValue(
			content,
			PersonVO::class.java
		)
		Assertions.assertNotNull(createdPerson.id)
		Assertions.assertNotNull(createdPerson.firstName)
		Assertions.assertNotNull(createdPerson.lastName)
		Assertions.assertNotNull(createdPerson.address)
		Assertions.assertNotNull(createdPerson.gender)

		Assertions.assertTrue(createdPerson.id > 0)

		Assertions.assertEquals("Gutierre", createdPerson.firstName)
		Assertions.assertEquals("Guimaraes", createdPerson.lastName)
		Assertions.assertEquals("Brasilia, DF, Brasil", createdPerson.address)
		Assertions.assertEquals("Male", createdPerson.gender)

		Assertions.assertTrue(content.contains("Swagger UI"))
	}

	private fun mockPerson() {
		person.firstName = "Gutierre"
		person.lastName = "Guimaraes"
		person.address = "Brasilia, DF, Brasil"
		person.gender = "Male"
	}

}
