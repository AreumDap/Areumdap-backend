package com.umc9th.areumdap.common.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Areumdap Backend API")
                        .description("UMC 9TH Areumdap 프로젝트 백엔드 API 문서")
                        .version("1.0.0")
                        .contact(
                                new Contact()
                                        .name("Areumdap Dev Team")
                                        .email("areumdap2026@gmail.com")
                        )
                        .license(new License()
                                .name("Apache License 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));

    }
}
