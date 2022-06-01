package com.jpmc.theater;

import java.time.LocalDate;

public class Reservation {
    private Customer customer;
    //showDate is needed as customer should be able to book show for current as well as future dates
    private LocalDate showDate;
    private Showing showing;
    private int audienceCount;

    public Reservation(Customer customer, LocalDate showDate, Showing showing, int audienceCount) {
        validate(customer, showDate, showing, audienceCount);
        this.customer = customer;
        this.showDate = showDate;
        this.showing = showing;
        this.audienceCount = audienceCount;
    }

    private void validate(Customer customer, LocalDate showDate, Showing showing, int audienceCount) {
        //note: you can throw individual messages instead of a generic error message like 'Reservation cannot be created';
        //I am doing this way to save some time.
        if(customer == null || showDate == null || showing == null || audienceCount < 1) {
            throw new IllegalStateException("Reservation cannot be created");
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public Showing getShowing() {
        return showing;
    }

    public int getAudienceCount() {
        return audienceCount;
    }

    public double getTotalFee() {
        return Utility.roundOff(showing.getDiscountedMovieFee() * audienceCount);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", showDate=" + showDate +
                ", showing=" + showing +
                ", audienceCount=" + audienceCount +
                ", totalFee=" + getTotalFee() +
                '}';
    }
}
