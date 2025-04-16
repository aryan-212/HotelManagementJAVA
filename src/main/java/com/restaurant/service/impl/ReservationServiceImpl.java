package com.restaurant.service.impl;

import com.restaurant.model.Reservation;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

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
        logger.info("Fetching reservations between {} and {}", start, end);
        List<Reservation> reservations = reservationRepository.findByReservationTimeBetweenOrderByReservationTimeAsc(start, end);
        logger.info("Found {} reservations", reservations.size());
        return reservations;
    }

    @Override
    public boolean isTableAvailable(Long tableId, LocalDateTime reservationTime) {
        List<Reservation> existingReservations = reservationRepository.findByTableIdAndReservationTime(tableId, reservationTime);
        return existingReservations.isEmpty();
    }
} 