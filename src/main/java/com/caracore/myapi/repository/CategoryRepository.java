package com.caracore.myapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caracore.myapi.entities.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    
    Optional<CategoryEntity> findByGuid(String guid);

    Optional<CategoryEntity> findByName(String name);

}
