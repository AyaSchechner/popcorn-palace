package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    // Add a new showtime (Prevents overlapping showtimes)
    public Showtime addShowtime(Showtime showtime) {
        // Check if an overlapping showtime exists
        List<Showtime> conflictingShowtimes = showtimeRepository.findByTheaterAndStartTimeBetween(
            showtime.getTheater(), showtime.getStartTime(), showtime.getEndTime()
        );

        if (!conflictingShowtimes.isEmpty()) {
            throw new RuntimeException("Overlapping showtime exists in the theater. Please choose a different time.");
        }

        return showtimeRepository.save(showtime);
    }

    // Get all showtimes
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    // Get a showtime by id
    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    // Update an existing showtime
    public Showtime updateShowtime(Long id, Showtime updated) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Showtime with ID " + id + " not found."));

        showtime.setTheater(updated.getTheater());
        showtime.setStartTime(updated.getStartTime());
        showtime.setEndTime(updated.getEndTime());
        showtime.setPrice(updated.getPrice());
        showtime.setMovie(updated.getMovie());

        return showtimeRepository.save(showtime);
    }

    // Delete a showtime by id
    public boolean deleteShowtime(Long id) {
        if (showtimeRepository.existsById(id)) {
            showtimeRepository.deleteById(id);
            return true;
        }
        return false;
    }    
}
