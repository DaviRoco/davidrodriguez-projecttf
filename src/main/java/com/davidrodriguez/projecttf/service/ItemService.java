package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.dto.ItemDto;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends AbstractService<Item, Long> {

  private final ItemRepository itemRepository;
  public ItemService(ItemRepository itemRepository){
    this.itemRepository = itemRepository;
  }
  @Override
  protected CrudRepository<Item, Long> getRepository() {
    return itemRepository;
  }

  public Item create(ItemDto entity) {
    Item newItem = Item.builder().name(entity.getName()).build();
    return itemRepository.save(newItem);
  }

  public Item update(Item existingItem, ItemDto itemDto) {
    existingItem.setName(itemDto.getName());
    return itemRepository.save(existingItem);
  }


}
