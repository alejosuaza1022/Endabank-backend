package com.endava.endabank.configuration;


import com.endava.endabank.constants.SwaggerConstants;
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
                .addResourceHandler(SwaggerConstants.SWAGGER_UI)
                .addResourceLocations(SwaggerConstants.SWAGGER_LOCATION);

        registry
                .addResourceHandler(SwaggerConstants.WEBJARS)
                .addResourceLocations(SwaggerConstants.WEBJARS_LOCATION);
    }


    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
            .apis(RequestHandlerSelectors.basePackage(SwaggerConstants.BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                ;
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                SwaggerConstants.API_INFO_TITTLE,
                SwaggerConstants.API_INFO_DESCRIPTION,
                SwaggerConstants.API_INFO_VERSION,
                SwaggerConstants.API_INFO_TERM_OF_SERVICE_URL,
                new Contact(SwaggerConstants.API_INFO_CONTACT_NAME, SwaggerConstants.API_INFO_CONTACT_URL, SwaggerConstants.API_INFO_CONTACT_EMAIL),
                SwaggerConstants.API_INFO_LICENSE,
                SwaggerConstants.API_INFO_LICENSE_URL,
                Collections.emptyList()
        );
    }
}
