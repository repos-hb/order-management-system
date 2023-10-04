package com.hb.inventoryservice.repository;

import com.hb.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuId(String skuId);

    List<Inventory> findBySkuIdIn(List<String> skuIds);
}
