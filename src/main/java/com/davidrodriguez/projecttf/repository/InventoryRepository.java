package com.davidrodriguez.projecttf.repository;

import com.davidrodriguez.projecttf.entity.Inventory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findAllByItemId(Long id);
  List<Inventory> findAllByState(String state);
}
