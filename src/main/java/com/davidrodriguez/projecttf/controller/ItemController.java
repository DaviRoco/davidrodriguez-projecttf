package com.davidrodriguez.projecttf.controller;

import com.davidrodriguez.projecttf.dto.ItemDto;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.service.ItemService;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

  public ItemController(ItemService itemService) {
    this.itemService = itemService;
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
    } else {
      return ResponseEntity.notFound().build();
    }
  }
/*
  @DeleteMapping("/item/{id}")
  public ResponseEntity<String> deleteItem(@RequestBody ItemDto itemDto) {
    boolean deleted = itemService.delete(itemDto);
    if (deleted) {
      return ResponseEntity.ok("Item got deleted successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
    }
  }
  */
}
