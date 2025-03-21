// package com.att.tdp.popcorn_palace;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.Optional;

// import com.att.tdp.popcorn_palace.model.*;
// import com.att.tdp.popcorn_palace.repository.*;
// import com.att.tdp.popcorn_palace.service.*;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;

// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// @ExtendWith(MockitoExtension.class)
// public class PopcornPalaceApplicationTests {

//     @Mock private MovieRepository movieRepository;
//     @Mock private ShowtimeRepository showtimeRepository;
//     @Mock private TicketRepository ticketRepository;

//     @InjectMocks private MovieService movieService;
//     @InjectMocks private ShowtimeService showtimeService;
//     @InjectMocks private TicketService ticketService;

//     private Movie movie;
//     private Showtime showtime;

//     @BeforeEach
//     void setUp() {
//         movie = new Movie("Inception", "Sci-Fi", 148, 8.8, 2010);
//         showtime = new Showtime(movie, "CinemaX", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20.0);
//     }

//     // ====== MOVIE TESTS ======

//     @Test
//     void testAddMovie_Success() {
//         when(movieRepository.save(any(Movie.class))).thenReturn(movie);
//         Movie result = movieService.addMovie(movie);
//         assertNotNull(result);
//         assertEquals("Inception", result.getTitle());
//     }

//     @Test
//     void testGetMovieById_Found() {
//         when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
//         Optional<Movie> result = movieService.getMovieById(1L);
//         assertTrue(result.isPresent());
//         assertEquals("Inception", result.get().getTitle());
//     }

//     @Test
//     void testGetAllMovies() {
//         when(movieRepository.findAll()).thenReturn(List.of(movie));
//         List<Movie> result = movieService.getAllMovies();
//         assertEquals(1, result.size());
//     }

//     // ====== SHOWTIME TESTS ======

//     @Test
//     void testAddShowtime_Success() {
//         when(showtimeRepository.findByTheaterAndStartTimeBetween(
//             anyString(), any(LocalDateTime.class), any(LocalDateTime.class)
//         )).thenReturn(List.of()); // No overlapping showtimes

//         when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);

//         Showtime result = showtimeService.addShowtime(showtime);
//         assertNotNull(result);
//     }

//     @Test
//     void testGetShowtimeById() {
//         when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
//         Optional<Showtime> result = showtimeService.getShowtimeById(1L);
//         assertTrue(result.isPresent());
//     }

//     // ====== TICKET TESTS ======

//     @Test
//     void testBookTicket_Success() {
//         when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
//         when(ticketRepository.findByShowtimeIdAndSeatNumber(1L, 5)).thenReturn(Optional.empty());
//         when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

//         Ticket result = ticketService.bookTicket(1L, "user-123", 5);

//         assertNotNull(result);
//         assertEquals("user-123", result.getUserId());
//         assertEquals(5, result.getSeatNumber());
//     }

//     @Test
//     void testBookTicket_SeatAlreadyBooked() {
//         when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
//         when(ticketRepository.findByShowtimeIdAndSeatNumber(1L, 5))
//             .thenReturn(Optional.of(new Ticket()));

//         RuntimeException ex = assertThrows(RuntimeException.class, () -> {
//             ticketService.bookTicket(1L, "user-123", 5);
//         });

//         assertEquals("Seat 5 is already booked for this showtime.", ex.getMessage());
//     }

//     @Test
//     void testCancelTicket_Success() {
//         when(ticketRepository.existsById(1L)).thenReturn(true);
//         boolean result = ticketService.cancelTicket(1L);
//         assertTrue(result);
//         verify(ticketRepository).deleteById(1L);
//     }

//     @Test
//     void testCancelTicket_NotFound() {
//         when(ticketRepository.existsById(99L)).thenReturn(false);
//         boolean result = ticketService.cancelTicket(99L);
//         assertFalse(result);
//         verify(ticketRepository, never()).deleteById(anyLong());
//     }
// }

package com.att.tdp.popcorn_palace;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.att.tdp.popcorn_palace.model.*;
import com.att.tdp.popcorn_palace.repository.*;
import com.att.tdp.popcorn_palace.service.*;

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
        showtime = new Showtime(movie, "CinemaX", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 20.0);
    }

    // ====== MOVIE TESTS ======

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

    // ====== SHOWTIME TESTS ======

    @Test
    void testAddShowtime_Success() {
        when(showtimeRepository.findByTheaterAndStartTimeBetween(
            anyString(), any(LocalDateTime.class), any(LocalDateTime.class)
        )).thenReturn(List.of());
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);

        Showtime result = showtimeService.addShowtime(showtime);
        assertNotNull(result);
    }

    @Test
    void testGetShowtimeById() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        Optional<Showtime> result = showtimeService.getShowtimeById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testShowtimeWithInvalidTimes() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Showtime(movie, "Neo Cinema", LocalDateTime.now().plusHours(2), LocalDateTime.now(), 10.0);
        });
    }

    @Test
    void testOverlappingShowtimesShouldFail() {
        when(showtimeRepository.findByTheaterAndStartTimeBetween(any(), any(), any()))
            .thenReturn(List.of(showtime));

        Showtime overlapping = new Showtime(movie, "CinemaX", showtime.getStartTime().plusMinutes(10),
            showtime.getEndTime().plusMinutes(10), 20.0);

        Exception exception = assertThrows(RuntimeException.class, () -> showtimeService.addShowtime(overlapping));
        assertTrue(exception.getMessage().contains("Overlapping showtime"));
    }

    // ====== TICKET TESTS ======

    @Test
    void testBookTicket_Success() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeIdAndSeatNumber(1L, 5)).thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = ticketService.bookTicket(1L, "user-123", 5);

        assertNotNull(result);
        assertEquals("user-123", result.getUserId());
        assertEquals(5, result.getSeatNumber());
    }

    @Test
    void testBookTicket_SeatAlreadyBooked() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeIdAndSeatNumber(1L, 5))
            .thenReturn(Optional.of(new Ticket()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ticketService.bookTicket(1L, "user-123", 5);
        });

        assertEquals("Seat 5 is already booked for this showtime.", ex.getMessage());
    }

    @Test
    void testDoubleBookingShouldFail() {
        Ticket ticket = new Ticket(showtime, "user123", 10);
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(ticketRepository.findByShowtimeIdAndSeatNumber(1L, 10)).thenReturn(Optional.of(ticket));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.bookTicket(1L, "user123", 10);
        });

        assertTrue(exception.getMessage().contains("already booked"));
    }

    @Test
    void testBookingWithInvalidSeatNumber() {
        lenient().when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.bookTicket(1L, "userX", -1);
        });
        assertTrue(exception.getMessage().contains("must be a positive integer"));
    }

    @Test
    void testCancelTicket_Success() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        boolean result = ticketService.cancelTicket(1L);
        assertTrue(result);
        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void testCancelTicket_NotFound() {
        when(ticketRepository.existsById(99L)).thenReturn(false);
        boolean result = ticketService.cancelTicket(99L);
        assertFalse(result);
        verify(ticketRepository, never()).deleteById(anyLong());
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
