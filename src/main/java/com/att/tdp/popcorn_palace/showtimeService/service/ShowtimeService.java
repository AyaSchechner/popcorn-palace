package com.att.tdp.popcorn_palace.showtimeService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.showtimeService.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.movieService.model.Movie;
import com.att.tdp.popcorn_palace.movieService.repository.MovieRepository;
import com.att.tdp.popcorn_palace.showtimeService.dto.ShowtimeRequestDTO;
import com.att.tdp.popcorn_palace.showtimeService.dto.ShowtimeResponseDTO;
import com.att.tdp.popcorn_palace.showtimeService.model.Showtime;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    // Add a new showtime (Prevents overlapping showtimes)
    public ShowtimeResponseDTO addShowtime(ShowtimeRequestDTO dto) {
        validateDto(dto);
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie ID " + dto.getMovieId() + " not found."));
        List<Showtime> overlapping = showtimeRepository.findByTheaterAndStartTimeBetween(
                dto.getTheater(), dto.getStartTime(), dto.getEndTime());

        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Overlapping showtime exists in the theater.");
        }

        Showtime showtime = new Showtime(movie, dto.getTheater(), dto.getStartTime(), dto.getEndTime(), dto.getPrice());
        Showtime saved = showtimeRepository.save(showtime);
        return mapToResponseDTO(saved);
    }
    
    // Get all showtimes (for debugging purposes)
    public List<ShowtimeResponseDTO> getAllShowtimes() {
        return showtimeRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }
    
    // Get a showtime by id
    public Optional<ShowtimeResponseDTO> getShowtimeById(Long id) {
        return showtimeRepository.findById(id).map(this::mapToResponseDTO);
    }

    // Update an existing showtime
    public Optional<Showtime> updateShowtime(Long id, ShowtimeRequestDTO dto) {
        Optional<Showtime> existing = showtimeRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();

        validateDto(dto);

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Movie ID " + dto.getMovieId() + " not found."));

        Showtime showtime = existing.get();
        showtime.setMovie(movie);
        showtime.setTheater(dto.getTheater());
        showtime.setStartTime(dto.getStartTime());
        showtime.setEndTime(dto.getEndTime());
        showtime.setPrice(dto.getPrice());

        return Optional.of(showtimeRepository.save(showtime));
    }

    // Delete a showtime by id
    public boolean deleteShowtime(Long id) {
        if (showtimeRepository.existsById(id)) {
            showtimeRepository.deleteById(id);
            return true;
        }
        return false;
    }    

    // Validate the showtime DTO
    private void validateDto(ShowtimeRequestDTO dto) {
        if (dto.getMovieId() == null) throw new IllegalArgumentException("Movie ID is required.");
        if (dto.getTheater() == null || dto.getTheater().isBlank())
            throw new IllegalArgumentException("Theater must not be empty.");
        if (dto.getStartTime() == null || dto.getEndTime() == null)
            throw new IllegalArgumentException("Start and end time must be provided.");
        if (!dto.getStartTime().isBefore(dto.getEndTime()))
            throw new IllegalArgumentException("Start time must be before end time.");
        if (dto.getPrice() < 0) throw new IllegalArgumentException("Price must be non-negative.");
    }

    private ShowtimeResponseDTO mapToResponseDTO(Showtime showtime) {
        return new ShowtimeResponseDTO(
            showtime.getId(),
            showtime.getPrice(),
            showtime.getMovie() != null ? showtime.getMovie().getId() : null,
            showtime.getTheater(),
            showtime.getStartTime(),
            showtime.getEndTime()
        );
    }
    
}
