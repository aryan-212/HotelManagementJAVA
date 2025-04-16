package com.restaurant.controller;

import com.restaurant.model.Reservation;
import com.restaurant.model.RestaurantTable;
import com.restaurant.service.ReservationService;
import com.restaurant.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
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
        model.addAttribute("reservation", new Reservation());
        List<RestaurantTable> availableTables = tableService.getAvailableTables();
        model.addAttribute("availableTables", availableTables);
        return "reservations/form";
    }

    @PostMapping
    public String createReservation(@ModelAttribute Reservation reservation,
                                  @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @RequestParam(name = "time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                  Model model) {
        try {
            LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
            reservation.setReservationTime(reservationDateTime);
            
            // Set default status if not provided
            if (reservation.getStatus() == null) {
                reservation.setStatus(Reservation.ReservationStatus.PENDING);
            }
            
            // Validate table
            if (reservation.getTable() == null || reservation.getTable().getId() == null) {
                throw new IllegalArgumentException("Table is required");
            }
            
            // Check table availability
            if (!tableService.isTableAvailable(reservation.getTable().getId(), reservationDateTime)) {
                model.addAttribute("error", "Selected table is not available at the specified time");
                model.addAttribute("reservation", reservation);
                model.addAttribute("availableTables", tableService.getAvailableTables());
                return "reservations/form";
            }
            
            reservationService.createReservation(reservation);
            return "redirect:/reservations";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating reservation: " + e.getMessage());
            model.addAttribute("reservation", reservation);
            model.addAttribute("availableTables", tableService.getAvailableTables());
            return "reservations/form";
        }
    }

    @GetMapping("/{id}")
    public String viewReservation(@PathVariable("id") Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        return "reservations/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Reservation reservation = reservationService.getReservationById(id);
        model.addAttribute("reservation", reservation);
        List<RestaurantTable> availableTables = tableService.getAvailableTables();
        model.addAttribute("availableTables", availableTables);
        return "reservations/form";
    }

    @PostMapping("/{id}")
    public String updateReservation(@PathVariable("id") Long id,
                                  @ModelAttribute Reservation reservation,
                                  @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  @RequestParam(name = "time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                                  Model model) {
        try {
            LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
            reservation.setReservationTime(reservationDateTime);
            
            if (tableService.isTableAvailable(reservation.getTable().getId(), reservationDateTime)) {
                reservationService.updateReservation(id, reservation);
                return "redirect:/reservations";
            }
            model.addAttribute("error", "Selected table is not available at the specified time");
            model.addAttribute("reservation", reservation);
            model.addAttribute("availableTables", tableService.getAvailableTables());
            return "reservations/form";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating reservation: " + e.getMessage());
            model.addAttribute("reservation", reservation);
            model.addAttribute("availableTables", tableService.getAvailableTables());
            return "reservations/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteReservation(@PathVariable("id") Long id) {
        reservationService.deleteReservation(id);
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/status")
    public String updateReservationStatus(@PathVariable("id") Long id, @RequestParam(name = "status") Reservation.ReservationStatus status) {
        reservationService.updateReservationStatus(id, status);
        return "redirect:/reservations";
    }
} 