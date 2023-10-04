package com.hb.inventoryservice.service;

import com.hb.inventoryservice.dto.InventoryResponse;
import com.hb.inventoryservice.model.Inventory;
import com.hb.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuIds){
        List<Inventory> stockList = inventoryRepository.findBySkuIdIn(skuIds);

        return stockList.stream().map(stock->
                    InventoryResponse.builder()
                            .skuId(stock.getSkuId())
                            .isInStock(stock.getQuantity()>0)
                            .build()
                ).collect(Collectors.toList());
    }
}
