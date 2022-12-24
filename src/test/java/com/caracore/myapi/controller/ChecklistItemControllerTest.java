package com.caracore.myapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;
import com.caracore.myapi.service.ChecklistItemService;

@WebMvcTest(ChecklistItemController.class)
public class ChecklistItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ChecklistItemService checklistItemService;

    private ChecklistItemEntity getChecklistItemEntity(Long id, String description, Boolean isCompleted,
    LocalDate deadline, Long categoryId, String categoryName) {

        ChecklistItemEntity entity = new ChecklistItemEntity();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);
        categoryEntity.setGuid(UUID.randomUUID().toString());
        categoryEntity.setName(categoryName);

        entity.setChecklistitemId(id);
        entity.setGuid(UUID.randomUUID().toString());
        entity.setDescription(description);
        entity.setIsCompleted(isCompleted);
        entity.setDeadline(deadline);
        entity.setPostedDate(LocalDate.now());
        entity.setCategory(categoryEntity);

        return entity;
    }

    @Test
    public void shouldCallGetAllChecklistItemsAdnReturn200() throws Exception {
      
        List<ChecklistItemEntity> findAllData = Arrays.asList(
            getChecklistItemEntity(1L, "Item 1", false, LocalDate.of(2022, 10, 01), 1L ,"Cat 1"),
            getChecklistItemEntity(2L, "Item 2", false, LocalDate.of(2022, 10, 02), 2L,"Cat 2")
        );

        when(checklistItemService.findAllChecklistItems()).thenReturn(findAllData);

        this.mockMvc.perform(get("/v1/api/checklist-items"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[*]", hasSize(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].guid").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].isCompleted").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Item 1"));
    
    }

}
