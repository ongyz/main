package tutorhelper.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.logic.commands.CommandTestUtil.showStudentAtIndex;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
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
import tutorhelper.model.subject.Syllabus;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * AddSyllCommand.
 */
public class AddSyllCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullArguments_throwsNullPointerException() {
        List<Syllabus> syllabusList = new ArrayList<>();
        syllabusList.add(Syllabus.makeSyllabus("Mathematics"));
        thrown.expect(NullPointerException.class);
        new AddSyllCommand(null, null, null);
        new AddSyllCommand(null, INDEX_FIRST_SUBJECT, syllabusList);
        new AddSyllCommand(INDEX_FIRST_STUDENT, null, syllabusList);
        new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, null);
    }

    @Test
    public void execute_allValidArgumentsUnfilteredList_success() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);

        String expectedMessage = String.format(AddSyllCommand.MESSAGE_ADDSYLL_SUCCESS, studentTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
        Student newStudent = simulateAddSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, syllabusTest);

        expectedModel.updateStudentInternalField(studentTarget, newStudent);
        expectedModel.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        AddSyllCommand addSyllCommand = new AddSyllCommand(outOfBoundIndex,
                INDEX_FIRST_SUBJECT, syllabusTest);

        assertCommandFailure(addSyllCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(studentTarget.getSubjects().size() + 1);
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_STUDENT, outOfBoundIndex, syllabusTest);

        assertCommandFailure(addSyllCommand, model, commandHistory, MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);

        String expectedMessage = String.format(AddSyllCommand.MESSAGE_ADDSYLL_SUCCESS, studentTarget);
        Student updatedStudent = simulateAddSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, syllabusTest);
        expectedModel.updateStudentInternalField(studentTarget, updatedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of TutorHelper list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorHelper().getStudentList().size());
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        assertCommandFailure(addSyllCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidSubjectIndex_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(studentTarget.getSubjects().size() + 1);
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_STUDENT, outOfBoundIndex, syllabusTest);

        assertCommandFailure(addSyllCommand, model, commandHistory, MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidSyllabus_throwsCommandException() {
        thrown.expect(IllegalArgumentException.class);
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus(" "));
        new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);
    }

    @Test
    public void execute_invalidSyllabusInvalidChar_throwsCommandException() {
        thrown.expect(IllegalArgumentException.class);
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("Integration sy/Differentiation"));
        new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);
    }

    @Test
    public void execute_duplicateSyllabus_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("SyllabusCopy"));
        syllabusTest.add(Syllabus.makeSyllabus("SyllabusCopy"));
        AddSyllCommand addSyllCommand = new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);

        assertCommandFailure(addSyllCommand, model, commandHistory,
                String.format(AddSyllCommand.MESSAGE_DUPLICATE_SYLLABUS_IN_ARGUMENT, studentTarget));
    }

    @Test
    public void execute_duplicateSyllabusInArguments_throwsCommandException() {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        List<Syllabus> syllabusTest = new ArrayList<>();
        Syllabus syllabusCopy = Syllabus.makeSyllabus(new ArrayList<>(studentTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased())
                .getSubjectContent()
                .get(INDEX_FIRST_SYLLABUS.getZeroBased()).syllabus); //makes a copy of existing syllabus
        syllabusTest.add(syllabusCopy);
        AddSyllCommand addSyllCommand = new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);

        assertCommandFailure(addSyllCommand, model, commandHistory,
                String.format(AddSyllCommand.MESSAGE_DUPLICATE_SYLLABUS_IN_STUDENT, studentTarget));
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Student studentTarget = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllCommand = new AddSyllCommand(INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Student newStudent = simulateAddSyllCommand(studentTarget, INDEX_FIRST_SUBJECT, syllabusTest);
        expectedModel.updateStudentInternalField(studentTarget, newStudent);
        expectedModel.commitTutorHelper();

        // AddSyll -> first student has an added syllabus
        addSyllCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered student list to show all students
        expectedModel.undoTutorHelper();
        expectedModel.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first student deleted again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllCommand = new AddSyllCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(addSyllCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        List<Syllabus> syllabusTest = new ArrayList<>();
        syllabusTest.add(Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllFirstCommand = new AddSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);
        AddSyllCommand addSyllSecondCommand = new AddSyllCommand(
                INDEX_SECOND_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);

        // same object -> returns true
        assertEquals(addSyllFirstCommand, addSyllFirstCommand);

        // same values -> returns true
        AddSyllCommand addSyllFirstCommandCopy = new AddSyllCommand(
                INDEX_FIRST_STUDENT, INDEX_FIRST_SUBJECT, syllabusTest);
        assertEquals(addSyllFirstCommand, addSyllFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(addSyllFirstCommand, 1);

        // null -> returns false
        assertNotEquals(addSyllFirstCommand, null);

        // different command -> returns false
        assertNotEquals(addSyllFirstCommand, addSyllSecondCommand);
    }

    /**
     * Simulates and returns a new {@code Student} created by AddSyllCommand.
     */
    private Student simulateAddSyllCommand(Student studentTarget, Index subjectIndex, List<Syllabus> syllabuses) {
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased());
        for (Syllabus syllabus: syllabuses) {
            updatedSubject.add(syllabus);
        }
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return createStudentWithNewSubjects(studentTarget, newSubjects);
    }

}
