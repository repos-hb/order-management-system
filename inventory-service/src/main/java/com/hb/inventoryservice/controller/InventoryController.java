package com.hb.inventoryservice.controller;

import com.hb.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable String skuId){
        return inventoryService.isInStock(skuId);
    }
}
