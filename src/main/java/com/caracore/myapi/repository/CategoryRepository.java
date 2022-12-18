package com.caracore.myapi.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.caracore.myapi.entities.CategoryEntity;

public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long> {
    
    Optional<CategoryEntity> findByGuid(String guid);

    Optional<CategoryEntity> findByName(String name);

}
