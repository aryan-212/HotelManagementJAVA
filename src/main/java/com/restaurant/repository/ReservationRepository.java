package com.restaurant.repository;

import com.restaurant.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByTableIdAndReservationTime(Long tableId, LocalDateTime reservationTime);
    List<Reservation> findByReservationTimeBetweenOrderByReservationTimeAsc(LocalDateTime start, LocalDateTime end);
} 