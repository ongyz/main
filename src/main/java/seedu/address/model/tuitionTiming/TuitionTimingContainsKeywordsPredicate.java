package seedu.address.model.tuitionTiming;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.person.Person;

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
        DayOfWeek day = person.getTuitionTiming().day;
        String time = person.getTuitionTiming().time;

        if (keyword.matches(TuitionTiming.DAY_REGEX)) {
            return day.equals(DayOfWeek.valueOf(keyword.toUpperCase()));
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
