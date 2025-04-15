package com.restaurant.service;

import com.restaurant.model.Inventory;
import java.util.List;

public interface InventoryService {
    List<Inventory> getAllItems();
    Inventory getItemById(Long id);
    Inventory saveItem(Inventory item);
    void deleteItem(Long id);
    List<Inventory> getLowStockItems();
} 