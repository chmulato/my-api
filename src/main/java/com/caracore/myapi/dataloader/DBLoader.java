package com.caracore.myapi.dataloader;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.repository.CategoryRepository;
import com.caracore.myapi.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DBLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {

        log.info("Populating db with categories ...");
        
        List<String> categoryNames = Arrays.asList(
            "Trabalho", "Saude", "Educacao", "Pessoal", "Outros"
        );

        for (String categoryName : categoryNames) {
            Optional<CategoryEntity> catOpt = this.categoryRepository.findByName(categoryName);
            if (!catOpt.isPresent()) {
                categoryService.addNewCategory(categoryName);
            }
        }
        
    }
    
}
