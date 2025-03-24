package com.att.tdp.popcorn_palace.bookingService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.att.tdp.popcorn_palace.bookingService.dto.TicketRequest;
import com.att.tdp.popcorn_palace.bookingService.model.Ticket;
import com.att.tdp.popcorn_palace.bookingService.service.TicketService;

import java.util.*;

@RestController
@RequestMapping("/bookings")
public class TicketController {
    
    @Autowired
    private TicketService ticketService;

    // Book a new ticket
    @PostMapping
    public ResponseEntity<?> bookTicket(@RequestBody TicketRequest request) {
        if (request == null) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Ticket request is missing.");
        }
        try {
            Ticket ticket = ticketService.bookTicket(request.getShowtimeId(), request.getUserId(), request.getSeatNumber());
            return ResponseEntity.ok(Map.of("bookingId", ticket.getId().toString()));
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Creates a structured JSON error response
    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }
}
