package com.jpmc.theater;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * Utility class containing helpful methods
 */
public class Utility {

    private static final int DEFAULT_DECIMAL_POINTS = 2;
    private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    /**
     * Method to round off double value using default setup.
     * @param value
     * @return rounded off double value
     */
    public static double roundOff(double value) {
        return roundOff(value, DEFAULT_DECIMAL_POINTS, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Method to round off double value using default rounding mode but a non-default precision.
     * Note: this is not used anywhere at the moment, but can be handy in the future if needed.
     * @param value
     * @param decimalPoints
     * @return rounded off double value
     */
    public static double roundOff(double value, int decimalPoints) {
        return roundOff(value, decimalPoints, DEFAULT_ROUNDING_MODE);
    }

    /**
     * Method to round off the double value without using default setup.
     * This method can be handy if we decide to use a different rounding off mode and precision point in the future.
     * @param value
     * @param decimalPoints
     * @param roundingMode
     * @return rounded off double value
     */
    public static double roundOff(double value, int decimalPoints, RoundingMode roundingMode) {
        if (decimalPoints < 0) {
            throw new IllegalArgumentException("Invalid decimal points");
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPoints, roundingMode);
        return bd.doubleValue();
    }

    public static boolean checkIfHourFallsWithinRange(int showStartHour, int lowerRange, int upperRange) {
        if (showStartHour < 0 || showStartHour > 23) {
            throw new IllegalArgumentException("Invalid showStartHour");
        }
        if (lowerRange < 0 || lowerRange > 23 || upperRange < 0 || upperRange > 23 || upperRange < lowerRange) {
            throw new IllegalArgumentException("Invalid lower/upper range");
        }
        if (showStartHour >= lowerRange && showStartHour <= upperRange) {
            return true;
        }

        return false;
    }

    /**
     * Method for finding out next date with the given day of the month
     * @param day day of the month
     * @return next Date for the day
     */
    public static LocalDate nextDateForDay(LocalDate startDate, int day) {
        if(day < 1 || day > 31) {
            throw new IllegalStateException("invalid day");
        }
        while(startDate.getDayOfMonth() != day) {
            startDate = startDate.plusDays(1);
        }
        return startDate;
    }

    /**
     * Method for transforming Duration into human readable form.
     * This will be used for both simple TEXT and JSon output.
     * @param duration
     * @return
     */
    public static String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format("(%s hour%s %s minute%s)", hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    private static String handlePlural(long value) {
        if (value == 1) {
            return "";
        }
        else {
            return "s";
        }
    }

}
