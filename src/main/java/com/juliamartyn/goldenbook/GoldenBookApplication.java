package com.juliamartyn.goldenbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GoldenBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldenBookApplication.class, args);
	}
}
