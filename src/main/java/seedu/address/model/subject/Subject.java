package seedu.address.model.subject;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Subject {

    public static final String MESSAGE_SUBJECT_CONSTRAINTS =
            "Subject should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String SUBJECT_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String subjectName;

    /**
     * Constructs a {@code Name}.
     *
     * @param subject Subjects that the student is taking.
     */
    public Subject(String subject) {
        requireNonNull(subject);
        checkArgument(isValidSubjectName(subject), MESSAGE_SUBJECT_CONSTRAINTS);
        subjectName = subject;
    }

    /**
     * Returns true if a given string is a valid subject.
     */
    public static boolean isValidSubjectName(String test) {
        return test.matches(SUBJECT_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return subjectName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Subject // instanceof handles nulls
                && subjectName.equals(((Subject) other).subjectName)); // state check
    }

    @Override
    public int hashCode() {
        return subjectName.hashCode();
    }
}
