package com.jpmc.theater;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jpmc.theater.Showing.START_TIME_DISCOUNT_LOWER_HOUR;
import static com.jpmc.theater.Showing.START_TIME_DISCOUNT_UPPER_HOUR;
import static com.jpmc.theater.Theater.SEATING_CAPACITY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheaterTests {

    private Theater theater;

    @BeforeEach
    public void setup() {
        LocalDateProvider provider = LocalDateProvider.singleton();
        Map<LocalDate, List<Showing>> schedule = new HashMap<>();
        //todays showing
        LocalDate today = provider.currentDate();
        schedule.put(today, prepareShowingForDate(today));
        //tomorrow's showing
        LocalDate tomorrow = today.plusDays(1);
        schedule.put(tomorrow, prepareShowingForDate(tomorrow));
        //yesterday's showing
        LocalDate yesterday = today.minusDays(1);
        schedule.put(yesterday, prepareShowingForDate(yesterday));
        //showing for next Day# 7 from today
        LocalDate nextDay7FromToday = Utility.nextDateForDay(today, 7);
        schedule.put(nextDay7FromToday, prepareShowingForDate(nextDay7FromToday));

        theater = new Theater(LocalDateProvider.singleton(), schedule);
    }

    @Test
    void testReservationWithSpecialMovieCodeDiscountOf20percent() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, LocalDate.now(), 8, 3);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("Spider-Man: No Way Home", reservation.getShowing().getMovie().getTitle());
        assertEquals(8, reservation.getShowing().getSequenceOfTheDay());
        assertEquals(12.5, reservation.getShowing().getMovieFee());
        assertEquals(10.0, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 3, reservation.getShowing().getSeatsLeft());
        assertEquals(30.0, reservation.getTotalFee());
    }

    @Test
    void testReservationWithSequence1DiscountOf3() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, LocalDate.now(), 1, 2);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("Turning Red", reservation.getShowing().getMovie().getTitle());
        assertEquals(1, reservation.getShowing().getSequenceOfTheDay());
        assertEquals(11.0, reservation.getShowing().getMovieFee());
        assertEquals(8.0, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 2, reservation.getShowing().getSeatsLeft());
        assertEquals(16.0, reservation.getTotalFee());
    }

    @Test
    void testReservationWithSequence2DiscountOf2() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, LocalDate.now(), 2, 1);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("The Batman", reservation.getShowing().getMovie().getTitle());
        assertEquals(2, reservation.getShowing().getSequenceOfTheDay());
        assertEquals(9.0, reservation.getShowing().getMovieFee());
        assertEquals(7.0, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 1, reservation.getShowing().getSeatsLeft());
        assertEquals(7.0, reservation.getTotalFee());
    }

    @Test
    void testReservationWithDay7DiscountOf1() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, Utility.nextDateForDay(LocalDate.now(), 7), 7, 6);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("Turning Red", reservation.getShowing().getMovie().getTitle());
        assertEquals(7, reservation.getShowing().getSequenceOfTheDay());
        assertEquals(11.0, reservation.getShowing().getMovieFee());
        assertEquals(10.0, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 6, reservation.getShowing().getSeatsLeft());
        assertEquals(60.0, reservation.getTotalFee());
    }

    @Test
    void testReservationWithStartTimeHourWithinRangeDiscountOf25percent() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, LocalDate.now(), 4, 2);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("Turning Red", reservation.getShowing().getMovie().getTitle());
        assertEquals(4, reservation.getShowing().getSequenceOfTheDay());
        assertTrue(Utility.checkIfHourFallsWithinRange(reservation.getShowing().getStartTime().getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(11.0, reservation.getShowing().getMovieFee());
        assertEquals(8.25, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 2, reservation.getShowing().getSeatsLeft());
        assertEquals(16.5, reservation.getTotalFee());
    }

    @Test
    void testReservationWithNoDiscount() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, LocalDate.now(), 9, 2);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("The Batman", reservation.getShowing().getMovie().getTitle());
        assertEquals(9, reservation.getShowing().getSequenceOfTheDay());
        assertEquals(9.0, reservation.getShowing().getMovieFee());
        assertEquals(9.0, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 2, reservation.getShowing().getSeatsLeft());
        assertEquals(18.0, reservation.getTotalFee());
    }

    @Test
    void testReservationWithSpecialMovieCodeDiscountOf20percentForSeatsLeft() {
        LocalDate today = LocalDate.now();
        Customer customerRajesh = new Customer("Rajesh", "1");
        Reservation reservationForRajesh = theater.reserve(customerRajesh, today, 8, 3);
        assertEquals("Rajesh", reservationForRajesh.getCustomer().getName());
        assertEquals("Spider-Man: No Way Home", reservationForRajesh.getShowing().getMovie().getTitle());
        assertEquals(8, reservationForRajesh.getShowing().getSequenceOfTheDay());
        assertEquals(12.5, reservationForRajesh.getShowing().getMovieFee());
        assertEquals(10.0, reservationForRajesh.getShowing().getDiscountedMovieFee());
        //after this reservation only 97 seats are left for this showing
        assertEquals(SEATING_CAPACITY - 3, reservationForRajesh.getShowing().getSeatsLeft());
        assertEquals(30.0, reservationForRajesh.getTotalFee());

        Customer customerJohn = new Customer("John", "2");
        assertThrows(IllegalStateException.class, () -> {
            //booking tickets more than what is left
            theater.reserve(customerJohn, today, 8, 98);
        });
        Reservation reservationForJohn = theater.reserve(customerJohn, today, 8, 97);
        assertEquals("John", reservationForJohn.getCustomer().getName());
        assertEquals("Spider-Man: No Way Home", reservationForJohn.getShowing().getMovie().getTitle());
        assertEquals(8, reservationForJohn.getShowing().getSequenceOfTheDay());
        assertEquals(12.5, reservationForJohn.getShowing().getMovieFee());
        assertEquals(10.0, reservationForJohn.getShowing().getDiscountedMovieFee());
        assertEquals(0, reservationForJohn.getShowing().getSeatsLeft());
        assertEquals(970.0, reservationForJohn.getTotalFee());

        assertThrows(IllegalStateException.class, () -> {
            //no more booking allowed for today's showing because of house full
            theater.reserve(customerJohn, today, 8, 1);
        });

        //John can still book for tomorrow same showing but different showDate
        Reservation reservationForJohnForTomorrow = theater.reserve(customerJohn, today.plusDays(1), 8, 1);
        assertEquals("John", reservationForJohnForTomorrow.getCustomer().getName());
        assertEquals("Spider-Man: No Way Home", reservationForJohnForTomorrow.getShowing().getMovie().getTitle());
        assertEquals(8, reservationForJohnForTomorrow.getShowing().getSequenceOfTheDay());
        assertEquals(12.5, reservationForJohnForTomorrow.getShowing().getMovieFee());
        assertEquals(10.0, reservationForJohnForTomorrow.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 1, reservationForJohnForTomorrow.getShowing().getSeatsLeft());
        assertEquals(10.0, reservationForJohnForTomorrow.getTotalFee());
    }

    @Test
    void testReservationWithStartTimeHourWithinRangeDiscountOf25percentForFuture() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, LocalDate.now().plusDays(1), 4, 2);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("Turning Red", reservation.getShowing().getMovie().getTitle());
        assertEquals(4, reservation.getShowing().getSequenceOfTheDay());
        assertTrue(Utility.checkIfHourFallsWithinRange(reservation.getShowing().getStartTime().getHour(), START_TIME_DISCOUNT_LOWER_HOUR, START_TIME_DISCOUNT_UPPER_HOUR));
        assertEquals(11.0, reservation.getShowing().getMovieFee());
        assertEquals(8.25, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 2, reservation.getShowing().getSeatsLeft());
        assertEquals(16.5, reservation.getTotalFee());
    }

    @Test
    void testReservationWithNoDiscountForFuture() {
        Customer customer = new Customer("Rajesh", "1");
        Reservation reservation = theater.reserve(customer, LocalDate.now().plusDays(1), 9, 2);
        assertEquals("Rajesh", reservation.getCustomer().getName());
        assertEquals("The Batman", reservation.getShowing().getMovie().getTitle());
        assertEquals(9, reservation.getShowing().getSequenceOfTheDay());
        assertEquals(9.0, reservation.getShowing().getMovieFee());
        assertEquals(9.0, reservation.getShowing().getDiscountedMovieFee());
        assertEquals(SEATING_CAPACITY - 2, reservation.getShowing().getSeatsLeft());
        assertEquals(18.0, reservation.getTotalFee());
    }

    @Test
    void testReservationInThePast() {
        Customer customer = new Customer("Rajesh", "1");
        assertThrows(IllegalStateException.class, () -> {
            theater.reserve(customer, LocalDate.now().minusDays(1), 9, 2);
        });
    }

    @Test
    void testPrintScheduleInTextFormat() {
        theater.printSchedule(PrintSchedule.TEXT, LocalDate.now());
    }

    @Test
    void testPrintScheduleInJsonFormat() {
        theater.printSchedule(PrintSchedule.JSON, LocalDate.now());
    }

    private List<Showing> prepareShowingForDate(LocalDate showDate) {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), 11);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), 9);
        return List.of(
                new Showing(turningRed, 1, LocalDateTime.of(showDate, LocalTime.of(9, 0))),
                new Showing(theBatMan, 2, LocalDateTime.of(showDate, LocalTime.of(10, 45))),
                new Showing(spiderMan, 3, LocalDateTime.of(showDate, LocalTime.of(12, 50))),
                new Showing(turningRed, 4, LocalDateTime.of(showDate, LocalTime.of(14, 30))),
                new Showing(spiderMan, 5, LocalDateTime.of(showDate, LocalTime.of(16, 10))),
                new Showing(theBatMan, 6, LocalDateTime.of(showDate, LocalTime.of(17, 50))),
                new Showing(turningRed, 7, LocalDateTime.of(showDate, LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(showDate, LocalTime.of(21, 10))),
                new Showing(theBatMan, 9, LocalDateTime.of(showDate, LocalTime.of(23, 0)))
        );
    }

}
