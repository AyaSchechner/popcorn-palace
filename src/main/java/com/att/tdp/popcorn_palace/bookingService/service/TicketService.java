package com.att.tdp.popcorn_palace.bookingService.service;

import com.att.tdp.popcorn_palace.bookingService.repository.TicketRepository;
import com.att.tdp.popcorn_palace.bookingService.model.Ticket;
import com.att.tdp.popcorn_palace.showtimeService.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.showtimeService.model.Showtime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;


    public Ticket bookTicket(Long showtimeId, String userId, int seatNumber) {
        if (showtimeId == null || userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Showtime ID and user ID must not be null or empty.");
        }
        if (seatNumber <= 0) {
            throw new IllegalArgumentException("Seat number must be a positive integer.");
        }

        // Check if showtime exists
        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
        if (optionalShowtime.isEmpty()) {
            throw new IllegalArgumentException("Showtime with ID " + showtimeId + " not found.");
        }

        // Check if seat is already booked
        if (ticketRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber).isPresent()) {
            System.out.println("Seat already booked");

            throw new IllegalArgumentException("Seat " + seatNumber + " is already booked for this showtime.");
        }

        // Create and save ticket
        Ticket ticket = new Ticket(optionalShowtime.get(), userId, seatNumber);
        return ticketRepository.save(ticket);
    }
}
