package com.restaurant.controller;

import com.restaurant.model.MenuItem;
import com.restaurant.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu")
public class MenuItemController {
    
    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public String listMenuItems(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "menu/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("menuItem", new MenuItem());
        return "menu/form";
    }

    @PostMapping("/new")
    public String createMenuItem(@ModelAttribute("menuItem") MenuItem menuItem) {
        menuItemService.createMenuItem(menuItem);
        return "redirect:/menu";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("menuItem", menuItemService.getMenuItemById(id));
        return "menu/form";
    }

    @PostMapping("/{id}/edit")
    public String updateMenuItem(@PathVariable("id") Long id, @ModelAttribute("menuItem") MenuItem menuItem) {
        menuItemService.updateMenuItem(id, menuItem);
        return "redirect:/menu";
    }

    @PostMapping("/{id}/delete")
    public String deleteMenuItem(@PathVariable("id") Long id) {
        try {
            menuItemService.deleteMenuItem(id);
            return "redirect:/menu";
        } catch (RuntimeException e) {
            // If the menu item doesn't exist, just redirect to the menu list
            return "redirect:/menu";
        }
    }
} 