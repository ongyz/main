package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DUPLICATE_SYLLABUS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SYLLABUS_DIFFERENTIATION;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SYLLABUS;
import static seedu.address.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.Syllabus;
import seedu.address.model.util.SubjectsUtil;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code EditSyllCommand}.
 */
public class EditSyllCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexesUnfilteredList_success() throws CommandException {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));
        String expectedMessage = String.format(EditSyllCommand.MESSAGE_EDITSYLL_SUCCESS, studentTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
        Student newStudent = simulateEditSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS,
                new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        expectedModel.updateStudentInternalField(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(editSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(studentTarget.getSubjects().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, outOfBoundIndex,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_SUBJECT_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndexSyllabusUnfilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(studentTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                outOfBoundIndex, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() throws CommandException {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
        String expectedMessage = String.format(EditSyllCommand.MESSAGE_EDITSYLL_SUCCESS, studentTarget);
        Student updatedStudent = simulateEditSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS,
                new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        expectedModel.updateStudentInternalField(studentTarget, updatedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(editSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of TutorHelper list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorHelper().getStudentList().size());
        EditSyllCommand editSyllCommand = new EditSyllCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidIndexSubjectFilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(studentTarget.getSubjects().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, outOfBoundIndex,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_SUBJECT_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndexSyllabusFilteredList_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(studentTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                outOfBoundIndex, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void execute_duplicateSyllabus_throwsCommandException() {
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(DUPLICATE_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_DUPLICATE_SYLLABUS);
    }

    @Test
    public void execute_invalidSyllabus_throwsCommandException() {
        thrown.expect(IllegalArgumentException.class);
        new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, Syllabus.makeSyllabus(" "));
    }

    @Test
    public void execute_invalidSyllabusExceedLength_throwsCommandException() {
        thrown.expect(IllegalArgumentException.class);
        new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS,
                Syllabus.makeSyllabus("Thisisasyllabusthatexceedsthirtycharactersanditshouldfail"));
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateEditSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS,
                new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        expectedModel.updateStudentInternalField(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        // DeleteSyll -> first student syllabus is erased
        editSyllCommand.execute(model, commandHistory);

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
        EditSyllCommand editSyllCommand = new EditSyllCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void executeUndoRedo_duplicate_failure() {
        EditSyllCommand editSyllCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(DUPLICATE_SYLLABUS, true));

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_DUPLICATE_SYLLABUS);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        EditSyllCommand editSyllFirstCommand = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));
        EditSyllCommand editSyllSecondCommand = new EditSyllCommand(INDEX_SECOND_STUDENT, INDEX_SECOND_SUBJECT,
                INDEX_SECOND_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));
        EditSyllCommand editSyllThirdCommand = new EditSyllCommand(INDEX_SECOND_STUDENT, INDEX_SECOND_SUBJECT,
                INDEX_SECOND_SYLLABUS, new Syllabus(DUPLICATE_SYLLABUS, true));

        // same object -> returns true
        assertEquals(editSyllFirstCommand, editSyllFirstCommand);

        // same values -> returns true
        EditSyllCommand editSyllFirstCommandCopy = new EditSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT,
                INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS_DIFFERENTIATION, true));
        assertEquals(editSyllFirstCommand, editSyllFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(editSyllFirstCommand, 1);

        // null -> returns false
        assertNotEquals(editSyllFirstCommand, null);

        // different index command -> returns false
        assertNotEquals(editSyllFirstCommand, editSyllSecondCommand);

        // different syllabus command -> returns false
        assertNotEquals(editSyllSecondCommand, editSyllThirdCommand);

        // different index and syllabus command -> returns false
        assertNotEquals(editSyllFirstCommand, editSyllThirdCommand);
    }

    /**
     * Simulates and returns a new {@code Student} created by EditSyllCommand.
     */
    private Student simulateEditSyllCommand(Student studentTarget, Index subjectIndex, Index syllabusIndex,
                                            Syllabus syllabus) throws CommandException {
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).edit(syllabus, syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return SubjectsUtil.createStudentWithNewSubjects(studentTarget, newSubjects);
    }

}
