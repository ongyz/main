package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.DeleteSubCommand.MESSAGE_DELETESUB_SUCCESS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.TestUtil.getStudent;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;
import static seedu.address.testutil.TypicalStudents.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteSubCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

public class DeleteSubCommandSystemTest extends TutorHelperSystemTest {

    private static final String MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteSubCommand.MESSAGE_USAGE);

    @Test
    public void deletesub() {
        /* ----------------- Performing erase operation while an unfiltered list is being shown -------------------- */

        /* Case: delete the first subject of the third student in the list,
         * command with leading spaces and trailing spaces -> success
         */
        Model lastModel = getModel();
        Model expectedModel = getModel();
        String command = "     " + DeleteSubCommand.COMMAND_WORD + "      "
                + INDEX_THIRD_STUDENT.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + "       ";

        Student deletedSubStudent = deleteSubStudent(expectedModel, INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT);
        String expectedResultMessage = String.format(MESSAGE_DELETESUB_SUCCESS, deletedSubStudent);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: undo command  -> third student restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, lastModel, expectedResultMessage);

        /* Case: redo command the last student in the list -> last student syllabus is erased again */
        command = RedoCommand.COMMAND_WORD;
        deleteSubStudent(lastModel, INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, lastModel, expectedResultMessage);

        /* ----------------- Performing deletesub operation while a filtered list is being shown ------------------  */

        /* Case: filtered student list, student index within bounds of TutorHelper and student list -> success */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        Index studentIndex = INDEX_SECOND_STUDENT;
        assertTrue(studentIndex.getZeroBased() < expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(studentIndex, INDEX_FIRST_SUBJECT);

        /* Case: filtered student list, student index within bounds of TutorHelper but out of bounds of student list
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getStudentList().size();
        command = DeleteSubCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SUBJECT.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: filtered student list, student index within bounds but subject index is out of bounds
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getStudentList()
                .get(INDEX_SECOND_STUDENT.getZeroBased()).getSubjects().size() + 1;
        command = DeleteSubCommand.COMMAND_WORD + " " + INDEX_SECOND_STUDENT.getOneBased()
                + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_SUBJECT_INDEX);

        /* ------------------------------- Performing invalid deletesub operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteSubCommand.COMMAND_WORD + " 0 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteSubCommand.COMMAND_WORD + " -1 -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getStudentList().size() + 1);
        command = DeleteSubCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 1";
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(
                DeleteSubCommand.COMMAND_WORD + " a b", MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(
                DeleteSubCommand.COMMAND_WORD + " 1 a b", MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("dElEtEsUb 1 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Subject} at the specified {@code subjectIndex} for
     * the {@code Student} at the specified {@code studentIndex} in {@code model}'s TutorHelper.
     * @return the updated student
     */
    private Student deleteSubStudent(Model model, Index studentIndex, Index subjectIndex) {
        Student studentTarget = getStudent(model, studentIndex);
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());
        subjects.remove(subjectIndex.getZeroBased());

        Set<Subject> newSubjects = new HashSet<>(subjects);
        Student studentUpdated = SubjectsUtil.createStudentWithNewSubjects(studentTarget, newSubjects);

        model.updateStudent(studentTarget, studentUpdated);
        return studentUpdated;
    }

    /**
     * Executes {@code command} and in addition,
     * 1. Asserts that the command box displays an empty string.
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.
     * 3. Asserts that the browser url and selected card remains unchanged.
     * 4. Asserts that the status bar's sync status changes.
     * 5. Asserts that the command box has the default style class.
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Index studentIndex, Index subjectIndex) {
        Model expectedModel = getModel();
        String command = DeleteSubCommand.COMMAND_WORD
                + " " + studentIndex.getOneBased()
                + " " + subjectIndex.getOneBased();
        Student deletedSubStudent = deleteSubStudent(expectedModel, studentIndex, subjectIndex);
        String expectedResultMessage = String.format(MESSAGE_DELETESUB_SUCCESS, deletedSubStudent);
        assertCommandSuccess(command,
                expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,
     * 1. Asserts that the command box displays an empty string.
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.
     * 3. Asserts that the browser url and selected card remains unchanged.
     * 4. Asserts that the status bar's sync status changes.
     * 5. Asserts that the command box has the default style class.
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        expectedModel.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
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
