package com.axa.test_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.axa.test_service", "com.axa.core_lib"})
public class TestServiceApplication {



	public static void main(String[] args) {
		SpringApplication.run(TestServiceApplication.class, args);
	}

}
