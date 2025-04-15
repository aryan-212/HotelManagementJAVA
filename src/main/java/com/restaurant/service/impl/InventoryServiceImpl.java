package com.restaurant.service.impl;

import com.restaurant.model.Inventory;
import com.restaurant.repository.InventoryRepository;
import com.restaurant.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> getAllItems() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory getItemById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory item not found with id: " + id));
    }

    @Override
    public Inventory saveItem(Inventory item) {
        return inventoryRepository.save(item);
    }

    @Override
    public void deleteItem(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public List<Inventory> getLowStockItems() {
        return inventoryRepository.findLowStockItems();
    }
} 