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
    private boolean cancelled;

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

    public boolean isCancelled() {
        return cancelled;
    }

    boolean isActiveAt(LocalDate date, LocalTime time) {
        return !cancelled && this.date.equals(date) && this.time.equals(time);
    }

    void cancel() {
        if (cancelled) {
            throw new IllegalStateException();
        }
        cancelled = true;
    }
}
