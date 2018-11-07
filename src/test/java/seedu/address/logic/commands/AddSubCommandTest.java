package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
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
import seedu.address.model.util.SubjectsUtil;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * AddSubCommand.
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
        new AddSubCommand(INDEX_FIRST_PERSON, null);
    }

    @Test
    public void execute_allValidArgumentsUnfilteredList_success() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Subject subjectTest = Subject.makeSubject("Physics");
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_PERSON, subjectTest);

        String expectedMessage = String.format(AddSubCommand.MESSAGE_ADDSUB_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
        Person updatedPerson = simulateAddSubCommand(personTarget, subjectTest);

        expectedModel.updatePerson(personTarget, updatedPerson);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidStudentIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddSubCommand addSubCommand = new AddSubCommand(outOfBoundIndex, Subject.makeSubject("Physics"));

        assertCommandFailure(addSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Subject subjectTest = Subject.makeSubject("Physics");
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_PERSON, subjectTest);

        String expectedMessage = String.format(AddSubCommand.MESSAGE_ADDSUB_SUCCESS, personTarget);
        Person updatedPerson = simulateAddSubCommand(personTarget, subjectTest);

        expectedModel.updatePerson(personTarget, updatedPerson);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(addSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentIndexFilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddSubCommand addSubCommand = new AddSubCommand(outOfBoundIndex, Subject.makeSubject("Physics"));

        assertCommandFailure(addSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidSubject_throwsCommandException() {
        thrown.expect(IllegalArgumentException.class);
        new AddSubCommand(INDEX_FIRST_PERSON, Subject.makeSubject(" "));
    }

    @Test
    public void execute_duplicateSubject_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Subject subjectCopy = Subject.makeSubject(new ArrayList<>(personTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased())
                .getSubjectName());
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_PERSON, subjectCopy);

        assertCommandFailure(addSubCommand, model, commandHistory,
                String.format(AddSubCommand.MESSAGE_DUPLICATE_SUBJECT, personTarget));
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Subject subjectTest = Subject.makeSubject("Physics");
        AddSubCommand addSubCommand = new AddSubCommand(INDEX_FIRST_PERSON, subjectTest);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Person newPerson = simulateAddSubCommand(personTarget, subjectTest);
        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitTutorHelper();

        // AddSub -> first person has an added subject
        addSubCommand.execute(model, commandHistory);

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
        AddSubCommand addSubCommand = new AddSubCommand(outOfBoundIndex, Subject.makeSubject("Physics"));

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(addSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        AddSubCommand addSubCommand1 = new AddSubCommand(INDEX_FIRST_PERSON, Subject.makeSubject("Physics"));
        AddSubCommand addSubCommand2 = new AddSubCommand(INDEX_SECOND_PERSON, Subject.makeSubject("Physics"));

        // same object -> returns true
        assertEquals(addSubCommand1, addSubCommand1);

        // same values -> returns true
        AddSubCommand addSubCommand1Copy = new AddSubCommand(INDEX_FIRST_PERSON, Subject.makeSubject("Physics"));
        assertEquals(addSubCommand1, addSubCommand1Copy);

        // different types -> returns false
        assertNotEquals(addSubCommand1, 1);

        // null -> returns false
        assertNotEquals(addSubCommand1, null);

        // different command -> returns false
        assertNotEquals(addSubCommand1, addSubCommand2);
    }

    /**
     * Simulates and returns a new {@code Person} created by AddSubCommand.
     */
    private Person simulateAddSubCommand(Person studentTarget, Subject subject) {
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());
        subjects.add(subject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return SubjectsUtil.createPersonWithNewSubjects(studentTarget, newSubjects);
    }

}
