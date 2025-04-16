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
        // Get all orders sorted by time
        List<Order> recentOrders = orderService.getAllOrders().stream()
                .sorted((o1, o2) -> o2.getOrderTime().compareTo(o1.getOrderTime()))
                .limit(5)
                .collect(Collectors.toList());

        // Get pending orders count
        long pendingOrdersCount = orderService.getAllOrders().stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.PENDING)
                .count();

        // Get available tables
        List<RestaurantTable> availableTables = tableService.getAvailableTables();

        // Get low stock items
        List<Inventory> lowStockItems = inventoryService.getLowStockItems();

        // Get today's reservations sorted by time
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        List<Reservation> todayReservations = reservationService.getReservationsByDateRange(startOfDay, endOfDay).stream()
                .sorted((r1, r2) -> r1.getReservationTime().compareTo(r2.getReservationTime()))
                .collect(Collectors.toList());

        // Add attributes to model
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("pendingOrdersCount", pendingOrdersCount);
        model.addAttribute("availableTables", availableTables);
        model.addAttribute("lowStockItems", lowStockItems);
        model.addAttribute("todayReservations", todayReservations);

        return "dashboard";
    }
} 