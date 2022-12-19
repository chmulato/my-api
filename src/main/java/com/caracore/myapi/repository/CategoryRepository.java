package com.caracore.myapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.caracore.myapi.entities.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    
    Optional<CategoryEntity> findByGuid(String guid);

    Optional<CategoryEntity> findByName(String name);

    @Query("select c from Category c where c.guid = ?1 and c.name = ?2")
    CategoryEntity findByGuidAndNameOne(String guid, String name);

}
