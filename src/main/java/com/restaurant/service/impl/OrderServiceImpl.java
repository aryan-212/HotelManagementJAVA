package com.restaurant.service.impl;

import com.restaurant.model.Order;
import com.restaurant.model.OrderItem;
import com.restaurant.model.MenuItem;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(Order order) {
        try {
            logger.info("Starting order creation process");
            logger.info("Order details before save: {}", order);
            
            // Ensure all required fields are set
            if (order.getTable() == null) {
                throw new IllegalArgumentException("Table is required");
            }
            if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
                throw new IllegalArgumentException("Customer name is required");
            }
            if (order.getCustomerPhone() == null || order.getCustomerPhone().trim().isEmpty()) {
                throw new IllegalArgumentException("Customer phone is required");
            }
            
            // Save the order
            Order savedOrder = orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}", savedOrder.getId());
            
            // Verify the order was saved
            Order retrievedOrder = orderRepository.findById(savedOrder.getId())
                    .orElseThrow(() -> new RuntimeException("Order not found after save"));
            logger.info("Retrieved saved order: {}", retrievedOrder);
            
            return retrievedOrder;
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order order) {
        Order existingOrder = getOrderById(id);
        existingOrder.setTable(order.getTable());
        existingOrder.setCustomerName(order.getCustomerName());
        existingOrder.setCustomerPhone(order.getCustomerPhone());
        existingOrder.setStatus(order.getStatus());
        return orderRepository.save(existingOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Order addOrderItem(Long orderId, Long menuItemId, int quantity, String specialInstructions) {
        Order order = getOrderById(orderId);
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(menuItem.getPrice());
        orderItem.setSpecialInstructions(specialInstructions);

        order.getItems().add(orderItem);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
} 