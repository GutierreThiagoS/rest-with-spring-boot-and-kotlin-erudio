package br.com.gutierre.config

import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Restfull API")
                    .version("v1")
                    .description("Some description about your API")
                    .license(
                        License().name("Apache 2.0")
                            .url("https://192.168.206.52:8080")
                    )
            )
    }
}

