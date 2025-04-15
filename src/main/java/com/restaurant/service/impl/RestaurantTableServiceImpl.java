package com.restaurant.service.impl;

import com.restaurant.model.RestaurantTable;
import com.restaurant.repository.RestaurantTableRepository;
import com.restaurant.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Override
    public List<RestaurantTable> getAvailableTables() {
        return tableRepository.findByStatus(RestaurantTable.TableStatus.AVAILABLE);
    }

    @Override
    public RestaurantTable createTable(RestaurantTable table) {
        return tableRepository.save(table);
    }

    @Override
    public RestaurantTable updateTable(RestaurantTable table) {
        return tableRepository.save(table);
    }

    @Override
    public void deleteTable(Long id) {
        tableRepository.deleteById(id);
    }

    @Override
    public RestaurantTable getTableById(Long id) {
        return tableRepository.findById(id).orElse(null);
    }

    @Override
    public List<RestaurantTable> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public boolean isTableAvailable(Long tableId, LocalDateTime reservationTime) {
        RestaurantTable table = getTableById(tableId);
        if (table == null || table.getStatus() != RestaurantTable.TableStatus.AVAILABLE) {
            return false;
        }
        // Check if there are any reservations for this table at the specified time
        return table.getReservations().stream()
                .noneMatch(reservation -> reservation.getReservationTime().equals(reservationTime));
    }
} 