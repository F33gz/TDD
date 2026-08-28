package com.restaurant.reservations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Reservation {

    private final String code;
    private final String customerName;
    private final int partySize;
    private final LocalDate date;
    private final LocalTime time;

    Reservation(String customerName, int partySize, LocalDate date, LocalTime time) {
        this.code = UUID.randomUUID().toString();
        this.customerName = customerName;
        this.partySize = partySize;
        this.date = date;
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getPartySize() {
        return partySize;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }
}
