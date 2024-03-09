package com.davidrodriguez.projecttf.repository;

import com.davidrodriguez.projecttf.entity.InventoryLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
}
