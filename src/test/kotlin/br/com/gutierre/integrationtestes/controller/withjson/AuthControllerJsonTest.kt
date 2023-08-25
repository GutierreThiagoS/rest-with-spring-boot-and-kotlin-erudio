package br.com.gutierre.integrationtestes.controller.withjson

import br.com.gutierre.data.vo.v1.AccountCredentialVO
import br.com.gutierre.data.vo.v1.TokenVO
import br.com.gutierre.integrationtestes.TestConfigs
import br.com.gutierre.integrationtestes.testecontainers.AbstractIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerJsonTest: AbstractIntegrationTest() {

    private lateinit var tokenVO: TokenVO

    @BeforeAll
    fun setupTests() {
        tokenVO = TokenVO()
    }

    @Test
    @Order(0)
    fun testLogin() {

        val user = AccountCredentialVO(
            userName = "gutierre",
            password = "admin123"
        )

        tokenVO = RestAssured.given().
        basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(user)
        .`when`()
            .post()
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                    .`as`(TokenVO::class.java)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }


    @Test
    @Order(1)
    fun testRefresh() {

        tokenVO = RestAssured.given().
        basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("username", tokenVO.userName)
                .header(
                    TestConfigs.HEADER_PARAM_AUTHORIZATION,
                    "Bearer ${tokenVO.refreshToken}"
                )
            .`when`()
                .put("{username}")
                    .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .`as`(TokenVO::class.java)

        assertNotNull(tokenVO.accessToken)
        assertNotNull(tokenVO.refreshToken)
    }

}