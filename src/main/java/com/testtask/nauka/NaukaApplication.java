package com.testtask.nauka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class NaukaApplication {
	public static void main(String[] args) {
		SpringApplication.run(NaukaApplication.class, args);
	}
}
