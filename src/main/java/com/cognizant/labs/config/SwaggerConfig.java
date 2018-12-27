package com.cognizant.labs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2WebMvc
@Import({SpringDataRestConfiguration.class})
public class SwaggerConfig {

    @Bean
    public Docket all() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("all")
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("api")
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(includePath("persons"))
            .build();
    }

    private Predicate<String> includePath(final String path) {
        return input -> input.contains(path);
    }

}
