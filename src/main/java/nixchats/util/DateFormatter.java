package nixchats.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for formatting dates in NixChats.
 * Handles conversion from YYYY-MM-DD input format to MMM dd yyyy display format.
 */
public class DateFormatter {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Formats a date string from YYYY-MM-DD to MMM dd yyyy format.
     * If the input is not in the expected format, returns the original string.
     *
     * @param dateString The date string to format (expected in YYYY-MM-DD format)
     * @return Formatted date string in MMM dd yyyy format, or original string if parsing fails
     */
    public static String formatDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return dateString;
        }

        try {
            LocalDate date = LocalDate.parse(dateString.trim(), INPUT_FORMAT);
            return date.format(OUTPUT_FORMAT);
        } catch (DateTimeParseException e) {
            // If parsing fails, return the original string
            // This maintains backward compatibility with existing data
            return dateString;
        }
    }

    /**
     * Validates if a date string is in the correct YYYY-MM-DD format.
     *
     * @param dateString The date string to validate
     * @return true if the date string is valid, false otherwise
     */
    public static boolean isValidDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }

        try {
            LocalDate.parse(dateString.trim(), INPUT_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
