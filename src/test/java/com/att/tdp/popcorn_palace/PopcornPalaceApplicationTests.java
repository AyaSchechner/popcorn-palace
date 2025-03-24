package com.att.tdp.popcorn_palace;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.att.tdp.popcorn_palace.bookingService.model.Ticket;
import com.att.tdp.popcorn_palace.bookingService.repository.TicketRepository;
import com.att.tdp.popcorn_palace.bookingService.service.TicketService;
import com.att.tdp.popcorn_palace.movieService.model.Movie;
import com.att.tdp.popcorn_palace.movieService.repository.MovieRepository;
import com.att.tdp.popcorn_palace.movieService.service.MovieService;
import com.att.tdp.popcorn_palace.showtimeService.dto.ShowtimeRequestDTO;
import com.att.tdp.popcorn_palace.showtimeService.dto.ShowtimeResponseDTO;
import com.att.tdp.popcorn_palace.showtimeService.model.Showtime;
import com.att.tdp.popcorn_palace.showtimeService.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.showtimeService.service.ShowtimeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PopcornPalaceApplicationTests {

    @Mock private MovieRepository movieRepository;
    @Mock private ShowtimeRepository showtimeRepository;
    @Mock private TicketRepository ticketRepository;

    @InjectMocks private MovieService movieService;
    @InjectMocks private ShowtimeService showtimeService;
    @InjectMocks private TicketService ticketService;

    private Movie movie;
    private Showtime showtime;

    @BeforeEach
    void setUp() {
        movie = new Movie("Inception", "Sci-Fi", 148, 8.8, 2010);
        movie.setId(1L);
        showtime = new Showtime(movie, "CinemaX", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20.0);
        showtime.setId(1L);
    }

    // ===== MOVIE TESTS =====

    @Test
    void testAddMovie_Success() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie result = movieService.addMovie(movie);
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void testGetMovieById_Found() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Optional<Movie> result = movieService.getMovieById(1L);
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
    }

    @Test
    void testGetAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(movie));
        List<Movie> result = movieService.getAllMovies();
        assertEquals(1, result.size());
    }

    @Test
    void testMovieWithInvalidDuration() {
        movie.setDuration(0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> movieService.addMovie(movie));
        assertTrue(exception.getMessage().contains("Duration must be greater than 0"));
    }

    @Test
    void testMovieWithOutOfRangeRating() {
        movie.setRating(11);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> movieService.addMovie(movie));
        assertTrue(exception.getMessage().contains("Rating must be between 0 and 10"));
    }

    @Test
    void testMovieWithEmptyTitle() {
        movie.setTitle("");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> movieService.addMovie(movie));
        assertTrue(exception.getMessage().contains("Title cannot be empty"));
    }

    // ===== SHOWTIME TESTS =====

    @Test
    void testAddShowtime_Success() {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(1L, "CinemaX", showtime.getStartTime(), showtime.getEndTime(), 20.0);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepository.findByTheaterAndStartTimeBetween(any(), any(), any())).thenReturn(List.of());
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);
    
        ShowtimeResponseDTO result = showtimeService.addShowtime(dto);
    
        assertNotNull(result);
        assertEquals(showtime.getId(), result.getId());
        assertEquals(showtime.getMovie().getId(), result.getMovieId());
        assertEquals(showtime.getTheater(), result.getTheater());
        assertEquals(showtime.getStartTime(), result.getStartTime());
        assertEquals(showtime.getEndTime(), result.getEndTime());
        assertEquals(showtime.getPrice(), result.getPrice());
    }
    
    @Test
    void testGetShowtimeById() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        Optional<ShowtimeResponseDTO> result = showtimeService.getShowtimeById(1L);
        assertTrue(result.isPresent());
        assertEquals(showtime.getId(), result.get().getId());
        assertEquals(showtime.getTheater(), result.get().getTheater());
        assertEquals(showtime.getPrice(), result.get().getPrice());
        assertEquals(showtime.getStartTime(), result.get().getStartTime());
        assertEquals(showtime.getEndTime(), result.get().getEndTime());
        assertEquals(showtime.getMovie().getId(), result.get().getMovieId());
    }

    @Test
    void testShowtimeWithInvalidTimes() {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(1L, "CinemaX", LocalDateTime.now().plusHours(2), LocalDateTime.now(), 20.0);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> showtimeService.addShowtime(dto));
        assertTrue(ex.getMessage().contains("Start time must be before end time"));
    }

    @Test
    void testOverlappingShowtimesShouldFail() {
        ShowtimeRequestDTO dto = new ShowtimeRequestDTO(1L, "CinemaX", showtime.getStartTime().plusMinutes(10), showtime.getEndTime().plusMinutes(10), 20.0);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepository.findByTheaterAndStartTimeBetween(any(), any(), any())).thenReturn(List.of(showtime));
        Exception ex = assertThrows(IllegalArgumentException.class, () -> showtimeService.addShowtime(dto));
        assertTrue(ex.getMessage().contains("Overlapping showtime"));
    }

    // ===== TICKET TESTS =====

    @Test
    void testBookTicket_Success() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeIdAndSeatNumber(1L, 5)).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> inv.getArgument(0));
        Ticket result = ticketService.bookTicket(1L, "user-123", 5);
        assertNotNull(result);
        assertEquals("user-123", result.getUserId());
        assertEquals(5, result.getSeatNumber());
    }

    @Test
    void testBookTicket_SeatAlreadyBooked() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeIdAndSeatNumber(1L, 5)).thenReturn(Optional.of(new Ticket()));
        Exception ex = assertThrows(IllegalArgumentException.class, () -> ticketService.bookTicket(1L, "user-123", 5));
        assertEquals("Seat 5 is already booked for this showtime.", ex.getMessage());
    }

    @Test
    void testBookingWithInvalidSeatNumber() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> ticketService.bookTicket(1L, "userX", -1));
        assertTrue(ex.getMessage().contains("Seat number must be a positive integer"));
    }

    @Test
    void testBookingWithNullUserId() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> ticketService.bookTicket(1L, null, 5));
        assertTrue(ex.getMessage().contains("Showtime ID and user ID must not be null or empty"));
    }

    @Test
    void testBookingWithNonexistentShowtime() {
        when(showtimeRepository.findById(99L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> ticketService.bookTicket(99L, "userX", 5));
        assertTrue(ex.getMessage().contains("Showtime with ID 99 not found"));
    }

    @Test
    void testValidBookingFlow() {
        when(showtimeRepository.findById(2L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeIdAndSeatNumber(2L, 5)).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);
        Ticket ticket = ticketService.bookTicket(2L, "user456", 5);
        assertNotNull(ticket);
        assertEquals(5, ticket.getSeatNumber());
        assertEquals("user456", ticket.getUserId());
        assertEquals(showtime, ticket.getShowtime());
    }
}
