package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    // Book a ticket (Ensures no duplicate bookings)
    public Ticket bookTicket(Long showtimeId, String userId, int seatNumber) {
        if (seatNumber <= 0) {
            throw new IllegalArgumentException("Seat number must be a positive integer.");
        }
        // Check if showtime exists
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Showtime with ID " + showtimeId + " not found."));

        // Check if seat is already booked
        if (ticketRepository.findByShowtimeIdAndSeatNumber(showtimeId, seatNumber).isPresent()) {
            throw new IllegalArgumentException("Seat " + seatNumber + " is already booked for this showtime.");
        }

        // Save the ticket
        Ticket ticket = new Ticket(showtime, userId, seatNumber);
        return ticketRepository.save(ticket);
    }

    // Get all tickets for a showtime
    public List<Ticket> getTicketsForShowtime(Long showtimeId) {
        return ticketRepository.findByShowtimeId(showtimeId);
    }

    // Cancel a ticket
    public boolean cancelTicket(Long ticketId) {
        if (ticketRepository.existsById(ticketId)) {
            ticketRepository.deleteById(ticketId);
            return true;
        }
        return false;
    }
}
