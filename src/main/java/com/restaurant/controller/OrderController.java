package com.restaurant.controller;

import com.restaurant.model.Order;
import com.restaurant.model.RestaurantTable;
import com.restaurant.model.MenuItem;
import com.restaurant.model.OrderItem;
import com.restaurant.service.OrderService;
import com.restaurant.service.RestaurantTableService;
import com.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantTableService tableService;

    @Autowired
    private MenuItemService menuItemService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(RestaurantTable.class, "table", new CustomNumberEditor(Long.class, true) {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.isEmpty()) {
                    Long id = Long.parseLong(text);
                    RestaurantTable table = tableService.getTableById(id);
                    setValue(table);
                }
            }
        });
    }

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

    @PostMapping("/new")
    public String createOrder(@ModelAttribute Order order, 
                            @RequestParam(value = "itemsJson", required = false) String itemsJson,
                            @RequestParam(value = "tableId", required = false) Long tableId,
                            Model model) {
        try {
            logger.info("Starting order creation process");
            logger.info("Order details: {}", order);
            logger.info("Items JSON: {}", itemsJson);
            logger.info("Table ID: {}", tableId);
            
            // Validate table
            if (tableId == null) {
                throw new IllegalArgumentException("Table is required");
            }

            // Get and validate table
            RestaurantTable table = tableService.getTableById(tableId);
            logger.info("Found table: {}", table);
            if (table == null) {
                throw new IllegalArgumentException("Table not found");
            }

            // Set the table
            order.setTable(table);
            logger.info("Table set on order");

            // Validate table status
            if (table.getStatus() != RestaurantTable.TableStatus.AVAILABLE) {
                throw new IllegalArgumentException("Table is not available");
            }

            // Validate customer info
            if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name is required");
            }
            if (order.getCustomerPhone() == null || order.getCustomerPhone().trim().isEmpty()) {
                throw new IllegalArgumentException("Customer phone is required");
            }

            // Validate items
            if (itemsJson == null || itemsJson.trim().isEmpty()) {
                throw new IllegalArgumentException("At least one item is required");
            }

            // Set order details
            order.setOrderTime(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.PENDING);
            order.setItems(new ArrayList<>());
            logger.info("Basic order details set");

            // Parse and validate items
            String[] itemPairs = itemsJson.split("\\|");
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (String itemPair : itemPairs) {
                String[] parts = itemPair.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid item format");
                }

                Long menuItemId = Long.parseLong(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                if (quantity <= 0) {
                    throw new IllegalArgumentException("Quantity must be greater than 0");
                }

                MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
                logger.info("Found menu item: {}", menuItem);
                if (menuItem == null) {
                    throw new IllegalArgumentException("Menu item not found: " + menuItemId);
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(quantity);
                orderItem.setUnitPrice(menuItem.getPrice());
                orderItem.setSubtotal(menuItem.getPrice().multiply(new BigDecimal(quantity)));
                order.getItems().add(orderItem);
                logger.info("Added order item: {}", orderItem);

                totalAmount = totalAmount.add(orderItem.getSubtotal());
            }

            order.setTotalAmount(totalAmount.doubleValue());
            logger.info("Order total amount: {}", totalAmount);

            // Update table status
            table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
            tableService.updateTable(table);
            logger.info("Table status updated");

            // Save order
            logger.info("Saving order...");
            Order savedOrder = orderService.createOrder(order);
            logger.info("Order saved with ID: {}", savedOrder.getId());

            return "redirect:/orders";
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            model.addAttribute("error", "Error creating order: " + e.getMessage());
            model.addAttribute("menuItems", menuItemService.getAllMenuItems());
            model.addAttribute("tables", tableService.getAvailableTables());
            return "orders/form";
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

    @PostMapping("/api/orders")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request) {
        try {
            logger.info("Received order creation request: {}", request);
            
            // Validate required fields
            if (!request.containsKey("tableId")) {
                return ResponseEntity.badRequest().body("Table ID is required");
            }
            if (!request.containsKey("customerName")) {
                return ResponseEntity.badRequest().body("Customer name is required");
            }
            if (!request.containsKey("items")) {
                return ResponseEntity.badRequest().body("Order items are required");
            }

            // Get table
            Long tableId = Long.parseLong(request.get("tableId").toString());
            RestaurantTable table = tableService.getTableById(tableId);
            if (table == null) {
                return ResponseEntity.badRequest().body("Table not found");
            }
            if (table.getStatus() != RestaurantTable.TableStatus.AVAILABLE) {
                return ResponseEntity.badRequest().body("Table is not available");
            }

            // Create order
            Order order = new Order();
            order.setTable(table);
            order.setCustomerName(request.get("customerName").toString());
            order.setCustomerPhone(request.get("customerPhone").toString());
            order.setOrderTime(LocalDateTime.now());
            order.setStatus(Order.OrderStatus.PENDING);

            // Parse and add order items
            List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");
            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (Map<String, Object> item : items) {
                Long menuItemId = Long.parseLong(item.get("menuItemId").toString());
                int quantity = Integer.parseInt(item.get("quantity").toString());

                MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
                if (menuItem == null) {
                    return ResponseEntity.badRequest().body("Menu item not found: " + menuItemId);
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(quantity);
                orderItem.setOrder(order);
                orderItems.add(orderItem);

                totalAmount = totalAmount.add(menuItem.getPrice().multiply(new BigDecimal(quantity)));
            }

            order.setItems(orderItems);
            order.setTotalAmount(totalAmount.doubleValue());

            // Save order
            Order savedOrder = orderService.createOrder(order);
            logger.info("Order created successfully: {}", savedOrder);

            // Update table status
            table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
            tableService.updateTable(table);

            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error creating order: " + e.getMessage());
        }
    }
} 