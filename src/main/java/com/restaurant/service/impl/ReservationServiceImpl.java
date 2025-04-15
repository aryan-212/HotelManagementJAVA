package com.restaurant.service.impl;

import com.restaurant.model.Reservation;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existingReservation = getReservationById(id);
        existingReservation.setTable(reservation.getTable());
        existingReservation.setCustomerName(reservation.getCustomerName());
        existingReservation.setCustomerPhone(reservation.getCustomerPhone());
        existingReservation.setReservationTime(reservation.getReservationTime());
        existingReservation.setPartySize(reservation.getPartySize());
        existingReservation.setStatus(reservation.getStatus());
        existingReservation.setSpecialRequests(reservation.getSpecialRequests());
        return reservationRepository.save(existingReservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Reservation updateReservationStatus(Long id, Reservation.ReservationStatus status) {
        Reservation reservation = getReservationById(id);
        reservation.setStatus(status);
        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getReservationsByDateRange(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByReservationTimeBetween(start, end);
    }

    @Override
    public boolean isTableAvailable(Long tableId, LocalDateTime reservationTime) {
        List<Reservation> existingReservations = reservationRepository.findByTableIdAndReservationTime(tableId, reservationTime);
        return existingReservations.isEmpty();
    }
} 