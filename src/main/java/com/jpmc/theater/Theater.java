package com.jpmc.theater;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Theater {

    //I am assuming the theater has only 1 screen where each movie is shown one by one and the seating capacity is 100.
    //this will help us in making sure that we are not increasing the scope of the task.
    public static final int SEATING_CAPACITY = 100;

    private LocalDateProvider provider;

    //schedule should be a map of key -> Date and value -> list of showings,
    //this will help a customer make reservation for current day or for future days.
    private Map<LocalDate, List<Showing>> schedule;

    public Theater(LocalDateProvider provider, Map<LocalDate, List<Showing>> schedule) {
        validate(provider, schedule);
        this.provider = provider;
        this.schedule = schedule;
    }

    private void validate(LocalDateProvider provider, Map<LocalDate, List<Showing>> schedule) {
        if(provider == null || schedule == null) {
            throw new IllegalStateException("Theater cannot be created");
        }
    }

    public Reservation reserve(Customer customer, LocalDate showDate, int sequence, int howManyTickets) {
        if(showDate.isBefore(provider.currentDate())) {
            throw new IllegalStateException("cannot book tickets for showing in the past");
        }
        Showing showing;
        try {
            if(schedule.containsKey(showDate)) {
                showing = schedule.get(showDate).get(sequence - 1);
            } else {
                throw new IllegalStateException("not able to find any showing for showDate: " + showDate);
            }
        } catch (IndexOutOfBoundsException  iobe) {
            throw new IllegalStateException("not able to find any showing for given sequence " + sequence);
        }
        showing.reduceSeatsLeft(howManyTickets);
        return new Reservation(customer, showDate, showing, howManyTickets);
    }

    public void printSchedule(PrintSchedule printSchedule, LocalDate showDate) {
        if(schedule.containsKey(showDate)) {
            if(printSchedule == PrintSchedule.TEXT) {
                printScheduleInTextFormat(showDate);
            } else {
                //JSON format
                printScheduleInJsonFormat(showDate);
            }
        } else {
            throw new IllegalStateException("not able to find any showing for showDate: " + showDate);
        }
    }

    private void printScheduleInTextFormat(LocalDate showDate) {
        System.out.println(showDate);
        System.out.println("===================================================");
        schedule.get(showDate).forEach(s ->
                System.out.println(s.getSequenceOfTheDay() + ": " + s.getStartTime() + " " + s.getMovie().getTitle() + " " + Utility.humanReadableFormat(s.getMovie().getRunningTime()) +
                        " Seats left: " + s.getSeatsLeft() + " $" + s.getMovieFee() + " (movieFee) --> $" + s.getDiscountedMovieFee() + " (discountedMovieFee)")
        );
        System.out.println("===================================================");
    }

    private void printScheduleInJsonFormat(LocalDate showDate) {
        System.out.println(showDate);
        System.out.println("===================================================");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        schedule.get(showDate).forEach(s -> {
                    try {
                        System.out.println(mapper.writeValueAsString(s));
                    } catch (JsonProcessingException e) {
                        System.out.println("Failed to print schedule in Json format.");
                        e.printStackTrace();
                    }
                }
        );
        System.out.println("===================================================");
    }

    public static void main(String[] args) {
        LocalDateProvider provider = LocalDateProvider.singleton();
        Map<LocalDate, List<Showing>> schedule = new HashMap<>();
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90), 12.5, 1);
        Movie turningRed = new Movie("Turning Red", Duration.ofMinutes(85), 11);
        Movie theBatMan = new Movie("The Batman", Duration.ofMinutes(95), 9);
        LocalDate today = provider.currentDate();
        List<Showing> todaysSchedule = List.of(
                new Showing(turningRed, 1, LocalDateTime.of(today, LocalTime.of(9, 0))),
                new Showing(spiderMan, 2, LocalDateTime.of(today, LocalTime.of(11, 0))),
                new Showing(theBatMan, 3, LocalDateTime.of(today, LocalTime.of(12, 50))),
                new Showing(turningRed, 4, LocalDateTime.of(today, LocalTime.of(14, 30))),
                new Showing(spiderMan, 5, LocalDateTime.of(today, LocalTime.of(16, 10))),
                new Showing(theBatMan, 6, LocalDateTime.of(today, LocalTime.of(17, 50))),
                new Showing(turningRed, 7, LocalDateTime.of(today, LocalTime.of(19, 30))),
                new Showing(spiderMan, 8, LocalDateTime.of(today, LocalTime.of(21, 10))),
                new Showing(theBatMan, 9, LocalDateTime.of(today, LocalTime.of(23, 0)))
        );
        //todays showing
        schedule.put(today, todaysSchedule);

        Theater theater = new Theater(LocalDateProvider.singleton(), schedule);
        System.out.println("==================== Printing in Text format ====================");
        theater.printSchedule(PrintSchedule.TEXT, today);
        System.out.println("==================== Printing in Json format ====================");
        theater.printSchedule(PrintSchedule.JSON, today);

        System.out.println("======== Reservation complete with following details ============");
        Reservation reservationForRajesh = theater.reserve(new Customer("Rajesh", "1"), today, 2, 3);
        System.out.println("Customer: " + reservationForRajesh.getCustomer().getName() + " ShowDate: " + reservationForRajesh.getShowDate() + " Movie: " + reservationForRajesh.getShowing().getMovie().getTitle() +
                " NoOfTickets: " + reservationForRajesh.getAudienceCount() + " TotalFee: " + reservationForRajesh.getTotalFee());

        System.out.println("No of seats left after reservation: " + reservationForRajesh.getShowing().getSeatsLeft());
    }
}
