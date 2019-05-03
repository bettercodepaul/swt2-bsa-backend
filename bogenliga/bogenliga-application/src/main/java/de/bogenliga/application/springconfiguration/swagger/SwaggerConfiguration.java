package de.bogenliga.application.springconfiguration.swagger;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Configure the Swagger API documentation
 *
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 * @see <a href="https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api">Spring Swagger Tutorial</a>
 * @see <a href="http://localhost:9000/swagger-ui.html#">Open Swagger UI</a>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.bogenliga.application.services"))
                .paths(regex("/v1/.*")) // show v1 services
                .build()
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Bogenliga Application",
                "GitHub project: exxcellent/swt2-bsa-backend",
                null,
                null,
                new Contact("Andre Lehnert (maintainer)",
                        "https://github.com/exxcellent/swt2-bsa-backend",
                        "andre.lehnert@exxcellent.de"),
                "License of API", "https://github.com/exxcellent/swt2-bsa-backend/blob/master/LICENSE",
                Collections.emptyList());
    }
}
