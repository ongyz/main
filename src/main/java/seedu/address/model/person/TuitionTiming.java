package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class TuitionTiming {

    public static final String MESSAGE_TUITIONTIMING_CONSTRAINTS =
            "Tuition Time and Day should contain the specific time and day, and it should not be blank";

    /*
     * The first character of the tuition time and day must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    //Need to refine this further
    public static final String TUITIONTIMING_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code TuitionTiming}.
     *
     * @param tuitionTiming A valid tuition time and day.
     */
    public TuitionTiming(String tuitionTiming) {
        requireNonNull(tuitionTiming);
        checkArgument(isValidTiming(tuitionTiming), MESSAGE_TUITIONTIMING_CONSTRAINTS);
        value = tuitionTiming;
    }

    /**
     * Returns true if a given string is a valid time and day
     */
    public static boolean isValidTiming(String test) {
        return test.matches(TUITIONTIMING_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Address // instanceof handles nulls
                && value.equals(((TuitionTiming) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
