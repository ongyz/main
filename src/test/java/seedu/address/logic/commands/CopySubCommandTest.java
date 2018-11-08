package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalStudents.getDifferentSubjectStudentIndexes;
import static seedu.address.testutil.TypicalStudents.getSameSubjectStudentsIndexes;
import static seedu.address.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code CopySubCommand}.
 */
public class CopySubCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexesUnfilteredList_success() {
        Student studentSource = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Student studentTarget = model.getFilteredStudentList().get(INDEX_SECOND_STUDENT.getZeroBased());
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);

        String expectedMessage = String.format(CopySubCommand.MESSAGE_COPYSUB_SUCCESS, studentTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateCopySubCommand(studentSource, INDEX_FIRST_SUBJECT, studentTarget);

        expectedModel.updateStudent(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(copySubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        CopySubCommand copySubCommand = new CopySubCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        copySubCommand = new CopySubCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, outOfBoundIndex);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_sameStudentUnfilteredList_throwsCommandException() {
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_STUDENT);
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        assertCommandFailure(copySubCommand, model, commandHistory,
                String.format(CopySubCommand.MESSAGE_COPYSUB_FAILED_SAME_STUDENT, studentTarget));
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(studentTarget.getSubjects().size() + 1);
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_STUDENT, outOfBoundIndex, INDEX_SECOND_STUDENT);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of TutorHelper list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorHelper().getStudentList().size());
        CopySubCommand copySubCommand = new CopySubCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_STUDENT);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_hasExistingSubject_success() {
        List<Index> sameSubjectIndex = getSameSubjectStudentsIndexes();

        Index firstIndex = sameSubjectIndex.get(0);
        Index secondIndex = sameSubjectIndex.get(1);

        Student studentSource = model.getFilteredStudentList().get(firstIndex.getZeroBased());
        Student studentTarget = model.getFilteredStudentList().get(secondIndex.getZeroBased());

        CopySubCommand copySubCommand = new CopySubCommand(
                firstIndex, INDEX_FIRST_SUBJECT, secondIndex);

        String expectedMessage = String.format(CopySubCommand.MESSAGE_COPYSUB_SUCCESS, studentTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateCopySubCommand(studentSource, INDEX_FIRST_SUBJECT, studentTarget);

        expectedModel.updateStudent(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        // No new subject should be created
        assertEquals(studentTarget.getSubjects().size(), newStudent.getSubjects().size());
        assertCommandSuccess(copySubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addNewSubject_success() {
        List<Index> sameSubjectIndex = getDifferentSubjectStudentIndexes();

        Index firstIndex = sameSubjectIndex.get(0);
        Index secondIndex = sameSubjectIndex.get(1);

        Student studentSource = model.getFilteredStudentList().get(firstIndex.getZeroBased());
        Student studentTarget = model.getFilteredStudentList().get(secondIndex.getZeroBased());

        CopySubCommand copySubCommand = new CopySubCommand(
                firstIndex, INDEX_FIRST_SUBJECT, secondIndex);

        String expectedMessage = String.format(CopySubCommand.MESSAGE_COPYSUB_SUCCESS, studentTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateCopySubCommand(studentSource, INDEX_FIRST_SUBJECT, studentTarget);

        expectedModel.updateStudent(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        // New subject should be created
        assertNotEquals(studentTarget.getSubjects().size(), newStudent.getSubjects().size());

        expectedModel.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        assertCommandSuccess(copySubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Student studentSource = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Student studentTarget = model.getFilteredStudentList().get(INDEX_SECOND_STUDENT.getZeroBased());
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateCopySubCommand(studentSource, INDEX_FIRST_SUBJECT, studentTarget);

        expectedModel.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        expectedModel.updateStudent(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        // CopySub -> first student syllabus is erased
        copySubCommand.execute(model, commandHistory);

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
        CopySubCommand copySubCommand = new CopySubCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        CopySubCommand copySubFirstCommand = new CopySubCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);
        CopySubCommand copySubSecondCommand = new CopySubCommand(
                INDEX_SECOND_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_STUDENT);

        // same object -> returns true
        assertEquals(copySubFirstCommand, copySubFirstCommand);

        // same values -> returns true
        CopySubCommand copySubFirstCommandCopy = new CopySubCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_SECOND_STUDENT);
        assertEquals(copySubFirstCommand, copySubFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(copySubFirstCommand, 1);

        // null -> returns false
        assertNotEquals(copySubFirstCommand, null);

        // different command -> returns false
        assertNotEquals(copySubFirstCommand, copySubSecondCommand);
    }

    /**
     * Simulates and returns a new {@code Student} created by CopySubCommand.
     */
    private Student simulateCopySubCommand(Student studentSource, Index subjectIndex, Student studentTarget) {
        List<Subject> sourceSubjects = new ArrayList<>(studentSource.getSubjects());
        List<Subject> targetSubjects = new ArrayList<>(studentTarget.getSubjects());
        Subject selectedSubject = sourceSubjects.get(subjectIndex.getZeroBased());

        Set<Subject> updatedSubjects;

        if (SubjectsUtil.hasSubject(studentTarget, selectedSubject.getSubjectType())) {
            Index index = SubjectsUtil.findSubjectIndex(studentTarget, selectedSubject.getSubjectType()).get();

            Subject updatedSubject = targetSubjects.get(index.getZeroBased())
                    .append(selectedSubject.getSubjectContent());
            targetSubjects.set(index.getZeroBased(), updatedSubject);
            updatedSubjects = new HashSet<>(targetSubjects);
        } else {
            targetSubjects.add(selectedSubject);
            updatedSubjects = new HashSet<>(targetSubjects);
        }

        return SubjectsUtil.createStudentWithNewSubjects(studentTarget, updatedSubjects);
    }

}
