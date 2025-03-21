package com.att.tdp.popcorn_palace.dto;

public class TicketRequest {
    
    private Long showtimeId;
    private String userId;
    private int seatNumber;

    public TicketRequest() {}

    public TicketRequest(Long showtimeId, int seatNumber, String userId) {
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
        this.userId = userId;
    }

    public Long getShowtimeId() { return showtimeId; }
    public void setShowtimeId(Long showtimeId) { this.showtimeId = showtimeId; }
    public String getUserId() { return userId; }
    public void setUserId(String customerName) { this.userId = customerName; }
    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}
