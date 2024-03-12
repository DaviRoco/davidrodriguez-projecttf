package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.dto.InventoryDto;
import com.davidrodriguez.projecttf.dto.InventoryLogDto;
import com.davidrodriguez.projecttf.entity.Inventory;
import com.davidrodriguez.projecttf.entity.InventoryLog;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.repository.InventoryLogRepository;
import com.davidrodriguez.projecttf.repository.InventoryRepository;
import com.davidrodriguez.projecttf.repository.ItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService extends AbstractService<Inventory, Long> {
  private final InventoryRepository inventoryRepository;
  private final ItemRepository itemRepository;
  private final InventoryLogRepository inventoryLogRepository;
  public InventoryService(InventoryRepository inventoryRepository, ItemRepository itemRepository, InventoryLogRepository inventoryLogRepository) {
    this.inventoryRepository = inventoryRepository;
    this.itemRepository = itemRepository;
    this.inventoryLogRepository = inventoryLogRepository;
  }

  @Override
  protected CrudRepository<Inventory, Long> getRepository() {
    return inventoryRepository;
  }

  public Inventory create(InventoryDto entity) {
    Optional<Item> existingItem = itemRepository.findById(entity.getItemId());
    if (existingItem.isPresent()){
      Inventory newInventory = Inventory.builder()
          .total(0)
          .state("Activo")
          .itemId(entity.getItemId()).build();
      return inventoryRepository.save(newInventory);
    }
    return null;
  }

  public Inventory update(Inventory existingInventory, InventoryDto inventoryDto) {
    Optional<Item> existingItem = itemRepository.findById(inventoryDto.getItemId());
    if (existingItem.isPresent()){
      if ((existingInventory.getTotal() + inventoryDto.getTotal()) >= 0){
        String transaction = "Inventario actualizado para el Inventario: " + existingInventory.getId() + " con el item " + existingItem.get().getName();
        int newAmount = existingInventory.getTotal() + inventoryDto.getTotal();
        if (inventoryDto.getTotal() > 0) {
          transaction = transaction + ", con un restock de " + (newAmount);
        } else if (inventoryDto.getTotal() < 0) {
          transaction = transaction + ", con ventas de " + (inventoryDto.getTotal()*-1);
        } else {
          transaction = transaction + ", se realizo un cambio en la información del inventario.";
        }
        InventoryLog inventoryLog = new InventoryLog(0L, transaction, inventoryDto.getTotal(), existingItem.get().getId());
        inventoryLogRepository.save(inventoryLog);
        existingInventory.setTotal(newAmount);
        existingInventory.setDescription(inventoryDto.getDescription());
        existingInventory.setItemId(inventoryDto.getItemId());
        existingInventory.setState(existingItem.get().getState());
        return inventoryRepository.save(existingInventory);
      }
      return null;
    }
    return null;
  }

  public boolean delete(InventoryDto inventoryDto) {
    inventoryRepository.deleteById(inventoryDto.getId());
    return true;
  }

  public List<Inventory> deleteInventoriesByItemId(Long id) {
    List<Inventory> inventories = inventoryRepository.findAllByItemId(id);
    for (int i = 0; i < inventories.size(); i++) {
      inventoryRepository.deleteById(inventories.get(i).getId());
    }
    return inventories;
  }

  public List<Inventory> findAllByState(String state) {
    return inventoryRepository.findAllByState(state);
  }
}
