package com.davidrodriguez.projecttf.controller;

import com.davidrodriguez.projecttf.dto.InventoryDto;
import com.davidrodriguez.projecttf.dto.ItemDto;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.service.InventoryService;
import com.davidrodriguez.projecttf.service.ItemService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.bytebuddy.description.method.MethodDescription;
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
public class ItemController {

  private final ItemService itemService;
  private final InventoryService inventoryService;

  public ItemController(ItemService itemService, InventoryService inventoryService) {
    this.itemService = itemService;
    this.inventoryService = inventoryService;
  }
  private final ModelMapper modelMapper = new ModelMapper();
  @GetMapping("/item")
  public ResponseEntity<List<ItemDto>> get() {
    var listType = new TypeToken<List<ItemDto>>() {}.getType();
    var list = (List<ItemDto>) modelMapper.map(itemService.findAll(), listType);
    return ResponseEntity.ok(list);
  }
  @GetMapping("/item/{id}")
  public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
    var type = new TypeToken<ItemDto>() {}.getType();
    Item item = itemService.findOne(id);
    if (item != null) {
      ItemDto itemDto = modelMapper.map(itemService.findOne(id), type);
      return ResponseEntity.ok(itemDto);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
  @PostMapping("/item")
  public ResponseEntity<Item> createItem(@RequestBody ItemDto itemDto) {
    Item createdItem = itemService.create(itemDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
  }
  @PutMapping("/item")
  public ResponseEntity<ItemDto> updateItem(@RequestBody ItemDto itemDto) {
    var type = new TypeToken<ItemDto>() {}.getType();
    Item existingItem = itemService.findOne(itemDto.getId());
    if (existingItem != null){
      Item updatedItem = itemService.update(existingItem, itemDto);
      ItemDto updatedItemDto = modelMapper.map(updatedItem, type);
      return ResponseEntity.ok(updatedItemDto);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/item")
  public ResponseEntity<String> deleteItem(@RequestBody ItemDto itemDto) {
    Item existingItem = itemService.findOne(itemDto.getId());
    if (existingItem != null) {
      var listType = new TypeToken<List<InventoryDto>>() {}.getType();
      List<InventoryDto> inventories = modelMapper.map(inventoryService.deleteInventoriesByItemId(itemDto.getId()), listType);
      boolean deleted = itemService.delete(itemDto);
      if (deleted) {
        return ResponseEntity.ok("Item got deleted successfully, with the respective inventories: " + inventories.toString());
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }
    return ResponseEntity.notFound().build();
  }
}
