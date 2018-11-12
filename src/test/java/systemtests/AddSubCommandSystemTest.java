package systemtests;

import static org.junit.Assert.assertTrue;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tutorhelper.logic.commands.AddSubCommand.MESSAGE_ADDSUB_SUCCESS;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static tutorhelper.model.util.SubjectsUtil.createStudentWithNewSubjects;
import static tutorhelper.testutil.TestUtil.getLastIndex;
import static tutorhelper.testutil.TestUtil.getStudent;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalStudents.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.AddSubCommand;
import tutorhelper.logic.commands.RedoCommand;
import tutorhelper.logic.commands.UndoCommand;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;

public class AddSubCommandSystemTest extends TutorHelperSystemTest {

    public static final String ADDSUB_TEST_SUBJECT = "Physics";

    private static final String MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSubCommand.MESSAGE_USAGE);

    @Test
    public void addsub() {
        /* -------------- Performing addsub operation while an unfiltered list is being shown ------------------*/

        /* Case: add a subject to the first student in the list
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        String command = "     " + AddSubCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_STUDENT.getOneBased() + " "
                + PREFIX_SUBJECT + "Physics       ";
        Subject subjectTest = Subject.makeSubject("Physics");
        Student newStudent = addSubStudent(
                expectedModel, INDEX_FIRST_STUDENT, subjectTest);
        String expectedResultMessage = String.format(MESSAGE_ADDSUB_SUCCESS, newStudent);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: add subject to the last student in the list -> success */
        Model modelBeforeAppendLast = getModel();
        Index lastStudentIndex = getLastIndex(modelBeforeAppendLast);
        assertCommandSuccess(lastStudentIndex, subjectTest);

        /* Case: undo command the last student in the list -> first student subject reverted */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* Case: redo command the last student in the list -> first student subject restored again */
        command = RedoCommand.COMMAND_WORD;
        addSubStudent(modelBeforeAppendLast, lastStudentIndex, subjectTest);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* ----------------- Performing addsub operation while a filtered list is being shown ------------------  */

        /* Case: filtered student list, student index within bounds of TutorHelper and student list -> success */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        Index studentIndex = INDEX_FIRST_STUDENT;
        assertTrue(studentIndex.getZeroBased() < expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(studentIndex, subjectTest);

        /* Case: filtered student list, student index within bounds of TutorHelper but out of bounds of student list
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getStudentList().size();
        command = AddSubCommand.COMMAND_WORD + " " + invalidIndex
                + " " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* ------------------------------- Performing invalid addsub operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = AddSubCommand.COMMAND_WORD + " 0 " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = AddSubCommand.COMMAND_WORD + " -1 " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getStudentList().size() + 1);
        command = AddSubCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased()
                + " " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(AddSubCommand.COMMAND_WORD + " a "
                + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(AddSubCommand.COMMAND_WORD + " 1 1 "
                + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("aDdSuB 1 "
                + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Adds a subject {@code subject} to the {@code Student}
     * at the specified {@code index} in {@code model}'s TutorHelper.
     * @return the removed student
     */
    private Student addSubStudent(Model model, Index targetStudentIndex, Subject subject) {
        Student studentTarget = getStudent(model, targetStudentIndex);
        List<Subject> targetSubjects = new ArrayList<>(studentTarget.getSubjects());
        targetSubjects.add(subject);

        Set<Subject> newSubjects = new HashSet<>(targetSubjects);

        Student studentUpdated = createStudentWithNewSubjects(studentTarget, newSubjects);
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
    private void assertCommandSuccess(Index sourceStudentIndex, Subject subject) {
        Model expectedModel = getModel();
        String command = AddSubCommand.COMMAND_WORD
                + " " + sourceStudentIndex.getOneBased()
                + " " + PREFIX_SUBJECT + subject.getSubjectName();
        Student newStudent = addSubStudent(
                expectedModel, sourceStudentIndex, subject);
        String expectedResultMessage = String.format(MESSAGE_ADDSUB_SUCCESS, newStudent);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
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
