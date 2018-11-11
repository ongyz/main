package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.AddSyllCommand.MESSAGE_ADDSYLL_SUCCESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYLLABUS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TestUtil.getStudent;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalStudents.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddSyllCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.Syllabus;
import seedu.address.model.util.SubjectsUtil;

public class AddSyllCommandSystemTest extends TutorHelperSystemTest {

    public static final String ADD_SYLLABUS_STRING = "AddSyllCommandTest";

    private static final String MESSAGE_INVALID_ADDSYLL_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE);

    @Test
    public void addsyll() {
        /* -------------- Performing addsyll operation while an unfiltered list is being shown ------------------*/

        /* Case: append the first subject of the first student in the list with a new syllabus
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        Syllabus syllabusTest = Syllabus.makeSyllabus("AddSyllCommandSystemTest");
        assertCommandSuccess(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);

        /* Case: append first subject the last student in the list -> success */
        Model modelBeforeAppendLast = getModel();
        Index lastStudentIndex = getLastIndex(modelBeforeAppendLast);
        assertCommandSuccess(lastStudentIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        /* Case: undo command the last student in the list -> first student subject reverted */
        String command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* Case: redo command the last student in the list -> first student subject restored again */
        command = RedoCommand.COMMAND_WORD;
        addSyllStudent(modelBeforeAppendLast, lastStudentIndex, INDEX_FIRST_SUBJECT, syllabusTest);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* Case: append to the first subject of the middle student in the list -> success */
        Index middleStudentIndex = getMidIndex(getModel());
        assertCommandSuccess(middleStudentIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        /* ----------------- Performing addsyll operation while a filtered list is being shown ------------------  */

        /* Case: filtered student list, student index within bounds of TutorHelper and student list -> success */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        Index studentIndex = INDEX_FIRST_STUDENT;
        assertTrue(studentIndex.getZeroBased() < expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(studentIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        /* Case: filtered student list, student index within bounds of TutorHelper but out of bounds of student list
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getStudentList().size();
        command = AddSyllCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SUBJECT.getOneBased()
                + " " + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: filtered student list, student index within bounds but subject index is out of bounds
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getStudentList()
                .get(INDEX_FIRST_STUDENT.getZeroBased()).getSubjects().size() + 1;
        command = AddSyllCommand.COMMAND_WORD + " " + INDEX_FIRST_STUDENT.getOneBased()
                + " " + invalidIndex
                + " " + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING;
        assertCommandFailure(command, Messages.MESSAGE_INVALID_SUBJECT_INDEX);

        /* ------------------------------- Performing invalid addsyll operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = AddSyllCommand.COMMAND_WORD + " 0 0 " + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_ADDSYLL_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = AddSyllCommand.COMMAND_WORD + " -1 -1 " + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_ADDSYLL_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getStudentList().size() + 1);
        command = AddSyllCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased()
                + " " + INDEX_FIRST_SUBJECT.getOneBased() + " " + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(AddSyllCommand.COMMAND_WORD + " a b "
                + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING, MESSAGE_INVALID_ADDSYLL_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(AddSyllCommand.COMMAND_WORD + " 1 1 1 "
                + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING, MESSAGE_INVALID_ADDSYLL_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("aDdsyLL 1 1 "
                + PREFIX_SYLLABUS + ADD_SYLLABUS_STRING, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Appends a syllabus topic {@code syllabus} to the{@code Subjcct} at the specified {@code subjectIndex} for the
     * {@code Student} at the specified {@code targetStudentIndex} in {@code model}'s TutorHelper.
     * @return the updated student
     */
    private Student addSyllStudent(Model model, Index targetStudentIndex, Index subjectIndex, Syllabus syllabus) {
        Student studentTarget = getStudent(model, targetStudentIndex);
        List<Subject> targetSubjects = new ArrayList<>(studentTarget.getSubjects());

        Subject updatedSubject = targetSubjects.get(subjectIndex.getZeroBased()).add(syllabus);
        targetSubjects.set(subjectIndex.getZeroBased(), updatedSubject);
        Set<Subject> newSubjects = new HashSet<>(targetSubjects);

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
    private void assertCommandSuccess(Index sourceStudentIndex, Index subjectIndex, Syllabus syllabus) {
        Model expectedModel = getModel();
        Student studentTarget = expectedModel.getFilteredStudentList().get(sourceStudentIndex.getZeroBased());
        String command = AddSyllCommand.COMMAND_WORD
                + " " + sourceStudentIndex.getOneBased()
                + " " + subjectIndex.getOneBased()
                + " " + PREFIX_SYLLABUS + syllabus.syllabus;
        Student newStudent = addSyllStudent(
                expectedModel, sourceStudentIndex, subjectIndex, syllabus);
        expectedModel.updateStudentInternalField(studentTarget, newStudent);
        String expectedResultMessage = String.format(MESSAGE_ADDSYLL_SUCCESS, newStudent);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
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
