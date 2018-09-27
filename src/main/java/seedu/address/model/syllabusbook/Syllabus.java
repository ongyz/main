package seedu.address.model.syllabusbook;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's syllabus in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSyllabus(String)}
 */
public class Syllabus {

    public static final String MESSAGE_SYLLABUS_CONSTRAINTS =
            "Syllabus can take any values, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String SYLLABUS_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code Syllabus}.
     *
     * @param syllabus A valid syllabus.
     */
    public Syllabus(String syllabus) {
        requireNonNull(syllabus);
        checkArgument(isValidSyllabus(syllabus), MESSAGE_SYLLABUS_CONSTRAINTS);
        value = syllabus;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidSyllabus(String test) {
        return test.matches(SYLLABUS_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Syllabus // instanceof handles nulls
                && value.equals(((Syllabus) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
