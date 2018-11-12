package tutorhelper.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tutorhelper.commons.core.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.testutil.TypicalStudents.BENSON;
import static tutorhelper.testutil.TypicalStudents.CARL;
import static tutorhelper.testutil.TypicalStudents.DANIEL;
import static tutorhelper.testutil.TypicalStudents.GEORGE;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.UserPrefs;
import tutorhelper.model.tuitiontiming.TuitionTimingContainsKeywordsPredicate;

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
    public void execute_zeroKeywords_noStudentFound() {
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 0);
        TuitionTimingContainsKeywordsPredicate predicate = new TuitionTimingContainsKeywordsPredicate(" ");
        GroupCommand command = new GroupCommand(predicate, false, false);
        expectedModel.updateFilteredStudentList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredStudentList());
    }

    @Test
    public void execute_keywords_multipleStudentsFound() {
        //day predicate
        String expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 2);
        TuitionTimingContainsKeywordsPredicate dayPredicate = new TuitionTimingContainsKeywordsPredicate("Saturday");
        GroupCommand command = new GroupCommand(dayPredicate, true, false);
        expectedModel.updateFilteredStudentList(dayPredicate);
        expectedModel.sortByTime();
        expectedModel.commitTutorHelper();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, DANIEL), model.getFilteredStudentList());

        //time predicate
        expectedMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW, 2);
        TuitionTimingContainsKeywordsPredicate timePredicate = new TuitionTimingContainsKeywordsPredicate("5:00pm");
        command = new GroupCommand(timePredicate, false, true);
        expectedModel.updateFilteredStudentList(timePredicate);
        expectedModel.sortByDay();
        expectedModel.commitTutorHelper();
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BENSON, GEORGE), model.getFilteredStudentList());
    }
}
