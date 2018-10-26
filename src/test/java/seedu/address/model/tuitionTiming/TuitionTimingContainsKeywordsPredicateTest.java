package seedu.address.model.tuitionTiming;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import seedu.address.model.person.Person;
import seedu.address.model.tuitiontiming.TuitionTimingContainsKeywordsPredicate;
import seedu.address.testutil.PersonBuilder;

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

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_tuitionTimingContainsKeywords_returnsTrue() {
        Person person = new PersonBuilder().withTuitionTiming("Monday 12:00pm").build();

        //keyword follows day regex
        TuitionTimingContainsKeywordsPredicate predicate = new TuitionTimingContainsKeywordsPredicate("Monday");
        assertTrue(predicate.test(person));

        //keyword follows time regex
        predicate = new TuitionTimingContainsKeywordsPredicate("12:00pm");
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_tuitionTimingDoesNotContainKeywords_returnsFalse() {
        Person person = new PersonBuilder().withTuitionTiming("Monday 12:00pm").build();

        //Zero keywords
        TuitionTimingContainsKeywordsPredicate predicate = new TuitionTimingContainsKeywordsPredicate("");
        assertFalse(predicate.test(person));

        //Non-matching keywords
        predicate = new TuitionTimingContainsKeywordsPredicate("Tuesday");
        assertFalse(predicate.test(person));

        predicate = new TuitionTimingContainsKeywordsPredicate("1:00pm");
        assertFalse(predicate.test(person));
    }
}
