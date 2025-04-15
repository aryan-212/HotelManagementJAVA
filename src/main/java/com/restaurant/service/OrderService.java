package com.restaurant.service;

import com.restaurant.model.Order;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order createOrder(Order order);
    Order updateOrder(Long id, Order order);
    void deleteOrder(Long id);
    Order addOrderItem(Long orderId, Long menuItemId, int quantity, String specialInstructions);
    Order updateOrderStatus(Long id, Order.OrderStatus status);
} 