package tutorhelper.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.model.util.SubjectsUtil.createStudentWithNewSubjects;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.UserPrefs;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code AddSubCommand}.
 */
public class AddSubCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddSubCommand(null, null);
        new AddSubCommand(null, Subject.makeSubject("Physics"));
        new AddSubCommand(INDEX_FIRST_STUDENT, null);
    }

    @Test
    public void execute_validArgumentsUnfilteredList_success() {
        // Add Subject for first student
        Student student = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Subject subject = Subject.makeSubject("Physics");
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_STUDENT, subject);

        String expectedMessage = String.format(AddSubCommand.MESSAGE_ADDSUB_SUCCESS, student);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
        Student updatedStudent = simulateAddSubCommand(student, subject);

        expectedModel.updateStudent(student, updatedStudent);
        expectedModel.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidStudentIndexUnfilteredList_throwsCommandException() {
        // Command fails as index is out of bounds
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        AddSubCommand addSubCommand = new AddSubCommand(outOfBoundIndex, Subject.makeSubject("Physics"));

        assertCommandFailure(addSubCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validArgumentsFilteredList_success() {
        // Add Subject for first student
        Student student = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Subject subject = Subject.makeSubject("Physics");
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_STUDENT, subject);

        String expectedMessage = String.format(AddSubCommand.MESSAGE_ADDSUB_SUCCESS, student);
        Student updatedStudent = simulateAddSubCommand(student, subject);

        expectedModel.updateStudent(student, updatedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentIndexFilteredList_throwsCommandException() {
        // Command fails as index is out of bounds
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        AddSubCommand addSubCommand = new AddSubCommand(outOfBoundIndex, Subject.makeSubject("Physics"));

        assertCommandFailure(addSubCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidSubject_throwsCommandException() {
        // Empty Subject throws IllegalArgumentException
        thrown.expect(IllegalArgumentException.class);
        new AddSubCommand(INDEX_FIRST_STUDENT, Subject.makeSubject(" "));
    }

    @Test
    public void execute_duplicateSubject_throwsCommandException() {
        // Command fails as Subject already exists for student
        Student student = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Subject copiedSubject = Subject.makeSubject(new ArrayList<>(student.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased())
                .getSubjectName());
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_STUDENT, copiedSubject);

        assertCommandFailure(addSubCommand, model, commandHistory,
                String.format(AddSubCommand.MESSAGE_DUPLICATE_SUBJECT, student));
    }

    @Test
    public void executeUndoRedo_validArgumentsUnfilteredList_success() throws Exception {
        Student student = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Subject subject = Subject.makeSubject("Physics");
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_STUDENT, subject);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateAddSubCommand(student, subject);
        expectedModel.updateStudent(student, newStudent);
        expectedModel.commitTutorHelper();

        // AddSubCommand: Add Subject to first student
        addSubCommand.execute(model, commandHistory);

        // UndoCommand: Reverts TutorHelper back to previous state and removes filter from student list
        expectedModel.undoTutorHelper();
        expectedModel.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // RedoCommand: Adds Subject to first student again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidArgumentsUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        AddSubCommand addSubCommand = new AddSubCommand(outOfBoundIndex, Subject.makeSubject("Physics"));

        // Throws new CommandException
        assertCommandFailure(addSubCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        // Only one TutorHelper state in model, UndoCommand and RedoCommand fails
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        AddSubCommand addSubCommand1 = new AddSubCommand(INDEX_FIRST_STUDENT, Subject.makeSubject("Physics"));
        AddSubCommand addSubCommand2 = new AddSubCommand(INDEX_SECOND_STUDENT, Subject.makeSubject("Physics"));

        // Same Object: Equal
        assertEquals(addSubCommand1, addSubCommand1);

        // Same Values: Equal
        AddSubCommand addSubCommand1Copy = new AddSubCommand(INDEX_FIRST_STUDENT, Subject.makeSubject("Physics"));
        assertEquals(addSubCommand1, addSubCommand1Copy);

        // Different Types: Not Equal
        assertNotEquals(addSubCommand1, 1);

        // One Null Argument: Not Equal
        assertNotEquals(addSubCommand1, null);

        // Different Command: Not Equal
        assertNotEquals(addSubCommand1, addSubCommand2);
    }

    /**
     * Simulates and returns a new {@code Student} created by AddSubCommand.
     */
    private Student simulateAddSubCommand(Student student, Subject subject) {
        List<Subject> subjects = new ArrayList<>(student.getSubjects());
        subjects.add(subject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return createStudentWithNewSubjects(student, newSubjects);
    }

}
