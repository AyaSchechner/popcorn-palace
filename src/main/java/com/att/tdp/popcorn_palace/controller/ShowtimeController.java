package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    
    @Autowired
    private ShowtimeService showtimeService;

    // Add a new showtime (Prevents overlapping showtimes)
    @PostMapping
    public ResponseEntity<?> addShowtime(@RequestBody Showtime showtime) {
        try {
            Showtime savedShowtime = showtimeService.addShowtime(showtime);
            return ResponseEntity.ok(savedShowtime);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Get all showtimes
    @GetMapping
    public ResponseEntity<List<Showtime>> getAllShowtimes() {
        List<Showtime> showtimes = showtimeService.getAllShowtimes();
        return ResponseEntity.ok(showtimes);
    }

    // Get a showtime by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getShowtimeById(@PathVariable Long id) {
        return showtimeService.getShowtimeById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Showtime not found."));
    }

    // Update a showtime (prevent overlapping updates)
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateShowtime(@PathVariable Long id, @RequestBody Showtime updatedShowtime) {
        try {
            Showtime updated = showtimeService.updateShowtime(id, updatedShowtime);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Delete a showtime by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShowtime(@PathVariable Long id) {
        if (showtimeService.deleteShowtime(id)) {
            return ResponseEntity.ok("Showtime deleted successfully.");
        }
        return createErrorResponse(HttpStatus.NOT_FOUND, "Showtime not found.");
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }
}
