package com.restaurant.controller;

import com.restaurant.model.Order;
import com.restaurant.model.RestaurantTable;
import com.restaurant.model.Inventory;
import com.restaurant.model.Reservation;
import com.restaurant.service.OrderService;
import com.restaurant.service.RestaurantTableService;
import com.restaurant.service.InventoryService;
import com.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantTableService tableService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String dashboard(Model model) {
        // Get pending orders
        List<Order> pendingOrders = orderService.getAllOrders().stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.PENDING)
                .collect(Collectors.toList());

        // Get available tables
        List<RestaurantTable> availableTables = tableService.getAvailableTables();

        // Get low stock items
        List<Inventory> lowStockItems = inventoryService.getLowStockItems();

        // Get today's reservations
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        List<Reservation> todayReservations = reservationService.getReservationsByDateRange(startOfDay, endOfDay);

        // Add attributes to model
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("availableTables", availableTables);
        model.addAttribute("lowStockItems", lowStockItems);
        model.addAttribute("todayReservations", todayReservations);

        return "dashboard";
    }
} 