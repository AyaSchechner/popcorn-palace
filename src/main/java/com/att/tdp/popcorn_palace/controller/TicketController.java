package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.model.Ticket;
import com.att.tdp.popcorn_palace.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;

    // Book a new ticket
    @PostMapping("/book")
    public ResponseEntity<?> bookTicket(@RequestParam Long showtimeId, @RequestParam String customerName, @RequestParam int seatNumber) {
        try {
            Ticket ticket = ticketService.bookTicket(showtimeId, customerName, seatNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all tickets for a showtime
    @GetMapping("/{showtimeId}")
    public ResponseEntity<List<Ticket>> getTicketsForShowtime(@PathVariable Long showtimeId) {
        return ResponseEntity.ok(ticketService.getTicketsForShowtime(showtimeId));
    }

    // Cancel a ticket by ID
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> cancelTicket(@PathVariable Long ticketId) {
        if (ticketService.cancelTicket(ticketId)) {
            return ResponseEntity.ok("Ticket canceled successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found.");
        }
    }
}
