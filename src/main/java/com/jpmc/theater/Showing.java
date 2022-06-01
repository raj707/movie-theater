package com.jpmc.theater;

import java.time.LocalDateTime;

import static com.jpmc.theater.Theater.SEATING_CAPACITY;

public class Showing {

    private static final int MOVIE_CODE_SPECIAL = 1;
    public static final int START_TIME_DISCOUNT_LOWER_HOUR = 11;
    public static final int START_TIME_DISCOUNT_UPPER_HOUR = 16;

    private Movie movie;
    private int sequenceOfTheDay;
    private LocalDateTime showStartTime;
    private double discountedMovieFee;
    private int seatsLeft;

    public Showing(Movie movie, int sequenceOfTheDay, LocalDateTime showStartTime) {
        validate(movie, sequenceOfTheDay, showStartTime);
        this.movie = movie;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.showStartTime = showStartTime;
        this.discountedMovieFee = calculateFee();
        this.seatsLeft = SEATING_CAPACITY;
    }

    private void validate(Movie movie, int sequenceOfTheDay, LocalDateTime showStartTime) {
        //note: you can throw individual messages instead of a generic error message like 'Showing cannot be created';
        //I am doing this way to save some time.
        if(movie == null || sequenceOfTheDay < 1 || showStartTime == null) {
            throw new IllegalStateException("Showing cannot be created");
        }
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalDateTime getStartTime() {
        return showStartTime;
    }

    public double getMovieFee() {
        return movie.getTicketPrice();
    }

    public int getSequenceOfTheDay() {
        return sequenceOfTheDay;
    }

    public double getDiscountedMovieFee() {
        return discountedMovieFee;
    }

    public int getSeatsLeft() {
        return seatsLeft;
    }

    /**
     * Method to reduce the seats from the showing depending on how many tickets were bought.
     * @param noOfTicketsToBuy
     */
    public void reduceSeatsLeft(int noOfTicketsToBuy) {
        if(seatsLeft < noOfTicketsToBuy) {
            throw new IllegalStateException("not enough tickets left for the showing");
        }
        seatsLeft = seatsLeft - noOfTicketsToBuy;
    }

    private double calculateFee() {
        double discount = getDiscount();
        double ticketPrice = movie.getTicketPrice();
        if(discount > ticketPrice) {
            //assuming we don't want to give any money back to the user.
            return 0.0;
        }
        return Utility.roundOff(movie.getTicketPrice() - getDiscount());
    }

    /**
     * Method to find the discount if applicable.
     * In case a showing is eligible for multiple discounts, will return the one which is maximum.
     * @return
     */
    private double getDiscount() {
        //discount# 1
        double specialDiscount = 0;
        if (MOVIE_CODE_SPECIAL == movie.getSpecialCode()) {
            specialDiscount = movie.getTicketPrice() * 0.2;  // 20% discount for special movie
        }

        //discount# 2
        double sequenceDiscount = 0;
        if (sequenceOfTheDay == 1) {
            sequenceDiscount = 3; // $3 discount for 1st show
        } else if (sequenceOfTheDay == 2) {
            sequenceDiscount = 2; // $2 discount for 2nd show
        }

        //discount# 3
        double showStartTimeDiscount = 0;
        //check start time to be between 11 am to 4 pm
        if(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR)) {
            showStartTimeDiscount = movie.getTicketPrice() * 0.25;
        }

        //discount# 4
        double showingOn7thDiscount = 0;
        if(showStartTime.getDayOfMonth() == 7) {
            showingOn7thDiscount = 1; // $1 discount for showing on day 7th
        }

        // biggest discount wins
        double biggestDiscount = Math.max(specialDiscount, Math.max(sequenceDiscount, Math.max(showStartTimeDiscount, showingOn7thDiscount)));
        return Utility.roundOff(biggestDiscount);
    }

    @Override
    public String toString() {
        return "Showing{" +
                "movie=" + movie +
                ", sequenceOfTheDay=" + sequenceOfTheDay +
                ", showStartTime=" + showStartTime +
                ", discountedMovieFee=" + discountedMovieFee +
                ", seatsLeft=" + seatsLeft +
                '}';
    }
}
