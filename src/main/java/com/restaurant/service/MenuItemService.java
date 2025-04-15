package com.restaurant.service;

import com.restaurant.model.MenuItem;
import java.util.List;

public interface MenuItemService {
    List<MenuItem> getAllMenuItems();
    MenuItem getMenuItemById(Long id);
    MenuItem createMenuItem(MenuItem menuItem);
    MenuItem updateMenuItem(Long id, MenuItem menuItem);
    void deleteMenuItem(Long id);
} 