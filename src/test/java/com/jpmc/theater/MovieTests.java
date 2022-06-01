package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MovieTests {

    @Test
    void testCreateMovieWithSpecialCode() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5, 1);
        assertEquals("Spider-Man: No Way Home", spiderMan.getTitle());
        assertEquals(Duration.ofMinutes(90), spiderMan.getRunningTime());
        assertEquals(12.5, spiderMan.getTicketPrice());
        assertEquals(1, spiderMan.getSpecialCode());
    }

    @Test
    void testCreateMovieWithoutSpecialCode() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),12.5);
        assertEquals("Spider-Man: No Way Home", spiderMan.getTitle());
        assertEquals(Duration.ofMinutes(90), spiderMan.getRunningTime());
        assertEquals(12.5, spiderMan.getTicketPrice());
        assertEquals(0, spiderMan.getSpecialCode());
    }

    @Test
    void testCreateMovieWithZeroTicketPrice() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),0.0, 1);
        assertEquals("Spider-Man: No Way Home", spiderMan.getTitle());
        assertEquals(Duration.ofMinutes(90), spiderMan.getRunningTime());
        assertEquals(0.0, spiderMan.getTicketPrice());
        assertEquals(1, spiderMan.getSpecialCode());
    }

    @Test
    void testCreateMovieWithEmptyTitle() {
        assertThrows(IllegalStateException.class, () -> {
            new Movie("  ", Duration.ofMinutes(90),12.5, 1);
        });
    }

    @Test
    void testCreateMovieWithMissingTitle() {
        assertThrows(IllegalStateException.class, () -> {
            new Movie(null, Duration.ofMinutes(90),12.5, 1);
        });
    }

    @Test
    void testCreateMovieWithMissingRunningTime() {
        assertThrows(IllegalStateException.class, () -> {
            new Movie("Spider-Man: No Way Home", null,12.5, 1);
        });
    }

    @Test
    void testCreateMovieWithInvalidTicketPrice() {
        assertThrows(IllegalStateException.class, () -> {
            new Movie("Spider-Man: No Way Home", Duration.ofMinutes(90),-2, 1);
        });
    }

}
