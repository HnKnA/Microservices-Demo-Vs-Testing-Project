package com.example.inventory.controller;

import com.example.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkStock(@RequestParam String productId, @RequestParam int quantity) {
        boolean available = inventoryService.checkStock(productId, quantity);
        return ResponseEntity.ok(available);
    }
}
