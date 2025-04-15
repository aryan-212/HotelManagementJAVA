package com.restaurant.controller;

import com.restaurant.model.Inventory;
import com.restaurant.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public String listInventory(Model model) {
        model.addAttribute("items", inventoryService.getAllItems());
        return "inventory/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new Inventory());
        return "inventory/form";
    }

    @PostMapping
    public String createItem(@ModelAttribute Inventory item) {
        inventoryService.saveItem(item);
        return "redirect:/inventory";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("item", inventoryService.getItemById(id));
        return "inventory/form";
    }

    @PostMapping("/{id}")
    public String updateItem(@PathVariable Long id, @ModelAttribute Inventory item) {
        item.setId(id);
        inventoryService.saveItem(item);
        return "redirect:/inventory";
    }

    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return "redirect:/inventory";
    }
} 