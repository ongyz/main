package systemtests;

import static org.junit.Assert.assertTrue;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static tutorhelper.logic.commands.CopySubCommand.MESSAGE_COPYSUB_SUCCESS;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static tutorhelper.model.util.SubjectsUtil.createStudentWithNewSubjects;
import static tutorhelper.model.util.SubjectsUtil.findSubjectIndex;
import static tutorhelper.model.util.SubjectsUtil.hasSubject;
import static tutorhelper.testutil.TestUtil.getLastIndex;
import static tutorhelper.testutil.TestUtil.getMidIndex;
import static tutorhelper.testutil.TestUtil.getStudent;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static tutorhelper.testutil.TypicalStudents.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.CopySubCommand;
import tutorhelper.logic.commands.RedoCommand;
import tutorhelper.logic.commands.UndoCommand;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;

public class CopySubCommandSystemTest extends TutorHelperSystemTest {

    private static final String MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE);

    @Test
    public void copysub() throws CommandException {
        /* ---------------- Performing copysub operation while an unfiltered list is being shown ------------------- */

        /* Case: copy the first subject of the first student in the list,
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        String command = "     " + CopySubCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_STUDENT.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " "
                + INDEX_SECOND_STUDENT.getOneBased() + "       ";

        Student newStudent = copySubStudent(
                expectedModel, INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);
        String expectedResultMessage = String.format(MESSAGE_COPYSUB_SUCCESS, newStudent);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: copy first subject the last student in the list to the first student on the list -> success */
        Model modelBeforeMarkingLast = getModel();
        Index lastStudentIndex = getLastIndex(modelBeforeMarkingLast);
        assertCommandSuccess(lastStudentIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_STUDENT);

        /* Case: undo command the last student in the list -> first student subject reverted */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: redo command the last student in the list -> first student subject restored again */
        command = RedoCommand.COMMAND_WORD;
        copySubStudent(modelBeforeMarkingLast, lastStudentIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: copy first subject of the middle student in the list to first student in the list -> success */
        Index middleStudentIndex = getMidIndex(getModel());
        assertCommandSuccess(middleStudentIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_STUDENT);

        /* ----------------- Performing copysub operation while a filtered list is being shown ------------------  */

        /* Case: filtered student list, student index within bounds of TutorHelper and student list -> success */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        Index studentIndex = INDEX_FIRST_STUDENT;
        assertTrue(studentIndex.getZeroBased() < expectedModel.getFilteredStudentList().size());
        assertCommandSuccess(studentIndex, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);

        /* Case: filtered student list, student index within bounds of TutorHelper but out of bounds of student list
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getStudentList().size();
        command = CopySubCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SUBJECT.getOneBased()
                + " " + INDEX_SECOND_STUDENT.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: filtered student list, student index within bounds but subject index is out of bounds
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getStudentList()
                .get(INDEX_FIRST_STUDENT.getZeroBased()).getSubjects().size() + 1;
        command = CopySubCommand.COMMAND_WORD + " " + INDEX_FIRST_STUDENT.getOneBased()
                + " " + invalidIndex
                + " " + INDEX_SECOND_STUDENT.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_SUBJECT_INDEX);

        /* ------------------------------- Performing invalid copySub operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = CopySubCommand.COMMAND_WORD + " 0 0 0";
        assertCommandFailure(command, MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = CopySubCommand.COMMAND_WORD + " -1 -1 -1";
        assertCommandFailure(command, MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getStudentList().size() + 1);
        command = CopySubCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 1 1";
        assertCommandFailure(command, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(
                CopySubCommand.COMMAND_WORD + " a b c", MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(
                CopySubCommand.COMMAND_WORD + " 1 a b c", MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("cOPYsub 1 1 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Student} at the specified {@code index} in {@code model}'s TutorHelper.
     * @return the removed student
     */
    private Student copySubStudent(Model model, Index sourceStudentIndex,
                                   Index subjectIndex, Index targetStudentIndex) {
        Student studentSource = getStudent(model, sourceStudentIndex);
        Student studentTarget = getStudent(model, targetStudentIndex);
        List<Subject> sourceSubjects = new ArrayList<>(studentSource.getSubjects());
        List<Subject> targetSubjects = new ArrayList<>(studentTarget.getSubjects());
        Subject selectedSubject = sourceSubjects.get(subjectIndex.getZeroBased());
        Set<Subject> updatedSubjects;

        if (hasSubject(studentTarget, selectedSubject.getSubjectType())) {
            Index index = findSubjectIndex(studentTarget, selectedSubject.getSubjectType()).get();
            Subject updatedSubject = targetSubjects.get(index.getZeroBased())
                    .append(selectedSubject.getSubjectContent());
            targetSubjects.set(index.getZeroBased(), updatedSubject);
            updatedSubjects = new HashSet<>(targetSubjects);
        } else {
            targetSubjects.add(selectedSubject);
            updatedSubjects = new HashSet<>(targetSubjects);
        }

        Set<Subject> newSubjects = new HashSet<>(updatedSubjects);
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
    private void assertCommandSuccess(Index sourceStudentIndex, Index subjectIndex, Index targetStudentIndex) {
        Model expectedModel = getModel();
        Student copySubStudent = copySubStudent(expectedModel, sourceStudentIndex, subjectIndex, targetStudentIndex);
        String expectedResultMessage = String.format(MESSAGE_COPYSUB_SUCCESS, copySubStudent);
        assertCommandSuccess(CopySubCommand.COMMAND_WORD
                + " " + sourceStudentIndex.getOneBased() + " " + subjectIndex.getOneBased()
                + " " + targetStudentIndex.getOneBased(), expectedModel, expectedResultMessage);
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
