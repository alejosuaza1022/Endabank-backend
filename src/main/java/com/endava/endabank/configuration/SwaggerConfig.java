package com.endava.endabank.configuration;

import com.endava.endabank.constants.Swagger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler(Swagger.SWAGGER_UI)
                .addResourceLocations(Swagger.SWAGGER_LOCATION);

        registry
                .addResourceHandler(Swagger.WEBJARS)
                .addResourceLocations(Swagger.WEBJARS_LOCATION);
    }


    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
            .apis(RequestHandlerSelectors.basePackage(Swagger.BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                ;
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                Swagger.API_INFO_TITTLE,
                Swagger.API_INFO_DESCRIPTION,
                Swagger.API_INFO_VERSION,
                Swagger.API_INFO_TERM_OF_SERVICE_URL,
                new Contact(Swagger.API_INFO_CONTACT_NAME, Swagger.API_INFO_CONTACT_URL, Swagger.API_INFO_CONTACT_EMAIL),
                Swagger.API_INFO_LICENSE,
                Swagger.API_INFO_LICENSE_URL,
                Collections.emptyList()
        );
    }
}
