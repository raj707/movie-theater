package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.jpmc.theater.Showing.START_TIME_DISCOUNT_LOWER_HOUR;
import static com.jpmc.theater.Showing.START_TIME_DISCOUNT_UPPER_HOUR;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationTests {

    @Test
    void testGetTotalFeeWithSpecialMovieCodeDiscountOf20percent() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                3,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(30.0, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithSequence1DiscountOf3() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                1,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(28.5, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithSequence2DiscountOf2() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
                2,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(31.5, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithDay7DiscountOf1() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDate nextDay7FromToday = Utility.nextDateForDay(LocalDate.now(), 7);
        LocalDateTime showStartTime = LocalDateTime.of(nextDay7FromToday, LocalTime.of(10, 0));
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 0),
                3,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(34.5, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithStartTimeHourLowerBoundDiscountOf25percent() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(11);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                2,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertTrue(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(28.14, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithStartTimeHourUpperBoundDiscountOf25percent() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(16);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                2,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertTrue(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(28.14, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithStartTimeHourWithinRangeDiscountOf25percent() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(15);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1),
                2,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertTrue(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(28.14, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithNoDiscount() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 15, 0),
                3,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(45.0, reservation.getTotalFee());
    }

    @Test
    void testGetTotalFeeWithSequence1DiscountOf3AndTicketPriceLessThanDiscount() {
        Customer customer = new Customer("John Doe", "unused-id");
        LocalDateTime showStartTime = LocalDateTime.now().withHour(10);
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 2.0, 1),
                1,
                showStartTime
        );
        Reservation reservation = new Reservation(customer, LocalDate.now(), showing, 3);
        assertFalse(Utility.checkIfHourFallsWithinRange(showStartTime.getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(0.0, reservation.getTotalFee());
    }

}
