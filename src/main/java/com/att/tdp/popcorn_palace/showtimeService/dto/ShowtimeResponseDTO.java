package com.att.tdp.popcorn_palace.showtimeService.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDateTime;

@JsonPropertyOrder({ "id", "price", "movieId", "theater", "startTime", "endTime" })
public class ShowtimeResponseDTO {
    private Long id;
    private double price;
    private Long movieId;
    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ShowtimeResponseDTO() {}

    public ShowtimeResponseDTO(Long id, double price, Long movieId, String theater, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.price = price;
        this.movieId = movieId;
        this.theater = theater;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getTheater() { return theater; }
    public void setTheater(String theater) { this.theater = theater; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
