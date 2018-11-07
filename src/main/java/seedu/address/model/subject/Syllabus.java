package seedu.address.model.subject;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's syllabus in the TutorHelper.
 * Guarantees: immutable; is valid as declared in {@link #isValidSyllabus(String)}
 */
public class Syllabus {

    public static final String MESSAGE_SYLLABUS_CONSTRAINTS =
            "Syllabus can take any values, and it should not be blank or preceded by white space.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String SYLLABUS_VALIDATION_REGEX = "[^\\s].*";

    public final String syllabus;

    public final boolean state;

    /**
     * Constructor to facilitate immutability.
     *
     * @param syllabus A valid syllabus.
     */
    public Syllabus(String syllabus, boolean state) {
        requireNonNull(syllabus);
        checkArgument(isValidSyllabus(syllabus), MESSAGE_SYLLABUS_CONSTRAINTS);
        this.syllabus = syllabus;
        this.state = state;
    }

    /**
     * Construct a {@code Syllabus} from the {@code String syllabusName}
     * @param syllabusName
     * @return a new {@code Syllabus}
     */
    public static Syllabus makeSyllabus(String syllabusName) {
        checkArgument(isValidSyllabus(syllabusName), MESSAGE_SYLLABUS_CONSTRAINTS);
        Syllabus syllabus = new Syllabus(syllabusName, false);
        return syllabus;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidSyllabus(String test) {
        return test.matches(SYLLABUS_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        String marked = state ? "X" : "  ";
        return "[" + marked + "] " + syllabus;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Syllabus // instanceof handles nulls
                && syllabus.equals(((Syllabus) other).syllabus)); //content check
    }

    @Override
    public int hashCode() {
        return syllabus.hashCode();
    }

}
