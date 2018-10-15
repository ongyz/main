package seedu.address.model.tuitionTiming;

import static java.util.Objects.requireNonNull;

/**
 * Enum for Day in TuitionTiming class.
 */
public enum Day {
    Monday("Monday"),
    Tuesday("Tuesday"),
    Wednesday("Wednesday"),
    Thursday("Thursday"),
    Friday("Friday"),
    Saturday("Saturday"),
    Sunday("Sunday");

    private final String stringRepresentation;

    Day (String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public String getDay() {
        return this.stringRepresentation;
    }

    /**
     * Converts a string representation of day into a Day enum.
     */
    public static Day convertStringToDayEnum(String stringRepresentation) {
        requireNonNull(stringRepresentation);
        Day day = Day.valueOf(stringRepresentation);
        return day;
    }
}
