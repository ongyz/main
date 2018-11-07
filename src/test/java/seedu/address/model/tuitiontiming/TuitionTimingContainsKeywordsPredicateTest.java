package seedu.address.model.tuitiontiming;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.model.student.Student;
import seedu.address.testutil.StudentBuilder;

public class TuitionTimingContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        String firstPredicateKeywordList = "first";
        String secondPredicateKeywordList = "second";

        TuitionTimingContainsKeywordsPredicate firstPredicate = new TuitionTimingContainsKeywordsPredicate(
                firstPredicateKeywordList);
        TuitionTimingContainsKeywordsPredicate secondPredicate = new TuitionTimingContainsKeywordsPredicate(
                secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TuitionTimingContainsKeywordsPredicate firstPredicateCopy = new TuitionTimingContainsKeywordsPredicate(
                firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different student -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_tuitionTimingContainsKeywords_returnsTrue() {
        Student student = new StudentBuilder().withTuitionTiming("Monday 12:00pm").build();

        //keyword follows day regex
        TuitionTimingContainsKeywordsPredicate predicate = new TuitionTimingContainsKeywordsPredicate("Monday");
        assertTrue(predicate.test(student));

        //keyword follows time regex
        predicate = new TuitionTimingContainsKeywordsPredicate("12:00pm");
        assertTrue(predicate.test(student));
    }

    @Test
    public void test_tuitionTimingDoesNotContainKeywords_returnsFalse() {
        Student student = new StudentBuilder().withTuitionTiming("Monday 12:00pm").build();

        //Zero keywords
        TuitionTimingContainsKeywordsPredicate predicate = new TuitionTimingContainsKeywordsPredicate("");
        assertFalse(predicate.test(student));

        //Non-matching keywords
        predicate = new TuitionTimingContainsKeywordsPredicate("Tuesday");
        assertFalse(predicate.test(student));

        predicate = new TuitionTimingContainsKeywordsPredicate("1:00pm");
        assertFalse(predicate.test(student));
    }
}
