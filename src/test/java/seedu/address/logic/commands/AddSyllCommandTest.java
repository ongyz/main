package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalTutorHelper;

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
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.Syllabus;
import seedu.address.model.util.SubjectsUtil;

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
        thrown.expect(NullPointerException.class);
        new AddSyllCommand(null, null, null);
        new AddSyllCommand(null, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("Mathematics"));
        new AddSyllCommand(INDEX_FIRST_PERSON, null, Syllabus.makeSyllabus("Mathematics"));
        new AddSyllCommand(INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, null);
    }

    @Test
    public void execute_allValidArgumentsUnfilteredList_success() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Syllabus syllabusTest = Syllabus.makeSyllabus("AddSyllTestSyllabus");
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, syllabusTest);

        String expectedMessage = String.format(AddSyllCommand.MESSAGE_ADDSYLL_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
        Person newPerson = simulateAddSyllCommand(personTarget, INDEX_FIRST_SUBJECT, syllabusTest);

        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddSyllCommand addSyllCommand = new AddSyllCommand(outOfBoundIndex,
                INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("AddSyllTestSyllabus"));

        assertCommandFailure(addSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, Syllabus.makeSyllabus("AddSyllTestSyllabus"));

        assertCommandFailure(addSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Syllabus syllabusTest = Syllabus.makeSyllabus("AddSyllTestSyllabus");
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, syllabusTest);

        String expectedMessage = String.format(AddSyllCommand.MESSAGE_ADDSYLL_SUCCESS, personTarget);
        Person updatedPerson = simulateAddSyllCommand(personTarget, INDEX_FIRST_SUBJECT, syllabusTest);
        expectedModel.updatePerson(personTarget, updatedPerson);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorHelper().getPersonList().size());
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("AddSyllTestSyllabus"));

        assertCommandFailure(addSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidSubjectIndex_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, Syllabus.makeSyllabus("AddSyllTestSyllabus"));

        assertCommandFailure(addSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidSyllabus_throwsCommandException() {
        thrown.expect(IllegalArgumentException.class);
        new AddSyllCommand(INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus(" "));
    }

    @Test
    public void execute_duplicateSyllabus_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Syllabus syllabusCopy = Syllabus.makeSyllabus(new ArrayList<>(personTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased())
                .getSubjectContent()
                .get(INDEX_FIRST_SYLLABUS.getZeroBased()).syllabus); //makes a copy of existing syllabus
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, syllabusCopy);

        assertCommandFailure(addSyllCommand, model, commandHistory,
                String.format(AddSyllCommand.MESSAGE_DUPLICATE_SYLLABUS, personTarget));
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Syllabus syllabusTest = Syllabus.makeSyllabus("AddSyllTestSyllabus");
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, syllabusTest);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Person newPerson = simulateAddSyllCommand(personTarget, INDEX_FIRST_SUBJECT, syllabusTest);
        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitTutorHelper();

        // AddSyll -> first person has an added syllabus
        addSyllCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered person list to show all persons
        expectedModel.undoTutorHelper();
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person deleted again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddSyllCommand addSyllCommand = new AddSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("AddSyllTestSyllabus"));

        // execution failed -> address book state not added into model
        assertCommandFailure(addSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        AddSyllCommand addSyllFirstCommand = new AddSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        AddSyllCommand addSyllSecondCommand = new AddSyllCommand(
                INDEX_SECOND_PERSON, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("AddSyllTestSyllabus"));

        // same object -> returns true
        assertEquals(addSyllFirstCommand, addSyllFirstCommand);

        // same values -> returns true
        AddSyllCommand addSyllFirstCommandCopy = new AddSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, Syllabus.makeSyllabus("AddSyllTestSyllabus"));
        assertEquals(addSyllFirstCommand, addSyllFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(addSyllFirstCommand, 1);

        // null -> returns false
        assertNotEquals(addSyllFirstCommand, null);

        // different command -> returns false
        assertNotEquals(addSyllFirstCommand, addSyllSecondCommand);
    }

    /**
     * Simulates and returns a new {@code Person} created by AddSyllCommand.
     */
    private Person simulateAddSyllCommand(Person personTarget, Index subjectIndex, Syllabus syllabus) {
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).add(syllabus);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return SubjectsUtil.createPersonWithNewSubjects(personTarget, newSubjects);
    }

}
