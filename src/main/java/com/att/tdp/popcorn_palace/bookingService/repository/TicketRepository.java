package com.att.tdp.popcorn_palace.bookingService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.att.tdp.popcorn_palace.bookingService.model.Ticket;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByShowtimeIdAndSeatNumber(Long showtimeId, int seatNumber);
    List<Ticket> findByShowtimeId(Long showtimeId);
    
}
