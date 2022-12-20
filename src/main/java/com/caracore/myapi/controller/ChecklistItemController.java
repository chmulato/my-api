package com.caracore.myapi.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caracore.myapi.dto.ChecklistItemDTO;
import com.caracore.myapi.dto.GuidDTO;
import com.caracore.myapi.entities.ChecklistItemEntity;
import com.caracore.myapi.exception.ValidationException;
import com.caracore.myapi.service.ChecklistItemService;

@RestController
@RequestMapping("/v1/api/checklist-items")
public class ChecklistItemController {

    private ChecklistItemService checklistItemService;
    
    public ChecklistItemController(ChecklistItemService checklistItemService) {
        this.checklistItemService = checklistItemService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChecklistItemDTO>> getAllChecklistItems() {

        List<ChecklistItemDTO> resp = StreamSupport.stream(this.checklistItemService.findAllChecklistItems().spliterator(), false)
        .map(checklistItemEntity -> ChecklistItemDTO.toDTO(checklistItemEntity)).collect(Collectors.toList());

        return new ResponseEntity<>(resp, HttpStatus.OK);   
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GuidDTO> createNewChecklistItem(@RequestBody ChecklistItemDTO checklistItemDTO) {

        if (checklistItemDTO.getCategory() == null) {
            throw new ValidationException("Category cannot null");
        }

        ChecklistItemEntity newChecklistItem = this.checklistItemService.addNewChecklistItem(
            checklistItemDTO.getDescription(), checklistItemDTO.getIsCompleted(),
            checklistItemDTO.getDeadline(), checklistItemDTO.getCategory().getGuid());

        return new ResponseEntity<>(new GuidDTO(newChecklistItem.getGuid()), HttpStatus.CREATED);   
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateChecklistItem(@RequestBody ChecklistItemDTO checklistItemDTO) {

        if (!StringUtils.hasText(checklistItemDTO.getGuid())) {
            throw new ValidationException("Checklist item guid cannot be empty or null");
        }

        this.checklistItemService.updateChecklistItem(checklistItemDTO.getGuid(), checklistItemDTO.getDescription(),
            checklistItemDTO.getIsCompleted(), checklistItemDTO.getDeadline(),
            checklistItemDTO.getCategory() != null ? checklistItemDTO.getCategory().getGuid() : null);
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable String guid) {
        this.checklistItemService.deleteChecklistItem(guid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
