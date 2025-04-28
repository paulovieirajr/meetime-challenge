package com.github.paulovieirajr.meetime.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${info.app.name:unknown}")
    private String name;

    @Value("${info.app.description:unknown}")
    private String description;

    @Value("${info.app.version:unknown}")
    private String version;

    @Value("${info.app.scm.url:unknown}")
    private String gitUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(this.name)
                        .description(this.description)
                        .version(version)
                        .contact(new Contact()
                                .name("PAULO CESAR VIEIRA JUNIOR")
                                .email("paulovieiradev@outlook.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("GitHub Repository")
                        .url(gitUrl)
                );
    }
}