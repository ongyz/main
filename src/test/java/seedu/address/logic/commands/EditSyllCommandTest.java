package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DUPLICATE_SYLLABUS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SYLLABUS;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SYLLABUS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.Syllabus;
import seedu.address.model.util.SubjectsUtil;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditSyllCommand.
 */
public class EditSyllCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexesUnfilteredList_success() throws CommandException {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));
        String expectedMessage = String.format(EditSyllCommand.MESSAGE_EDITSYLL_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Person newPerson = simulateEditSyllCommand(personTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS,
                new Syllabus(VALID_SYLLABUS, true));
        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_SUBJECT_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndexSyllabusUnfilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(personTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, outOfBoundIndex, new Syllabus(VALID_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() throws CommandException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(EditSyllCommand.MESSAGE_EDITSYLL_SUCCESS, personTarget);
        Person updatedPerson = simulateEditSyllCommand(personTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS,
                new Syllabus(VALID_SYLLABUS, true));
        expectedModel.updatePerson(personTarget, updatedPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(editSyllCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidIndexSubjectFilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_SUBJECT_NOT_FOUND);
    }

    @Test
    public void execute_invalidIndexSyllabusFilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(new ArrayList<>(personTarget.getSubjects())
                .get(INDEX_FIRST_SUBJECT.getZeroBased()).getSubjectContent().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, outOfBoundIndex, new Syllabus(VALID_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
    }

    @Test
    public void execute_duplicateSyllabus_throwsCommandException() {
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(DUPLICATE_SYLLABUS, true));

        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_DUPLICATE_SYLLABUS);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person newPerson = simulateEditSyllCommand(personTarget, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS,
                new Syllabus(VALID_SYLLABUS, true));
        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitAddressBook();

        // EraseSyll -> first person syllabus is erased
        editSyllCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person deleted again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));

        // execution failed -> address book state not added into model
        assertCommandFailure(editSyllCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void executeUndoRedo_duplicate_failure() {
        EditSyllCommand editSyllCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(DUPLICATE_SYLLABUS, true));

        // execution failed -> address book state not added into model
        assertCommandFailure(editSyllCommand, model, commandHistory, EditSyllCommand.MESSAGE_DUPLICATE_SYLLABUS);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        EditSyllCommand editSyllFirstCommand = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));
        EditSyllCommand editSyllSecondCommand = new EditSyllCommand(
                INDEX_SECOND_PERSON, INDEX_SECOND_SUBJECT, INDEX_SECOND_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));
        EditSyllCommand editSyllThirdCommand = new EditSyllCommand(
                INDEX_SECOND_PERSON, INDEX_SECOND_SUBJECT, INDEX_SECOND_SYLLABUS, new Syllabus(DUPLICATE_SYLLABUS, true));

        // same object -> returns true
        assertEquals(editSyllFirstCommand, editSyllFirstCommand);

        // same values -> returns true
        EditSyllCommand editSyllFirstCommandCopy = new EditSyllCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, new Syllabus(VALID_SYLLABUS, true));
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
     * Simulates and returns a new {@code Person} created by EditSyllCommand.
     */
    private Person simulateEditSyllCommand(Person personTarget, Index subjectIndex, Index syllabusIndex,
            Syllabus syllabus) throws CommandException {
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).edit(syllabus, syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return SubjectsUtil.createPersonWithNewSubjects(personTarget, newSubjects);
    }

}
