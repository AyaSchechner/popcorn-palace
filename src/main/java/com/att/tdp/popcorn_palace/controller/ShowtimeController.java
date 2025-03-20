package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
            return ResponseEntity.status(HttpStatus.CREATED).body(savedShowtime);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all showtimes
    @GetMapping
    public ResponseEntity<List<Showtime>> getAllShowtimes() {
        return ResponseEntity.ok(showtimeService.getAllShowtimes());
    }

    // Get a showtime by id
    @GetMapping("/{id}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long id) {
        Optional<Showtime> showtime = showtimeService.getShowtimeById(id);
        return showtime.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update a showtime (prevent overlapping updates)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateShowtime(@PathVariable Long id, @RequestBody Showtime updatedShowtime) {
        try {
            Showtime showtime = showtimeService.updateShowtime(id, updatedShowtime);
            return ResponseEntity.ok(showtime);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Delete a showtime by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShowtime(@PathVariable Long id) {
        if (showtimeService.deleteShowtime(id)) {
            return ResponseEntity.ok("Showtime deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Showtime not found.");
        }
    }
}
