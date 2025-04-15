package com.restaurant.controller;

import com.restaurant.model.RestaurantTable;
import com.restaurant.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tables")
public class TableController {

    @Autowired
    private RestaurantTableService tableService;

    @GetMapping
    public String listTables(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        return "tables/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("table", new RestaurantTable());
        return "tables/form";
    }

    @PostMapping
    public String createTable(@ModelAttribute RestaurantTable table) {
        tableService.createTable(table);
        return "redirect:/tables";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("table", tableService.getTableById(id));
        return "tables/form";
    }

    @PostMapping("/{id}")
    public String updateTable(@PathVariable Long id, @ModelAttribute RestaurantTable table) {
        table.setId(id);
        tableService.updateTable(table);
        return "redirect:/tables";
    }

    @PostMapping("/{id}/delete")
    public String deleteTable(@PathVariable Long id) {
        tableService.deleteTable(id);
        return "redirect:/tables";
    }

    @PostMapping("/{id}/status")
    public String updateTableStatus(@PathVariable Long id, @RequestParam RestaurantTable.TableStatus status) {
        RestaurantTable table = tableService.getTableById(id);
        table.setStatus(status);
        tableService.updateTable(table);
        return "redirect:/tables";
    }
} 