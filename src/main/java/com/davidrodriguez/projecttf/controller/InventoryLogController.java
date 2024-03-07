package com.davidrodriguez.projecttf.controller;

import com.davidrodriguez.projecttf.dto.InventoryLogDto;
import com.davidrodriguez.projecttf.entity.InventoryLog;
import com.davidrodriguez.projecttf.service.InventoryLogService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class InventoryLogController {
  private final InventoryLogService inventoryLogService;

  public InventoryLogController(InventoryLogService inventoryLogService) { this.inventoryLogService = inventoryLogService; }

  private final ModelMapper modelMapper = new ModelMapper();

  @GetMapping("/inventory-log")
  public ResponseEntity<List<InventoryLogDto>> get() {
    var listType = new TypeToken<List<InventoryLogDto>>() {}.getType();
    var list = (List<InventoryLogDto>) modelMapper.map(inventoryLogService.findAll(), listType);
    return ResponseEntity.ok(list);
  }

  @GetMapping("/inventory-log/{id}")
  public ResponseEntity<InventoryLogDto> getItemById(@PathVariable Long id) {
    var type = new TypeToken<InventoryLogDto>() {}.getType();
    InventoryLog inventoryLog = inventoryLogService.findOne(id);
    if (inventoryLog != null) {
      InventoryLogDto inventoryLogDto = modelMapper.map(inventoryLogService.findOne(id), type);
      return ResponseEntity.ok(inventoryLogDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  @PostMapping("/inventory-log")
  public ResponseEntity<InventoryLog> createItem(@RequestBody InventoryLogDto inventoryDto) {
    InventoryLog createdInventoryLog = inventoryLogService.create(inventoryDto);
    if (createdInventoryLog != null) {
      return ResponseEntity.status(HttpStatus.CREATED).body(createdInventoryLog);
    } else {
      return ResponseEntity.badRequest().build();
    }

  }
  @PutMapping("/inventory-log")
  public ResponseEntity<InventoryLogDto> updateItem(@RequestBody InventoryLogDto inventoryDto) {
    var type = new TypeToken<InventoryLogDto>() {}.getType();
    InventoryLog existingInventory = inventoryLogService.findOne(inventoryDto.getId());
    if (existingInventory != null){
      InventoryLog updatedInventory = inventoryLogService.update(existingInventory, inventoryDto);
      InventoryLogDto updatedInventoryDto = modelMapper.map(updatedInventory, type);
      return ResponseEntity.ok(updatedInventoryDto);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/inventory-log")
  public ResponseEntity<String> deleteItem(@RequestBody InventoryLogDto inventoryDto) {
    InventoryLog existingInventory = inventoryLogService.findOne(inventoryDto.getId());
    if (existingInventory != null) {
      boolean deleted = inventoryLogService.delete(inventoryDto);
      if (deleted) {
        return ResponseEntity.ok("Inventory Log Entry got deleted successfully");
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory Log Entry not found");
    }
    return ResponseEntity.notFound().build();
  }
}
