package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class TuitionTimingContainsKeywordsPredicate implements Predicate<Person> {

    private final String keyword;

    public TuitionTimingContainsKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Person person) {
        String trimmedArgs = person.getTuitionTiming().value.trim();
        String[] trimmedArgsArr = trimmedArgs.split("\\s+");
        String day = trimmedArgsArr[0];
        String time = trimmedArgsArr[1];

        if (day.endsWith(",")) {
            day = day.substring(0, day.length() - 1);
        }

        if (keyword.matches(TuitionTiming.DAY_REGEX)) {
            return StringUtil.containsWordIgnoreCase(day, keyword);
        } else if (keyword.matches(TuitionTiming.TIME_REGEX)) {
            return StringUtil.containsWordIgnoreCase(time, keyword);
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TuitionTimingContainsKeywordsPredicate // instanceof handles nulls
                && keyword.equals(((TuitionTimingContainsKeywordsPredicate) other).keyword)); // state check
    }
}
