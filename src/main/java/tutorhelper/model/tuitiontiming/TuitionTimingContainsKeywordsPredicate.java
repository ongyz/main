package tutorhelper.model.tuitiontiming;

import java.time.DayOfWeek;
import java.util.function.Predicate;

import tutorhelper.commons.util.StringUtil;
import tutorhelper.model.student.Student;

/**
 * Tests that a {@code Student}'s {@code TuitionTiming} matches any of the keywords given.
 */
public class TuitionTimingContainsKeywordsPredicate implements Predicate<Student> {

    private final String keyword; //note that keyword is capitalised for day.

    public TuitionTimingContainsKeywordsPredicate(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public boolean test(Student student) {
        DayOfWeek day = student.getTuitionTiming().day;
        String time = student.getTuitionTiming().time;

        if (keyword.matches(TuitionTiming.DAY_REGEX)) {
            return day.equals(DayOfWeek.valueOf(keyword.toUpperCase()));
        } else if ((keyword.toLowerCase()).matches((TuitionTiming.TIME_REGEX).toLowerCase())) {
            return StringUtil.containsWordIgnoreCase(time, keyword);
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TuitionTimingContainsKeywordsPredicate // instanceof handles nulls
                && keyword.equals(((TuitionTimingContainsKeywordsPredicate) other).keyword)); // state check
    }
}
