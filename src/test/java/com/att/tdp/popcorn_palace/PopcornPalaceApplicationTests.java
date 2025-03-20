// package com.att.tdp.popcorn_palace;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// class PopcornPalaceApplicationTests {

// 	@Test
// 	void contextLoads() {
// 	}

// }

package com.att.tdp.popcorn_palace;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.att.tdp.popcorn_palace.model.*;
import com.att.tdp.popcorn_palace.repository.*;
import com.att.tdp.popcorn_palace.service.*;
import com.att.tdp.popcorn_palace.controller.*;

@ExtendWith(MockitoExtension.class)
public class PopcornPalaceApplicationTests {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private MovieService movieService;

    @InjectMocks
    private ShowtimeService showtimeService;

    @InjectMocks
    private TicketService ticketService;

    @InjectMocks
    private MovieController movieController;

    @InjectMocks
    private ShowtimeController showtimeController;

    @InjectMocks
    private TicketController ticketController;

    private Movie movie;
    private Showtime showtime;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        movie = new Movie("Inception", "Sci-Fi", 148, 8.8, 2010);
        showtime = new Showtime(movie, "CinemaX Hall 2", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 15.0);
        ticket = new Ticket(showtime, "John Doe", 5);
    }

    @Test
    void testCreateMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        Movie savedMovie = movieService.addMovie(movie);
        assertNotNull(savedMovie);
        assertEquals("Inception", savedMovie.getTitle());
    }

    @Test
    void testFindMovieById() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        Optional<Movie> foundMovie = movieService.getMovieById(1L);
        assertTrue(foundMovie.isPresent());
        assertEquals("Inception", foundMovie.get().getTitle());
    }

    @Test
    void testGetAllMovies() {
        List<Movie> movies = Arrays.asList(movie);
        when(movieRepository.findAll()).thenReturn(movies);
        List<Movie> retrievedMovies = movieService.getAllMovies();
        assertEquals(1, retrievedMovies.size());
    }

    @Test
    void testCreateShowtime() {
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);
        Showtime savedShowtime = showtimeService.addShowtime(showtime);
        assertNotNull(savedShowtime);
    }

    @Test
    void testGetShowtimeById() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        Optional<Showtime> retrievedShowtime = showtimeService.getShowtimeById(1L);
        assertTrue(retrievedShowtime.isPresent());
    }

    @Test
	void testBookTicket() {
		// Given: A valid showtime exists
		Showtime showtime = new Showtime(movie, "CinemaX Hall 2", LocalDateTime.now(), LocalDateTime.now().plusHours(2), 15.0);
		
		when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));  // ✅ Mock showtime lookup
		when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket(showtime, "John Doe", 5));  // ✅ Mock save

		// When: Booking a ticket
		Ticket ticket = ticketService.bookTicket(1L, "John Doe", 5);

		// Then: Verify the result
		assertNotNull(ticket);
		assertEquals("John Doe", ticket.getCustomerName());
		verify(ticketRepository).save(any(Ticket.class));  // ✅ Ensure save was executed
	}


	@Test
	void testDeleteMovie() {
		// Given: The movie exists in the database
		when(movieRepository.existsById(1L)).thenReturn(true);  // ✅ Movie exists
		doNothing().when(movieRepository).deleteById(1L);  // ✅ Mock delete action

		// When: Calling deleteMovie()
		movieService.deleteMovie(1L);

		// Then: Verify that deleteById was called
		verify(movieRepository).deleteById(1L);  // ✅ Ensure delete was executed
	}

}
