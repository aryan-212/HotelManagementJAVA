package com.restaurant.controller;

import com.restaurant.model.Reservation;
import com.restaurant.model.RestaurantTable;
import com.restaurant.service.ReservationService;
import com.restaurant.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RestaurantTableService tableService;

    @GetMapping
    public String listReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservations/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<RestaurantTable> availableTables = tableService.getAvailableTables();
        model.addAttribute("tables", availableTables);
        model.addAttribute("reservation", new Reservation());
        return "reservations/form";
    }

    @PostMapping
    public String createReservation(@ModelAttribute Reservation reservation) {
        if (reservationService.isTableAvailable(reservation.getTable().getId(), reservation.getReservationTime())) {
            reservationService.createReservation(reservation);
            return "redirect:/reservations";
        }
        return "redirect:/reservations/new?error=table-unavailable";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        List<RestaurantTable> availableTables = tableService.getAvailableTables();
        model.addAttribute("reservation", reservation);
        model.addAttribute("tables", availableTables);
        return "reservations/form";
    }

    @PostMapping("/{id}")
    public String updateReservation(@PathVariable Long id, @ModelAttribute Reservation reservation) {
        if (reservationService.isTableAvailable(reservation.getTable().getId(), reservation.getReservationTime())) {
            reservationService.updateReservation(id, reservation);
            return "redirect:/reservations";
        }
        return "redirect:/reservations/" + id + "/edit?error=table-unavailable";
    }

    @PostMapping("/{id}/delete")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/status")
    public String updateReservationStatus(@PathVariable Long id, @RequestParam Reservation.ReservationStatus status) {
        reservationService.updateReservationStatus(id, status);
        return "redirect:/reservations";
    }
} 