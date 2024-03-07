package com.davidrodriguez.projecttf.service;

import com.davidrodriguez.projecttf.dto.InventoryLogDto;
import com.davidrodriguez.projecttf.entity.InventoryLog;
import com.davidrodriguez.projecttf.entity.Item;
import com.davidrodriguez.projecttf.repository.InventoryLogRepository;
import com.davidrodriguez.projecttf.repository.ItemRepository;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryLogService extends AbstractService<InventoryLog, Long> {

  private final InventoryLogRepository inventoryLogRepository;

  private final ItemRepository itemRepository;

  public InventoryLogService(InventoryLogRepository inventoryLogRepository, ItemRepository itemRepository) {
    this.inventoryLogRepository = inventoryLogRepository;
    this.itemRepository = itemRepository;
  }

  @Override
  protected CrudRepository<InventoryLog, Long> getRepository() {
    return inventoryLogRepository;
  }

  public InventoryLog create(InventoryLogDto entity) {
    Optional<Item> existingItem = itemRepository.findById(entity.getItemId());
    if (existingItem.isPresent()) {
      InventoryLog newInventoryLog = InventoryLog.builder()
          .amount(entity.getAmount())
          .transaction(entity.getTransaction())
          .itemId(entity.getItemId()).build();
      return inventoryLogRepository.save(newInventoryLog);
    }
    return null;
  }

  public InventoryLog update(InventoryLog existingInventoryLog, InventoryLogDto inventoryLogDto) {
    Optional<Item> existingItem = itemRepository.findById(existingInventoryLog.getItemId());
    if (existingItem.isPresent()) {
      existingInventoryLog.setAmount(inventoryLogDto.getAmount());
      existingInventoryLog.setTransaction(inventoryLogDto.getTransaction());
      existingInventoryLog.setItemId(inventoryLogDto.getItemId());
      return inventoryLogRepository.save(existingInventoryLog);
    }
    return null;
  }

  public boolean delete(InventoryLogDto inventoryLogDto) {
    inventoryLogRepository.deleteById(inventoryLogDto.getId());
    return true;
  }
}
