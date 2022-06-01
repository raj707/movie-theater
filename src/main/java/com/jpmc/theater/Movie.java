package com.jpmc.theater;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Duration;
import java.util.Objects;

public class Movie {

    private String title;
    @JsonSerialize(using = CustomDurationSerializer.class)
    private Duration runningTime;
    private double ticketPrice;
    //special code is optional; if not provided will default it to zero (missing)
    private int specialCode;

    public Movie(String title, Duration runningTime, double ticketPrice) {
        validate(title, runningTime, ticketPrice);
        this.title = title;
        this.runningTime = runningTime;
        this.ticketPrice = ticketPrice;
    }

    public Movie(String title, Duration runningTime, double ticketPrice, int specialCode) {
        validate(title, runningTime, ticketPrice);
        this.title = title;
        this.runningTime = runningTime;
        this.ticketPrice = ticketPrice;
        this.specialCode = specialCode;
    }

    private void validate(String title, Duration runningTime, double ticketPrice) {
        //assuming ticket price cannot be negative but it can be zero (case where its a promotional movie and is free to watch)
        //note: you can throw individual messages instead of a generic error message like 'Movie cannot be created';
        //I am doing this way to save some time.
        if(title == null || title.trim().isEmpty() || runningTime == null || ticketPrice < 0) {
            throw new IllegalStateException("Movie cannot be created");
        }
    }

    public String getTitle() {
        return title;
    }

    public Duration getRunningTime() {
        return runningTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public int getSpecialCode() {
        return specialCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Double.compare(movie.ticketPrice, ticketPrice) == 0
                && Objects.equals(title, movie.title)
                && Objects.equals(runningTime, movie.runningTime)
                && Objects.equals(specialCode, movie.specialCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, runningTime, ticketPrice, specialCode);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", runningTime=" + runningTime +
                ", ticketPrice=" + ticketPrice +
                ", specialCode=" + specialCode +
                '}';
    }
}
