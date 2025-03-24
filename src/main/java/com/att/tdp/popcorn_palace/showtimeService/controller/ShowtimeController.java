package com.att.tdp.popcorn_palace.showtimeService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.att.tdp.popcorn_palace.showtimeService.dto.ShowtimeRequestDTO;
import com.att.tdp.popcorn_palace.showtimeService.dto.ShowtimeResponseDTO;
import com.att.tdp.popcorn_palace.showtimeService.model.Showtime;
import com.att.tdp.popcorn_palace.showtimeService.service.ShowtimeService;

import java.util.*;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    
    @Autowired
    private ShowtimeService showtimeService;

    // Add a new showtime (Prevents overlapping showtimes)
    @PostMapping
    public ResponseEntity<?> addShowtime(@RequestBody ShowtimeRequestDTO dto) {
        try {
            ShowtimeResponseDTO response = showtimeService.addShowtime(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Get all showtimes (for debugging purposes)
    @GetMapping("/all")
    public ResponseEntity<List<ShowtimeResponseDTO>> getAllShowtimes() {
        List<ShowtimeResponseDTO> showtimes = showtimeService.getAllShowtimes();
        return ResponseEntity.ok(showtimes);
    }

    // Get a showtime by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getShowtimeById(@PathVariable Long id) {
        Optional<ShowtimeResponseDTO> showtime = showtimeService.getShowtimeById(id);
        return showtime
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> createErrorResponse(HttpStatus.NOT_FOUND, "Showtime not found."));
    }
    
    // Update a showtime (prevent overlapping updates)
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateShowtime(@PathVariable Long id, @RequestBody ShowtimeRequestDTO dto) {
        try {
            Optional<Showtime> updated = showtimeService.updateShowtime(id, dto);
            return updated
                .<ResponseEntity<?>>map(s -> ResponseEntity.ok().build()) // No body
                .orElseGet(() -> createErrorResponse(HttpStatus.NOT_FOUND, "Showtime not found."));
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    // Delete a showtime by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShowtime(@PathVariable Long id) {
        if (showtimeService.deleteShowtime(id)) {
            return ResponseEntity.ok().build(); // No body
        }
        return createErrorResponse(HttpStatus.NOT_FOUND, "Showtime not found.");
    }

    // Creates a structured JSON error response
    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }
}
