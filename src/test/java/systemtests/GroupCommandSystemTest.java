package systemtests;

import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.commons.core.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;

import org.junit.Test;

import tutorhelper.commons.core.Messages;
import tutorhelper.logic.commands.GroupCommand;
import tutorhelper.logic.commands.UndoCommand;
import tutorhelper.model.Model;
import tutorhelper.model.tuitiontiming.TuitionTimingContainsKeywordsPredicate;

public class GroupCommandSystemTest extends TutorHelperSystemTest {
    private static final String MESSAGE_INVALID_GROUP_COMMAND_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE);

    @Test
    public void group() {
        /* --------------------- Performing group operation while an unfiltered list is shown ---------------------- */
        Model modelBeforeGrouping = getModel();
        Model expectedModel = getModel();
        String command = "     " + GroupCommand.COMMAND_WORD + "      ";
        String undoCommand = UndoCommand.COMMAND_WORD;

        //Groups by day
        String validDayCommand = command + "Monday ";
        expectedModel.updateFilteredStudentList(new TuitionTimingContainsKeywordsPredicate("Monday"));
        String expectedResultMessage = String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW,
                expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(validDayCommand, true, false, expectedModel, expectedResultMessage);
        //Undo sorting by time
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(undoCommand, modelBeforeGrouping, expectedResultMessage);

        //Groups by time
        String validTimeCommand = command + "5:00pm ";
        expectedModel.updateFilteredStudentList(new TuitionTimingContainsKeywordsPredicate("5:00pm"));
        expectedResultMessage = String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW,
                expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(validTimeCommand, false, true, expectedModel, expectedResultMessage);
        //Undo sorting by day
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(undoCommand, modelBeforeGrouping, expectedResultMessage);


        /* ---------------------- Performing group operation while a filtered list is shown ------------------------ */
        //Initial model needs to be acquired and sorted first
        Model modelAfterFirstSort = getModel();
        modelAfterFirstSort.sortByTime();

        //Group by day and sort by time first
        String firstGroupCommand = command + "Saturday ";
        expectedModel.updateFilteredStudentList(new TuitionTimingContainsKeywordsPredicate("Saturday"));
        expectedResultMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW,
                expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(firstGroupCommand, true, false, expectedModel, expectedResultMessage);

        //Followed by grouping by time and sorting by day
        String secondGroupCommand = command + "1:00pm ";
        expectedModel.updateFilteredStudentList(new TuitionTimingContainsKeywordsPredicate("1:00pm"));
        expectedResultMessage = String.format(MESSAGE_STUDENTS_LISTED_OVERVIEW,
                expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(secondGroupCommand, false, true, expectedModel, expectedResultMessage);

        //Undo sorting twice
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(undoCommand, modelAfterFirstSort, expectedResultMessage);
        assertCommandSuccess(undoCommand, modelBeforeGrouping, expectedResultMessage);


        /* -------------------------------- Performing invalid group operation ------------------------------------- */
        String invalidDayCommand = command + "MONDAY ";
        assertCommandFailure(invalidDayCommand, MESSAGE_INVALID_GROUP_COMMAND_FORMAT);

        String invalidTimeCommand = command + "1300 ";
        assertCommandFailure(invalidTimeCommand, MESSAGE_INVALID_GROUP_COMMAND_FORMAT);

        //command with no additional parameter
        assertCommandFailure(command, MESSAGE_INVALID_GROUP_COMMAND_FORMAT);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card remains unchanged.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * 6. Sorts the list depending on whether its grouped by day or time.<br>
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, boolean isDay, boolean isTime,
                                      Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        if (isDay && !isTime) {
            expectedModel.sortByTime();
            expectedModel.commitTutorHelper();
        } else if (isTime && !isDay) {
            expectedModel.sortByDay();
            expectedModel.commitTutorHelper();
        }
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card remains unchanged.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
