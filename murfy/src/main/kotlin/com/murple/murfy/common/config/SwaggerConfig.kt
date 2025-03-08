package com.murple.murfy.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    @Bean
    fun swaggerApi(): OpenAPI = OpenAPI()
        .components(Components())
        .info(
            Info()
                .title("User API")
                .description("사용자 정보 CRUD API 입니다.")
                .version("1.0.0")
        )
}
