package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.dto.ItemDto;
import com.davidrodriguez.projecttf.entity.Inventory;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.repository.InventoryRepository;
import com.davidrodriguez.projecttf.repository.ItemRepository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService extends AbstractService<Item, Long> {

  private final ItemRepository itemRepository;

  private final InventoryRepository inventoryRepository;
  public ItemService(ItemRepository itemRepository, InventoryRepository inventoryRepository){
    this.itemRepository = itemRepository;
    this.inventoryRepository = inventoryRepository;
  }
  @Override
  protected CrudRepository<Item, Long> getRepository() {
    return itemRepository;
  }

  public Item create(ItemDto entity) {
    Item newItem = Item.builder().name(entity.getName()).state("activo").build();
    Item createdItem = itemRepository.save(newItem);
    Inventory newInventory = Inventory.builder()
        .total(0)
        .state("activo")
        .description("Inventario auto-generado al crear un item.")
        .itemId(createdItem.getId()).build();
    inventoryRepository.save(newInventory);
    return createdItem;
  }

  public Item update(Item existingItem, ItemDto itemDto) {
    existingItem.setName(itemDto.getName());
    return itemRepository.save(existingItem);
  }

  public Item changeStateItem(Item existingItem) {
    String stateChangeEnumeration;
    if (existingItem.getState().equals("inactivo")){
      stateChangeEnumeration = "activo";
      existingItem.setState(stateChangeEnumeration);
    } else {
      stateChangeEnumeration = "inactivo";
      existingItem.setState(stateChangeEnumeration);
    }
    List<Inventory> inventories = inventoryRepository.findAllByItemId(existingItem.getId());
    for (int i = 0; i < inventories.size(); i++) {
      inventories.get(i).setState(stateChangeEnumeration);
      inventoryRepository.save(inventories.get(i));
    }
    return itemRepository.save(existingItem);
  }

  public boolean delete(ItemDto itemDto) {
    itemRepository.deleteById(itemDto.getId());
    return true;
  }
}
