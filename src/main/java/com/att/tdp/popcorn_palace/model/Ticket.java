package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"showtime_id", "seat_number"})
})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(nullable = false, length = 255)
    private String userId;

    @Column(nullable = false)
    private int seatNumber;

    // Constructors
    public Ticket() {}

    public Ticket(Showtime showtime, String userId, int seatNumber) {
        this.showtime = showtime;
        this.userId = userId;
        this.seatNumber = seatNumber;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }

    public String getUserId() { return userId; }
    public void setUserId(String customerName) { this.userId = customerName; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}
