package com.caracore.myapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.caracore.myapi.service.CategoryService;

@SpringBootApplication
public class ChecklistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChecklistApiApplication.class, args);
	}
		
	@Bean
	CommandLineRunner initDatabase(CategoryService categoryService) {
		return args -> {
			categoryService.addNewCategory("Saude");
			categoryService.addNewCategory("Trabalho");
		};
	}	

}
