package seedu.address.model.subject;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.stream.Stream;

import seedu.address.commons.util.StringUtil;

/**
 * Represents the subject name of Subjects.
 */
public enum SubjectType {

    Mathematics("Mathematics"),
    Biology("Biology"),
    Chemistry("Chemistry"),
    Physics("Physics"),
    Economics("Economics"),
    Geography("Geography"),
    History("History");

    public final String stringRepresentation;

    SubjectType(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public static Stream<SubjectType> stream() {
        return Stream.of(SubjectType.values());
    }

    /**
     * Returns the string representation of the whole subject types.
     */
    public static String listRepresentation() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(values()).forEach(subjectEnum -> builder.append(subjectEnum.stringRepresentation));
        return builder.toString();
    }

    /**
     * Converts {@code subjectName} to its equivalent {@code SubjectType}
     * @param subjectName the string representation
     * @return the equivalent {@code SubjectType}
     */
    public static SubjectType convertStringToSubjectName(String subjectName) {
        requireNonNull(subjectName);
        SubjectType result = null;
        for (SubjectType subjectEnum : values()) {
            if (StringUtil.containsSubstringIgnoreCase(subjectEnum.toString(), subjectName)) {
                result = subjectEnum;
            }
        }
        return result;
    }

    /**
     * Returns true if a given string is a valid subject.
     */
    public static boolean isValidSubjectName(String test) {
        requireNonNull(test);
        return SubjectType.stream()
                .anyMatch(subjectEnum -> StringUtil.containsSubstringIgnoreCase(subjectEnum.toString(), test));
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }
}
