package com.att.tdp.popcorn_palace.showtimeService.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.att.tdp.popcorn_palace.movieService.model.Movie;

@Entity
@Table(name = "showtimes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"theater", "start_time"})
})
public class Showtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false, foreignKey = @ForeignKey(name = "fk_showtime_movie"))
    private Movie movie;

    @Column(nullable = false, length = 255)
    private String theater;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private double price;

    // Constructors
    public Showtime() {}

    public Showtime(Movie movie, String theater, LocalDateTime startTime, LocalDateTime endTime, double price) {
        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }        
        this.movie = movie;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public String getTheater() { return theater; }
    public void setTheater(String theater) { this.theater = theater; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) {
        if (endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) {
        if (startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        this.endTime = endTime;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }    
}
