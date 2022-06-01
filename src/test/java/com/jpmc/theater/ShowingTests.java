package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.jpmc.theater.Showing.START_TIME_DISCOUNT_LOWER_HOUR;
import static com.jpmc.theater.Showing.START_TIME_DISCOUNT_UPPER_HOUR;
import static com.jpmc.theater.Theater.SEATING_CAPACITY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShowingTests {

    @Test
    void testCreateShowingWithSpecialMovieCodeDiscountOf20percent() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                3,
                showStartTime
        );
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(10.0, showing.getDiscountedMovieFee());
        assertEquals(3, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithSequence1DiscountOf3() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                1,
                showStartTime
        );
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(9.5, showing.getDiscountedMovieFee());
        assertEquals(1, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithSequence2DiscountOf2() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
                2,
                showStartTime
        );
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(10.5, showing.getDiscountedMovieFee());
        assertEquals(2, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithDay7DiscountOf1() {
        LocalDate nextDay7FromToday = Utility.nextDateForDay(LocalDate.now(), 7);
        LocalDateTime showStartTime = LocalDateTime.of(nextDay7FromToday, LocalTime.of(10, 0));
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
                3,
                showStartTime
        );
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(11.5, showing.getDiscountedMovieFee());
        assertEquals(3, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithStartTimeHourLowerBoundDiscountOf25percent() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(11);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                2,
                showStartTime
        );
        assertTrue(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(9.38, showing.getDiscountedMovieFee());
        assertEquals(2, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithStartTimeHourUpperBoundDiscountOf25percent() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(16);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                2,
                showStartTime
        );
        assertTrue(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(9.38, showing.getDiscountedMovieFee());
        assertEquals(2, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithStartTimeHourWithinRangeDiscountOf25percent() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(15);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                2,
                showStartTime
        );
        assertTrue(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(9.38, showing.getDiscountedMovieFee());
        assertEquals(2, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithNoDiscount() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
                3,
                showStartTime
        );
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(12.5, showing.getDiscountedMovieFee());
        assertEquals(3, showing.getSequenceOfTheDay());
    }

    @Test
    void testCreateShowingWithSequence1DiscountOf3AndTicketPriceLessThanDiscount() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 2.0, 1),
                1,
                showStartTime
        );
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(2.0, showing.getMovieFee());
        assertEquals(0.0, showing.getDiscountedMovieFee());
        assertEquals(1, showing.getSequenceOfTheDay());
    }

    @Test
    void testReduceSeatsLeftForShowingWithSpecialMovieCodeDiscountOf20percent() {
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                3,
                showStartTime
        );
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(showStartTime, showing.getStartTime());
        assertEquals(12.5, showing.getMovieFee());
        assertEquals(10.0, showing.getDiscountedMovieFee());
        assertEquals(3, showing.getSequenceOfTheDay());
        assertEquals(SEATING_CAPACITY, showing.getSeatsLeft());

        assertThrows(IllegalStateException.class, () -> {
            //should throw error if trying to remove more seats than left
            showing.reduceSeatsLeft(SEATING_CAPACITY + 1);
        });

        showing.reduceSeatsLeft(60);
        assertEquals(SEATING_CAPACITY - 60, showing.getSeatsLeft());

        assertThrows(IllegalStateException.class, () -> {
            //should throw error if trying to remove more seats than left
            showing.reduceSeatsLeft(SEATING_CAPACITY - 60 + 1);
        });

        //remove all seats which are left
        showing.reduceSeatsLeft(SEATING_CAPACITY - 60);
        assertEquals(0, showing.getSeatsLeft());
    }

}
