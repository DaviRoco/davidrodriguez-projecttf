package com.davidrodriguez.projecttf.repository;

import com.davidrodriguez.projecttf.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
