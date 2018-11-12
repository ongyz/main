package systemtests;

import static org.junit.Assert.assertTrue;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SYLLABUS_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tutorhelper.logic.commands.DeleteSyllCommand.MESSAGE_DELETESYLL_SUCCESS;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static tutorhelper.testutil.TestUtil.getLastIndex;
import static tutorhelper.testutil.TestUtil.getMidIndex;
import static tutorhelper.testutil.TestUtil.getStudent;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static tutorhelper.testutil.TypicalStudents.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import tutorhelper.commons.core.Messages;
import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.DeleteSyllCommand;
import tutorhelper.logic.commands.RedoCommand;
import tutorhelper.logic.commands.UndoCommand;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;
import tutorhelper.model.util.SubjectsUtil;

public class DeleteSyllCommandSystemTest extends TutorHelperSystemTest {

    private static final String MESSAGE_INVALID_DELETESYLL_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteSyllCommand.MESSAGE_USAGE);

    @Test
    public void deletesyll() throws CommandException {
        /* ----------------- Performing erase operation while an unfiltered list is being shown -------------------- */

        /* Case: erase the syllabus of first subject of the first student in the list,
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        assertCommandSuccess(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* Case: erase ths syllabus of first subject the last student in the list -> success */
        Model modelBeforeMarkingLast = getModel();
        Index lastStudentIndex = getLastIndex(modelBeforeMarkingLast);
        assertCommandSuccess(lastStudentIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* Case: undo command the last student in the list -> last student restored */
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: redo command the last student in the list -> last student syllabus is erased again */
        command = RedoCommand.COMMAND_WORD;
        deleteSyllStudent(modelBeforeMarkingLast, lastStudentIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: erase syllabus of first subject of the middle student in the list -> success */
        Index middleStudentIndex = getMidIndex(getModel());
        assertCommandSuccess(middleStudentIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* ----------------- Performing deletesyll operation while a filtered list is being shown ------------------  */

        /* Case: filtered student list, student index within bounds of TutorHelper and student list -> success */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        Index studentIndex = INDEX_FIRST_STUDENT;
        assertTrue(studentIndex.getZeroBased() < expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(studentIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* Case: filtered student list, student index within bounds of TutorHelper but out of bounds of student list
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getStudentList().size();
        command = DeleteSyllCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SUBJECT.getOneBased()
                + " " + INDEX_FIRST_SYLLABUS.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: filtered student list, student index within bounds but subject index is out of bounds
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getStudentList()
                .get(INDEX_FIRST_STUDENT.getZeroBased()).getSubjects().size() + 1;
        command = DeleteSyllCommand.COMMAND_WORD + " " + INDEX_FIRST_STUDENT.getOneBased()
                + " " + invalidIndex
                + " " + INDEX_FIRST_SYLLABUS.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_SUBJECT_INDEX);

        /* Case: filtered student list, student and subject index within bounds but syllabus index is out of bounds
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        Set<Subject> subjects = getModel().getTutorHelper().getStudentList()
                .get(INDEX_FIRST_STUDENT.getZeroBased()).getSubjects();
        invalidIndex = new ArrayList<>(subjects)
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 5;
        command = DeleteSyllCommand.COMMAND_WORD + " " + INDEX_FIRST_STUDENT.getOneBased()
                + " " + INDEX_FIRST_SUBJECT.getOneBased()
                + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_SYLLABUS_INDEX);

        /* ------------------------------- Performing invalid deletesyll operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteSyllCommand.COMMAND_WORD + " 0 0 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETESYLL_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteSyllCommand.COMMAND_WORD + " -1 -1 -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETESYLL_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getStudentList().size() + 1);
        command = DeleteSyllCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 1 1";
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(
                DeleteSyllCommand.COMMAND_WORD + " a b c", MESSAGE_INVALID_DELETESYLL_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(
                DeleteSyllCommand.COMMAND_WORD + " 1 a b c", MESSAGE_INVALID_DELETESYLL_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("dElEtESYll 1 1 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Student} at the specified {@code index} in {@code model}'s TutorHelper.
     * @return the removed student
     */
    private Student deleteSyllStudent(Model model, Index studentIndex, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        Student studentTarget = getStudent(model, studentIndex);
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).remove(syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

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
    private void assertCommandSuccess(Index studentIndex, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        Model expectedModel = getModel();
        Student deletedSyllStudent = deleteSyllStudent(expectedModel, studentIndex, subjectIndex, syllabusIndex);
        String expectedResultMessage = String.format(MESSAGE_DELETESYLL_SUCCESS, deletedSyllStudent);
        assertCommandSuccess(DeleteSyllCommand.COMMAND_WORD
                + " " + studentIndex.getOneBased() + " " + subjectIndex.getOneBased()
                + " " + syllabusIndex.getOneBased(), expectedModel, expectedResultMessage, null);
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
            assertSelectedCardChanged();
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
