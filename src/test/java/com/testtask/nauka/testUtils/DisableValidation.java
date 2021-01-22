package com.testtask.nauka.testUtils;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@TestPropertySource(properties = "spring.jpa.properties.javax.persistence.validation.mode=none")
@MockBean(name = "mvcValidator", value = LocalValidatorFactoryBean.class)
public @interface DisableValidation {
}
