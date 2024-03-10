package com.davidrodriguez.projecttf.controller;

import com.davidrodriguez.projecttf.dto.InventoryDto;
import com.davidrodriguez.projecttf.entity.Inventory;
import com.davidrodriguez.projecttf.service.InventoryService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class InventoryController {
  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) { this.inventoryService = inventoryService; }

  private final ModelMapper modelMapper = new ModelMapper();

  @GetMapping("/inventory")
  public ResponseEntity<List<InventoryDto>> get(@RequestParam String state) {
    var listType = new TypeToken<List<InventoryDto>>() {}.getType();
    List<InventoryDto> list;
    if (state.equals("inactivo")) {
      list = modelMapper.map(inventoryService.findAllByState("inactivo"), listType);
    } else if (state.equals("activo")) {
      list = modelMapper.map(inventoryService.findAllByState("activo"), listType);
    } else {
      list = modelMapper.map(inventoryService.findAll(), listType);
    }
    return ResponseEntity.ok(list);
  }

  @GetMapping("/inventory/{id}")
  public ResponseEntity<InventoryDto> getItemById(@PathVariable Long id) {
    var type = new TypeToken<InventoryDto>() {}.getType();
    Inventory inventory = inventoryService.findOne(id);
    if (inventory != null) {
      InventoryDto inventoryDto = modelMapper.map(inventoryService.findOne(id), type);
      return ResponseEntity.ok(inventoryDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  @PostMapping("/inventory")
  public ResponseEntity<Inventory> createItem(@RequestBody InventoryDto inventoryDto) {
    Inventory createdInventory = inventoryService.create(inventoryDto);
    if (createdInventory != null) {
      return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
    } else {
      return ResponseEntity.badRequest().build();
    }

  }
  @PutMapping("/inventory")
  public ResponseEntity<InventoryDto> updateItem(@RequestBody InventoryDto inventoryDto) {
    var type = new TypeToken<InventoryDto>() {}.getType();
    Inventory existingInventory = inventoryService.findOne(inventoryDto.getId());
    if (existingInventory != null){
      Inventory updatedInventory = inventoryService.update(existingInventory, inventoryDto);
      if (updatedInventory != null) {
        InventoryDto updatedInventoryDto = modelMapper.map(updatedInventory, type);
        return ResponseEntity.ok(updatedInventoryDto);
      }
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/inventory")
  public ResponseEntity<String> deleteItem(@RequestBody InventoryDto inventoryDto) {
    Inventory existingInventory = inventoryService.findOne(inventoryDto.getId());
    if (existingInventory != null) {
      boolean deleted = inventoryService.delete(inventoryDto);
      if (deleted) {
        return ResponseEntity.ok("Inventory Entry got deleted successfully");
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inventory Entry not found");
    }
    return ResponseEntity.notFound().build();
  }
}
