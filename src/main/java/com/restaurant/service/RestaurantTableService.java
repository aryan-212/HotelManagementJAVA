package com.restaurant.service;

import com.restaurant.model.RestaurantTable;
import java.time.LocalDateTime;
import java.util.List;

public interface RestaurantTableService {
    List<RestaurantTable> getAvailableTables();
    RestaurantTable createTable(RestaurantTable table);
    RestaurantTable updateTable(RestaurantTable table);
    void deleteTable(Long id);
    RestaurantTable getTableById(Long id);
    List<RestaurantTable> getAllTables();
    boolean isTableAvailable(Long tableId, LocalDateTime reservationTime);
} 