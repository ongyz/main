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
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getDifferentSubjectPersonIndexes;
import static seedu.address.testutil.TypicalPersons.getSameSubjectPersonsIndexes;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code CopySubCommand}.
 */
public class CopySubCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexesUnfilteredList_success() {
        Person personSource = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personTarget = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);

        String expectedMessage = String.format(CopySubCommand.MESSAGE_COPYSUB_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person newPerson = simulateCopySubCommand(personSource, INDEX_FIRST_SUBJECT, personTarget);

        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(copySubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CopySubCommand copySubCommand = new CopySubCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        copySubCommand = new CopySubCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, outOfBoundIndex);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_samePersonUnfilteredList_throwsCommandException() {
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_PERSON);
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertCommandFailure(copySubCommand, model, commandHistory,
                String.format(CopySubCommand.MESSAGE_COPYSUB_FAILED_SAME_PERSON, personTarget));
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_PERSON, outOfBoundIndex, INDEX_SECOND_PERSON);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());
        CopySubCommand copySubCommand = new CopySubCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_PERSON);

        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_hasExistingSubject_success() {
        List<Index> sameSubjectIndex = getSameSubjectPersonsIndexes();

        Index firstIndex = sameSubjectIndex.get(0);
        Index secondIndex = sameSubjectIndex.get(1);

        Person personSource = model.getFilteredPersonList().get(firstIndex.getZeroBased());
        Person personTarget = model.getFilteredPersonList().get(secondIndex.getZeroBased());

        CopySubCommand copySubCommand = new CopySubCommand(
                firstIndex, INDEX_FIRST_SUBJECT, secondIndex);

        String expectedMessage = String.format(CopySubCommand.MESSAGE_COPYSUB_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person newPerson = simulateCopySubCommand(personSource, INDEX_FIRST_SUBJECT, personTarget);

        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitAddressBook();

        // No new subject should be created
        assertEquals(personTarget.getSubjects().size(), newPerson.getSubjects().size());
        assertCommandSuccess(copySubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addNewSubject_success() {
        List<Index> sameSubjectIndex = getDifferentSubjectPersonIndexes();

        Index firstIndex = sameSubjectIndex.get(0);
        Index secondIndex = sameSubjectIndex.get(1);

        Person personSource = model.getFilteredPersonList().get(firstIndex.getZeroBased());
        Person personTarget = model.getFilteredPersonList().get(secondIndex.getZeroBased());

        CopySubCommand copySubCommand = new CopySubCommand(
                firstIndex, INDEX_FIRST_SUBJECT, secondIndex);

        String expectedMessage = String.format(CopySubCommand.MESSAGE_COPYSUB_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person newPerson = simulateCopySubCommand(personSource, INDEX_FIRST_SUBJECT, personTarget);

        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitAddressBook();

        // New subject should be created
        assertNotEquals(personTarget.getSubjects().size(), newPerson.getSubjects().size());

        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertCommandSuccess(copySubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person personSource = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person personTarget = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        CopySubCommand copySubCommand = new CopySubCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person newPerson = simulateCopySubCommand(personSource, INDEX_FIRST_SUBJECT, personTarget);

        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitAddressBook();

        // CopySub -> first person syllabus is erased
        copySubCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered person list to show all persons
        expectedModel.undoAddressBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person deleted again
        expectedModel.redoAddressBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CopySubCommand copySubCommand = new CopySubCommand(
                outOfBoundIndex, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);

        // execution failed -> address book state not added into model
        assertCommandFailure(copySubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        CopySubCommand copySubFirstCommand = new CopySubCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);
        CopySubCommand copySubSecondCommand = new CopySubCommand(
                INDEX_SECOND_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_PERSON);

        // same object -> returns true
        assertEquals(copySubFirstCommand, copySubFirstCommand);

        // same values -> returns true
        CopySubCommand copySubFirstCommandCopy = new CopySubCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);
        assertEquals(copySubFirstCommand, copySubFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(copySubFirstCommand, 1);

        // null -> returns false
        assertNotEquals(copySubFirstCommand, null);

        // different command -> returns false
        assertNotEquals(copySubFirstCommand, copySubSecondCommand);
    }

    /**
     * Simulates and returns a new {@code Person} created by CopySubCommand.
     */
    private Person simulateCopySubCommand(Person personSource, Index subjectIndex, Person personTarget) {
        List<Subject> sourceSubjects = new ArrayList<>(personSource.getSubjects());
        List<Subject> targetSubjects = new ArrayList<>(personTarget.getSubjects());
        Subject selectedSubject = sourceSubjects.get(subjectIndex.getZeroBased());

        Set<Subject> updatedSubjects;

        if (SubjectsUtil.hasSubject(personTarget, selectedSubject.getSubjectType())) {
            Index index = SubjectsUtil.findSubjectIndex(personTarget, selectedSubject.getSubjectType()).get();

            Subject updatedSubject = targetSubjects.get(index.getZeroBased())
                    .append(selectedSubject.getSubjectContent());
            targetSubjects.set(index.getZeroBased(), updatedSubject);
            updatedSubjects = new HashSet<>(targetSubjects);
        } else {
            targetSubjects.add(selectedSubject);
            updatedSubjects = new HashSet<>(targetSubjects);
        }

        return SubjectsUtil.createPersonWithNewSubjects(personTarget, updatedSubjects);
    }

}
