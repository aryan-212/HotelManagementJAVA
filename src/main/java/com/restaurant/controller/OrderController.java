package com.restaurant.controller;

import com.restaurant.model.Order;
import com.restaurant.model.RestaurantTable;
import com.restaurant.model.MenuItem;
import com.restaurant.model.OrderItem;
import com.restaurant.service.OrderService;
import com.restaurant.service.RestaurantTableService;
import com.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantTableService tableService;

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(value = "tableId", required = false) Long tableId, Model model) {
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        model.addAttribute("menuItems", menuItems);
        
        if (tableId != null) {
            RestaurantTable table = tableService.getTableById(tableId);
            if (table != null) {
                model.addAttribute("table", table);
            }
        }
        
        List<RestaurantTable> availableTables = tableService.getAvailableTables();
        model.addAttribute("tables", availableTables);
        
        Order order = new Order();
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(0.0);
        model.addAttribute("order", order);
        
        return "orders/form";
    }

    @PostMapping
    public String createOrder(@ModelAttribute("order") Order order, @RequestParam(value = "itemsJson", required = false) String itemsJson) {
        try {
            // Set required fields
            order.setOrderTime(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.PENDING);
            
            // Initialize the items list
            List<OrderItem> orderItems = new ArrayList<>();
            order.setItems(orderItems);
            
            // Get the table from the service to ensure we have the complete entity
            if (order.getTable() == null || order.getTable().getId() == null) {
                System.err.println("Error: Table is null or has null ID");
                throw new IllegalArgumentException("Table is required for creating an order");
            }
            
            System.out.println("Table ID: " + order.getTable().getId());
            RestaurantTable table = tableService.getTableById(order.getTable().getId());
            if (table == null) {
                System.err.println("Error: Table not found with ID: " + order.getTable().getId());
                throw new IllegalArgumentException("Table not found with ID: " + order.getTable().getId());
            }
            
            order.setTable(table);
            
            // Update table status
            table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
            tableService.updateTable(table);
            
            // Parse and add order items
            double totalAmount = 0.0;
            if (itemsJson != null && !itemsJson.isEmpty()) {
                System.out.println("Items JSON: " + itemsJson);
                String[] items = itemsJson.split("\\|");
                for (String item : items) {
                    if (!item.isEmpty()) {
                        String[] parts = item.split(",");
                        Long menuItemId = Long.parseLong(parts[0]);
                        int quantity = Integer.parseInt(parts[1]);
                        
                        System.out.println("Processing menu item: " + menuItemId + " with quantity: " + quantity);
                        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
                        if (menuItem == null) {
                            System.err.println("Error: Menu item not found with ID: " + menuItemId);
                            throw new IllegalArgumentException("Menu item not found with ID: " + menuItemId);
                        }
                        
                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order);
                        orderItem.setMenuItem(menuItem);
                        orderItem.setQuantity(quantity);
                        orderItem.setUnitPrice(menuItem.getPrice());
                        orderItem.setSubtotal(menuItem.getPrice().multiply(new java.math.BigDecimal(quantity)));
                        
                        orderItems.add(orderItem);
                        totalAmount += orderItem.getSubtotal().doubleValue();
                    }
                }
            } else {
                System.err.println("Error: No items selected for the order");
                throw new IllegalArgumentException("At least one menu item is required for creating an order");
            }
            
            order.setTotalAmount(totalAmount);
            
            // Save the order
            System.out.println("Saving order with " + orderItems.size() + " items");
            orderService.createOrder(order);
            return "redirect:/orders";
        } catch (Exception e) {
            // Log the error and redirect to the form with an error message
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/orders/new?error=true";
        }
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable("id") Long id, Model model) {
        Order order = orderService.getOrderById(id);
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        model.addAttribute("order", order);
        model.addAttribute("menuItems", menuItems);
        return "orders/view";
    }

    @PostMapping("/{id}/items")
    public String addOrderItem(@PathVariable("id") Long id,
                             @RequestParam("menuItemId") Long menuItemId,
                             @RequestParam("quantity") int quantity,
                             @RequestParam(value = "specialInstructions", required = false) String specialInstructions) {
        orderService.addOrderItem(id, menuItemId, quantity, specialInstructions);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") Order.OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
} 