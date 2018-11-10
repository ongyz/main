package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DUPLICATE_SYLLABUS;
import static seedu.address.logic.commands.CommandTestUtil.SYLLABUS_DESC_DIFFERENTIATION;
import static seedu.address.logic.commands.CommandTestUtil.SYLLABUS_DESC_INTEGRATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SYLLABUS_DIFFERENTIATION;
import static seedu.address.logic.commands.EditSyllCommand.MESSAGE_DUPLICATE_SYLLABUS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.KEYWORD_MATCHING_ALICE;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditSyllCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.testutil.StudentBuilder;

public class EditSyllCommandSystemTest extends TutorHelperSystemTest {

    @Test
    public void editsyll() {
        Model model = getModel();

        /* ---------------- Performing editsyll operation while an unfiltered list is being shown ------------------- */

        /* Case: edit the syllabus entry of first subject of the first student in the list
         * -> edited
         */
        Index index = INDEX_FIRST_STUDENT;
        Index indexSubject = INDEX_FIRST_SUBJECT;
        Index indexSyll = INDEX_FIRST_SYLLABUS;
        String command = " " + EditSyllCommand.COMMAND_WORD + " " + index.getOneBased() + " "
                + indexSubject.getOneBased() + " " + indexSyll.getOneBased() + " " + SYLLABUS_DESC_DIFFERENTIATION
                + " ";

        Student editedStudent = new StudentBuilder(ALICE).replaceSyllabus(indexSubject, indexSyll,
                VALID_SYLLABUS_DIFFERENTIATION).build();
        assertCommandSuccess(command, index, editedStudent);

        /* Case: undo editing the last student in the list -> last student restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last student in the list -> last student edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateStudent(
                getModel().getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased()), editedStudent);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered student list, edit index within bounds of TutorHelper and student list -> edited */
        showStudentsWithName(KEYWORD_MATCHING_ALICE);
        index = INDEX_FIRST_STUDENT;
        assertTrue(index.getZeroBased() < getModel().getFilteredStudentList().size());
        command = EditSyllCommand.COMMAND_WORD + " " + index.getOneBased() + " " + indexSubject.getOneBased()
                + " " + indexSyll.getOneBased() + " " + SYLLABUS_DESC_INTEGRATION + " ";
        Student studentToEdit = getModel().getFilteredStudentList().get(index.getZeroBased());
        editedStudent = new StudentBuilder(ALICE).replaceSyllabus(indexSubject, indexSyll,
                DUPLICATE_SYLLABUS).build();
        assertCommandSuccess(command, index, editedStudent);

        /* Case: filtered student list, edit index within bounds of TutorHelper but out of bounds of student list
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_ALICE);
        int invalidIndex = getModel().getTutorHelper().getStudentList().size();
        assertCommandFailure(EditSyllCommand.COMMAND_WORD + " " + invalidIndex + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " " + INDEX_FIRST_SYLLABUS + " " + SYLLABUS_DESC_DIFFERENTIATION,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditSyllCommand.COMMAND_WORD + " 0" + " " + INDEX_FIRST_SYLLABUS.getOneBased()
                + " " + SYLLABUS_DESC_DIFFERENTIATION, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                EditSyllCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditSyllCommand.COMMAND_WORD + " -1" + " " + INDEX_FIRST_SYLLABUS.getOneBased()
                + " " + SYLLABUS_DESC_DIFFERENTIATION, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                EditSyllCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredStudentList().size() + 1;
        assertCommandFailure(EditSyllCommand.COMMAND_WORD + " " + invalidIndex + " "
                + INDEX_FIRST_SYLLABUS.getOneBased() + " " + SYLLABUS_DESC_DIFFERENTIATION,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));

        /* Case: missing index -> rejected */
        assertCommandFailure(EditSyllCommand.COMMAND_WORD + " " + SYLLABUS_DESC_DIFFERENTIATION,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));

        /* Case: missing syllabus field -> rejected */
        assertCommandFailure(EditSyllCommand.COMMAND_WORD + " " + INDEX_FIRST_STUDENT.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " " + INDEX_FIRST_SYLLABUS.getOneBased(),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));

        /* Case: duplicate syllabus field -> rejected */
        assertCommandFailure(EditSyllCommand.COMMAND_WORD + " " + INDEX_FIRST_STUDENT.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " " + INDEX_FIRST_SYLLABUS.getOneBased() + " "
                + SYLLABUS_DESC_INTEGRATION, MESSAGE_DUPLICATE_SYLLABUS);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Student, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Student, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Student editedStudent) {
        assertCommandSuccess(command, toEdit, editedStudent, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the student at index {@code toEdit} being
     * updated to values specified {@code editedStudent}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Student editedStudent,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        expectedModel.updateStudent(expectedModel.getFilteredStudentList().get(toEdit.getZeroBased()), editedStudent);
        expectedModel.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        assertCommandSuccess(command, expectedModel,
                String.format(EditSyllCommand.MESSAGE_EDITSYLL_SUCCESS, editedStudent), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see TutorHelperSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
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
