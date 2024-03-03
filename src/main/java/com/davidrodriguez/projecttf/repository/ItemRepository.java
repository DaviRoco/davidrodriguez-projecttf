package com.davidrodriguez.projecttf.repository;

import com.davidrodriguez.projecttf.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
