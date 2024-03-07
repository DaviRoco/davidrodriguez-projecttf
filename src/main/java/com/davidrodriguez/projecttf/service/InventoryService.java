package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.dto.InventoryDto;
import com.davidrodriguez.projecttf.entity.Inventory;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.repository.InventoryRepository;
import com.davidrodriguez.projecttf.repository.ItemRepository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends AbstractService<Inventory, Long> {
  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;
  public InventoryService(InventoryRepository inventoryRepository, ItemRepository itemRepository) {
    this.inventoryRepository = inventoryRepository;
    this.itemRepository = itemRepository;
  }

  @Override
  protected CrudRepository<Inventory, Long> getRepository() {
    return inventoryRepository;
  }

  public Inventory create(InventoryDto entity) {
    Optional<Item> existingItem = itemRepository.findById(entity.getItemId());
    if (existingItem.isPresent()){
      Inventory newInventory = Inventory.builder()
          .total(entity.getTotal())
          .itemId(entity.getItemId()).build();
      return inventoryRepository.save(newInventory);
    }
    return null;
  }

  public Inventory update(Inventory existingInventory, InventoryDto inventoryDto) {
    Optional<Item> existingItem = itemRepository.findById(inventoryDto.getItemId());
    if (existingItem.isPresent()){
      existingInventory.setTotal(inventoryDto.getTotal());
      existingInventory.setItemId(inventoryDto.getItemId());
      return inventoryRepository.save(existingInventory);
    }
    return null;
  }

  public boolean delete(InventoryDto inventoryDto) {
    inventoryRepository.deleteById(inventoryDto.getId());
    return true;
  }

}
