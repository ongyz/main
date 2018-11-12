package tutorhelper.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.logic.commands.DeleteSubCommand.MESSAGE_DELETE_ONLY_SUBJECT;
import static tutorhelper.model.util.SubjectsUtil.createStudentWithNewSubjects;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.UserPrefs;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteSubCommand}.
 */
public class DeleteSubCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validArgumentsUnfilteredList_success() {
        // Deletes first Subject from third student
        Student student = model.getFilteredStudentList().get(INDEX_THIRD_STUDENT.getZeroBased());
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT);

        String expectedMessage = String.format(DeleteSubCommand.MESSAGE_DELETESUB_SUCCESS, student);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateDeleteSubCommand(student, INDEX_FIRST_SUBJECT);

        expectedModel.updateStudent(student, newStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentIndexUnfilteredList_throwsCommandException() {
        // Command fails as student index is out of bounds
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT);

        assertCommandFailure(deleteSubCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidSubjectIndexUnfilteredList_throwsCommandException() {
        // Command fails as Subject index is out of bounds
        Student student = model.getFilteredStudentList().get(INDEX_THIRD_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(student.getSubjects().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_STUDENT, outOfBoundIndex);

        assertCommandFailure(deleteSubCommand, model, commandHistory, MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_validArgumentsFilteredList_success() {
        // Deletes first Subject from third student
        Student student = model.getFilteredStudentList().get(INDEX_THIRD_STUDENT.getZeroBased());
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT);

        String expectedMessage = String.format(DeleteSubCommand.MESSAGE_DELETESUB_SUCCESS, student);
        Student updatedStudent = simulateDeleteSubCommand(student, INDEX_FIRST_SUBJECT);
        expectedModel.updateStudent(student, updatedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentIndexFilteredList_throwsCommandException() {
        // Command fails as student index is out of bounds
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT);

        assertCommandFailure(deleteSubCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidSubjectIndexFilteredList_throwsCommandException() {
        // Command fails as Subject index is out of bounds
        Student student = model.getFilteredStudentList().get(INDEX_THIRD_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(student.getSubjects().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_STUDENT, outOfBoundIndex);

        assertCommandFailure(deleteSubCommand, model, commandHistory, MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void executeUndoRedo_validArgumentsUnfilteredList_success() throws Exception {
        Student student = model.getFilteredStudentList().get(INDEX_THIRD_STUDENT.getZeroBased());
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateDeleteSubCommand(student, INDEX_FIRST_SUBJECT);
        expectedModel.updateStudent(student, newStudent);
        expectedModel.commitTutorHelper();

        // DeleteSubCommand: Deletes first Subject from third student
        deleteSubCommand.execute(model, commandHistory);

        // UndoCommand: Reverts TutorHelper back to previous state and removes filter from student list
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // RedoCommand: Deletes first Subject to third student again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT);

        // Throws new CommandException
        assertCommandFailure(deleteSubCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        // Only one TutorHelper state in model, UndoCommand and RedoCommand fails
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_deleteOnlySubject_throwsCommandException() {
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT);

        assertCommandFailure(deleteSubCommand, model, commandHistory,
                String.format(MESSAGE_DELETE_ONLY_SUBJECT,
                        model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased())));
    }

    @Test
    public void equals() {
        DeleteSubCommand deleteSubCommand1 = new DeleteSubCommand(INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT);
        DeleteSubCommand deleteSubCommand2 = new DeleteSubCommand(INDEX_THIRD_STUDENT, INDEX_SECOND_SUBJECT);

        // Same Object: Equal
        assertEquals(deleteSubCommand1, deleteSubCommand1);

        // Same Values: Equal
        DeleteSubCommand deleteSubCommand1Copy = new DeleteSubCommand(INDEX_THIRD_STUDENT, INDEX_FIRST_SUBJECT);
        assertEquals(deleteSubCommand1, deleteSubCommand1Copy);

        // Different Types: Not Equal
        assertNotEquals(deleteSubCommand1, 1);

        // One Null Argument: Not Equal
        assertNotEquals(deleteSubCommand1, null);

        // Different Command: Not Equal
        assertNotEquals(deleteSubCommand1, deleteSubCommand2);
    }

    /**
     * Simulates and returns a new {@code Student} created by DeleteSubCommand.
     */
    private Student simulateDeleteSubCommand(Student student, Index subjectIndex) {
        List<Subject> subjects = new ArrayList<>(student.getSubjects());
        subjects.remove(subjectIndex.getZeroBased());

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return createStudentWithNewSubjects(student, newSubjects);
    }

}
