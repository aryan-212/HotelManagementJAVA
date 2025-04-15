package com.restaurant.controller;

import com.restaurant.model.Order;
import com.restaurant.model.RestaurantTable;
import com.restaurant.model.MenuItem;
import com.restaurant.service.OrderService;
import com.restaurant.service.RestaurantTableService;
import com.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String showCreateForm(Model model) {
        List<RestaurantTable> availableTables = tableService.getAvailableTables();
        model.addAttribute("tables", availableTables);
        model.addAttribute("order", new Order());
        return "orders/form";
    }

    @PostMapping
    public String createOrder(@ModelAttribute Order order) {
        orderService.createOrder(order);
        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        List<MenuItem> menuItems = menuItemService.getAllMenuItems();
        model.addAttribute("order", order);
        model.addAttribute("menuItems", menuItems);
        return "orders/view";
    }

    @PostMapping("/{id}/items")
    public String addOrderItem(@PathVariable Long id,
                             @RequestParam Long menuItemId,
                             @RequestParam int quantity,
                             @RequestParam(required = false) String specialInstructions) {
        orderService.addOrderItem(id, menuItemId, quantity, specialInstructions);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam Order.OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
} 