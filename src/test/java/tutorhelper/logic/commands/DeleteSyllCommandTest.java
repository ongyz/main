package tutorhelper.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SYLLABUS_INDEX;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.logic.commands.CommandTestUtil.showStudentAtIndex;
import static tutorhelper.model.util.SubjectsUtil.createStudentWithNewSubjects;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.UserPrefs;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteSyllCommand}.
 */
public class DeleteSyllCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexesUnfilteredList_success() throws CommandException {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        String expectedMessage = String.format(DeleteSyllCommand.MESSAGE_DELETESYLL_SUCCESS, studentTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateDeleteSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        expectedModel.updateStudentInternalField(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(outOfBoundIndex,
                INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory,
                MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(studentTarget.getSubjects().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, outOfBoundIndex, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidIndexSyllabusUnfilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(studentTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, outOfBoundIndex);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() throws CommandException {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        String expectedMessage = String.format(DeleteSyllCommand.MESSAGE_DELETESYLL_SUCCESS, studentTarget);
        Student updatedStudent = simulateDeleteSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedModel.updateStudentInternalField(studentTarget, updatedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of TutorHelper list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorHelper().getStudentList().size());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory,
                MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidIndexSubjectFilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(studentTarget.getSubjects().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, outOfBoundIndex, INDEX_FIRST_SYLLABUS);

        assertCommandFailure(deleteSyllCommand, model, commandHistory, MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidIndexSyllabusFilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(studentTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, outOfBoundIndex);
        assertCommandFailure(deleteSyllCommand, model, commandHistory, MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateDeleteSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedModel.updateStudentInternalField(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        // DeleteSyll -> first student syllabus is erased
        deleteSyllCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered student list to show all students
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first student deleted again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        DeleteSyllCommand deleteSyllCommand = new DeleteSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(deleteSyllCommand, model, commandHistory,
                MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        DeleteSyllCommand deleteSyllFirstCommand = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        DeleteSyllCommand deleteSyllSecondCommand = new DeleteSyllCommand(
                INDEX_SECOND_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        // same object -> returns true
        assertEquals(deleteSyllFirstCommand, deleteSyllFirstCommand);

        // same values -> returns true
        DeleteSyllCommand deleteSyllFirstCommandCopy = new DeleteSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        assertEquals(deleteSyllFirstCommand, deleteSyllFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(deleteSyllFirstCommand, 1);

        // null -> returns false
        assertNotEquals(deleteSyllFirstCommand, null);

        // different command -> returns false
        assertNotEquals(deleteSyllFirstCommand, deleteSyllSecondCommand);
    }

    /**
     * Simulates and returns a new {@code Student} created by DeleteSyllCommand.
     */
    private Student simulateDeleteSyllCommand(Student studentTarget, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).remove(syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return createStudentWithNewSubjects(studentTarget, newSubjects);
    }

}
