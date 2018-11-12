package systemtests;

import static org.junit.Assert.assertFalse;
import static tutorhelper.commons.core.Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW;
import static tutorhelper.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tutorhelper.testutil.TypicalStudents.BENSON;
import static tutorhelper.testutil.TypicalStudents.CARL;
import static tutorhelper.testutil.TypicalStudents.DANIEL;
import static tutorhelper.testutil.TypicalStudents.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.DeleteCommand;
import tutorhelper.logic.commands.FindCommand;
import tutorhelper.logic.commands.RedoCommand;
import tutorhelper.logic.commands.UndoCommand;
import tutorhelper.model.Model;
import tutorhelper.model.tag.Tag;

public class FindCommandSystemTest extends TutorHelperSystemTest {

    @Test
    public void find() {
        /* Case: find multiple students in TutorHelper, command with leading spaces and trailing spaces
         * -> 2 students found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();

        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where student list is displaying the students we are finding
         * -> 2 students found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find student where student list is not displaying the student we are finding -> 1 student found */
        command = FindCommand.COMMAND_WORD + " Carl";
        ModelHelper.setFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple students in TutorHelper, 2 keywords -> 2 students found */
        command = FindCommand.COMMAND_WORD + " Benson Daniel";
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple students in TutorHelper, 2 keywords in reversed order -> 2 students found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple students in TutorHelper, 2 keywords with 1 repeat -> 2 students found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson Daniel";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple students in TutorHelper, 2 matching keywords and 1 non-matching keyword
         * -> 2 students found
         */
        command = FindCommand.COMMAND_WORD + " Daniel Benson NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same students in TutorHelper after deleting 1 of them -> 1 student found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getTutorHelper().getStudentList().contains(BENSON));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find student in TutorHelper, keyword is same as name but of different case -> 1 student found */
        command = FindCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find student in TutorHelper, keyword is substring of name -> 0 students found */
        command = FindCommand.COMMAND_WORD + " Mei";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find student in TutorHelper, name is substring of keyword -> 0 students found */
        command = FindCommand.COMMAND_WORD + " Meiers";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find student not in TutorHelper -> 0 students found */
        command = FindCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of student in TutorHelper -> 0 students found */
        command = FindCommand.COMMAND_WORD + " " + DANIEL.getPhone().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of student in TutorHelper -> 0 students found */
        command = FindCommand.COMMAND_WORD + " " + DANIEL.getAddress().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of student in TutorHelper -> 0 students found */
        command = FindCommand.COMMAND_WORD + " " + DANIEL.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of student in TutorHelper -> 0 students found */
        List<Tag> tags = new ArrayList<>(DANIEL.getTags());
        command = FindCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a student is selected -> selected card deselected */
        showAllStudents();
        selectStudent(Index.fromOneBased(1));
        assertFalse(getStudentListPanel().getHandleToSelectedCard().getName().equals(DANIEL.getName().fullName));
        command = FindCommand.COMMAND_WORD + " Daniel";
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find student in empty TutorHelper -> 0 students found */
        deleteAllStudents();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_STUDENTS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_STUDENTS_LISTED_OVERVIEW, expectedModel.getFilteredStudentList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
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
