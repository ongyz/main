package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalTutorHelper;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tuitiontiming.TuitionTimingContainsKeywordsPredicate;

public class GroupCommandTest {
    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void equals() {
        TuitionTimingContainsKeywordsPredicate firstPredicate =
                new TuitionTimingContainsKeywordsPredicate("Monday");
        TuitionTimingContainsKeywordsPredicate secondPredicate =
                new TuitionTimingContainsKeywordsPredicate("5:00pm");

        GroupCommand groupDayCommand = new GroupCommand(firstPredicate, true, false);
        GroupCommand groupTimeCommand = new GroupCommand(secondPredicate, false, true);

        // same object -> returns true
        assertTrue(groupDayCommand.equals(groupDayCommand));
        assertTrue(groupTimeCommand.equals(groupTimeCommand));

        // same values -> returns true
        GroupCommand groupDayCommandCopy = new GroupCommand(firstPredicate, true, false);
        assertTrue(groupDayCommand.equals(groupDayCommandCopy));

        GroupCommand groupTimeCommandCopy = new GroupCommand(secondPredicate, false, true);
        assertTrue(groupTimeCommand.equals(groupTimeCommandCopy));

        // different types -> returns false
        assertFalse(groupDayCommand.equals(1));
        assertFalse(groupTimeCommand.equals(1));

        // null -> returns false
        assertFalse(groupDayCommand.equals(null));
        assertFalse(groupTimeCommand.equals(null));

        // different command -> returns false
        assertFalse(groupDayCommand.equals(groupTimeCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        TuitionTimingContainsKeywordsPredicate predicate = new TuitionTimingContainsKeywordsPredicate(" ");
        GroupCommand firstCommand = new GroupCommand(predicate, false, false);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(firstCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_keywords_multiplePersonsFound() {
        //day predicate
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        TuitionTimingContainsKeywordsPredicate dayPredicate = new TuitionTimingContainsKeywordsPredicate("Saturday");
        GroupCommand command = new GroupCommand(dayPredicate, true, false);
        expectedModel.updateFilteredPersonList(dayPredicate);
        expectedModel.sortByTime();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, DANIEL), model.getFilteredPersonList());

        //time predicate
        expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        TuitionTimingContainsKeywordsPredicate timePredicate = new TuitionTimingContainsKeywordsPredicate("5:00pm");
        command = new GroupCommand(timePredicate, false, true);
        expectedModel.updateFilteredPersonList(timePredicate);
        expectedModel.sortByDay();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, GEORGE), model.getFilteredPersonList());
    }
}
