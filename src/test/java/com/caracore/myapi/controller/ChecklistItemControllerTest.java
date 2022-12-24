package com.caracore.myapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.caracore.myapi.dto.CategoryDTO;
import com.caracore.myapi.dto.ChecklistItemDTO;
import com.caracore.myapi.dto.UpdateStatusDTO;
import com.caracore.myapi.entities.CategoryEntity;
import com.caracore.myapi.entities.ChecklistItemEntity;
import com.caracore.myapi.service.ChecklistItemService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ChecklistItemController.class)
public class ChecklistItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ChecklistItemService checklistItemService;

    @Autowired
    private ObjectMapper objectMapper;


    private UpdateStatusDTO getStatusDTO(Boolean isCompleted) {
        UpdateStatusDTO statusDTO = new UpdateStatusDTO();
        statusDTO.setComplete(isCompleted);
        return statusDTO;
    }

    private ChecklistItemDTO getChecklistItemDTO(String description, Boolean isCompleted, LocalDate deadline,
                                                String categoryName) {
        return ChecklistItemDTO.builder()
        .guid(UUID.randomUUID().toString())
        .description(description)
        .isCompleted(isCompleted)
        .deadline(deadline)
        .category(CategoryDTO.builder()
            .guid(UUID.randomUUID().toString())
            .name(categoryName)
        .build())
        .postedDate(LocalDate.now())
        .build();
    }

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
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Item 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].isCompleted").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].deadline").value("2022-10-01"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].postedDate").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].category.guid").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].category.name").value("Cat 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].guid").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value("Item 2"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].isCompleted").value(false))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].deadline").value("2022-10-02"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].postedDate").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].category").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].category.guid").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].category.name").value("Cat 2")); 
    }

    @Test
    public void shouldCallEndpointAndAddNewChecklistItemAndReturn201() throws Exception {

        //having
        when(this.checklistItemService.addNewChecklistItem(anyString(),
         anyBoolean(), any(LocalDate.class), anyString()))
            .thenReturn(
                getChecklistItemEntity(1L, "Item 1", false, LocalDate.of(2022, 10, 01), 1L ,"Cat 1"));

        //when - then
        this.mockMvc.perform(post("/v1/api/checklist-items")
        .content(objectMapper.writeValueAsString(
            getChecklistItemDTO(
                "Test",
                true,
                LocalDate.of(2022, 12, 24),
                "Test Cat")
        ))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.guid").isNotEmpty());

    }

    @Test
    public void shouldCallEndpointAndAddUpdateChecklistItemAndReturn204() throws Exception {

        //having
        when(this.checklistItemService.addNewChecklistItem(anyString(),
         anyBoolean(), any(LocalDate.class), anyString()))
            .thenReturn(
                getChecklistItemEntity(1L, "Item 1", false, LocalDate.of(2022, 10, 01), 1L ,"Cat 1"));

        //when - then
        this.mockMvc.perform(put("/v1/api/checklist-items")
        .content(objectMapper.writeValueAsString(
            getChecklistItemDTO(
                "Test",
                true,
                LocalDate.of(2022, 12, 24),
                "Test Cat")
        ))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());

    }

    @Test
    public void shouldCallEndpointAndAddDeleteChecklistItemAndReturn204() throws Exception {

        //having
        String checklistItemGuid = UUID.randomUUID().toString();

        //when - then
        this.mockMvc.perform(delete("/v1/api/checklist-items/{guid}", checklistItemGuid)
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNoContent());

        verify(checklistItemService, times(1)).deleteChecklistItem(checklistItemGuid);

    }

    @Test
    public void shouldCallEndpointAndAddPatchChecklistItemAndReturn202() throws Exception {

        //having
        String checklistItemGuid = UUID.randomUUID().toString();

        //when - then
        this.mockMvc.perform(patch("/v1/api/checklist-items/{guid}", checklistItemGuid)
        .content(objectMapper.writeValueAsString(
            getStatusDTO(true)
        ))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isAccepted());

        verify(checklistItemService, times(1)).updateIsCompleteStatus(checklistItemGuid, true);

    }
}
