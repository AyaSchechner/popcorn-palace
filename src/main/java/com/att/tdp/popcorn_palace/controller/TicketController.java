package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.service.TicketService;
import com.att.tdp.popcorn_palace.dto.TicketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/bookings")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;

    // Book a new ticket
    @PostMapping
    public ResponseEntity<?> bookTicket(@RequestBody TicketRequest request) {
        try {
            Ticket ticket = ticketService.bookTicket(request.getShowtimeId(), request.getUserId(), request.getSeatNumber());
            return ResponseEntity.ok(Map.of("bookingId", ticket.getId().toString()));
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // Get all tickets for a showtime
    @GetMapping("/{showtimeId}")
    public ResponseEntity<List<Ticket>> getTicketsForShowtime(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(ticketService.getTicketsForShowtime(showtimeId));
    }

    // Cancel a ticket by ID
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<?> cancelTicket(@PathVariable Long ticketId) {
        if (ticketService.cancelTicket(ticketId)) {
            return ResponseEntity.ok("Ticket canceled successfully.");
        }
        return createErrorResponse(HttpStatus.NOT_FOUND, "Ticket not found.");
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }

}
