package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByShowtimeIdAndSeatNumber(Long showtimeId, int seatNumber);
    List<Ticket> findByShowtimeId(Long showtimeId);
    
}
