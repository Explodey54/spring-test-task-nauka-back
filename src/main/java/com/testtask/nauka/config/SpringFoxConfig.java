package com.testtask.nauka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Calendar;

import static springfox.documentation.spring.web.paths.Paths.removeAdjacentForwardSlashes;

@Configuration
@EnableSwagger2
//@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {
    private final String basePath = "/api/v1";

    @Bean
    public Docket api(ServletContext servletContext) {
        return new Docket(DocumentationType.SWAGGER_2)
                .directModelSubstitute(Calendar.class, String.class)
                .pathProvider(setUpPathProvider(servletContext))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.testtask.nauka.api"))
                .paths(PathSelectors.any())
                .build();
    }

    private PathProvider setUpPathProvider(ServletContext servletContext) {
        return new RelativePathProvider(servletContext) {
            @Override
            public String getApplicationBasePath() {
                return basePath;
            }

            @Override
            public String getOperationPath(String operationPath) {
                if (operationPath.startsWith(basePath)) {
                    operationPath = operationPath.substring(basePath.length());
                }
                return removeAdjacentForwardSlashes(UriComponentsBuilder.newInstance().replacePath(operationPath)
                        .build().toString());
            }
        };
    }
}
