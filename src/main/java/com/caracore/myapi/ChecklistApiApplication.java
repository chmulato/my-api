package com.caracore.myapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.repository.CategoryRepository;

@SpringBootApplication
public class ChecklistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChecklistApiApplication.class, args);
	}
		
	@Bean
	CommandLineRunner initDatabase(CategoryRepository categoryRepository) {

		return args -> {

			categoryRepository.deleteAll();
			
			CategoryEntity cat01 = new CategoryEntity();
			cat01.setCategoryId(1L);
			cat01.setGuid("aaa-bbb-ccc-dddd");
			cat01.setName("Saúde");
			
			categoryRepository.save(cat01);

			CategoryEntity cat02 = new CategoryEntity();
			cat02.setCategoryId(2L);
			cat02.setGuid("aaa-bbb-ccc-dddf");
			cat02.setName("Trabalho");
			
			categoryRepository.save(cat02);
			
		};
	}	

}
