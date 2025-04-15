package com.restaurant.service;

import com.restaurant.model.Reservation;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Reservation getReservationById(Long id);
    Reservation createReservation(Reservation reservation);
    Reservation updateReservation(Long id, Reservation reservation);
    void deleteReservation(Long id);
    Reservation updateReservationStatus(Long id, Reservation.ReservationStatus status);
    List<Reservation> getReservationsByDateRange(LocalDateTime start, LocalDateTime end);
    boolean isTableAvailable(Long tableId, LocalDateTime reservationTime);
} 