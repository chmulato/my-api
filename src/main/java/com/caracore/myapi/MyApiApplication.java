package com.caracore.myapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyApiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfig() {
		return new WebMvcConfigurer() {

			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedOrigins("http://www.caracore.com.br/app")
					.allowedMethods("GET", "PUT", "OPTIONS", "POST", "DELETE", "PATCH")
					.maxAge(900)
					.allowedHeaders("Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization");
			}
			
		};
	}

}
