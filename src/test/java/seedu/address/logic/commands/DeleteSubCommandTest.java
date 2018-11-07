package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalTutorHelper;

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
 * DeleteSubCommand.
 */
public class DeleteSubCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexesUnfilteredList_success() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_PERSON, INDEX_FIRST_SUBJECT);

        String expectedMessage = String.format(DeleteSubCommand.MESSAGE_DELETESUB_SUCCESS, personTarget);
        ModelManager expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Person newPerson = simulateDeleteSubCommand(personTarget, INDEX_FIRST_SUBJECT);

        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT);

        assertCommandFailure(deleteSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidIndexSubjectUnfilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_PERSON, outOfBoundIndex);

        assertCommandFailure(deleteSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_PERSON, INDEX_FIRST_SUBJECT);

        String expectedMessage = String.format(DeleteSubCommand.MESSAGE_DELETESUB_SUCCESS, personTarget);
        Person updatedPerson = simulateDeleteSubCommand(personTarget, INDEX_FIRST_SUBJECT);
        expectedModel.updatePerson(personTarget, updatedPerson);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(deleteSubCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidStudentIndexFilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT);

        assertCommandFailure(deleteSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void execute_invalidSubjectIndexFilteredList_throwsCommandException() {
        Person personTarget = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        Index outOfBoundIndex = Index.fromOneBased(personTarget.getSubjects().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_PERSON, outOfBoundIndex);

        assertCommandFailure(deleteSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_SUBJECT_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Person personTarget = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(INDEX_THIRD_PERSON, INDEX_FIRST_SUBJECT);
        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());

        Person newPerson = simulateDeleteSubCommand(personTarget, INDEX_FIRST_SUBJECT);
        expectedModel.updatePerson(personTarget, newPerson);
        expectedModel.commitTutorHelper();

        // DeleteSub -> first person subject is erased
        deleteSubCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered person list to show all persons
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person deleted again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteSubCommand deleteSubCommand = new DeleteSubCommand(outOfBoundIndex, INDEX_FIRST_SUBJECT);

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(deleteSubCommand, model, commandHistory, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        DeleteSubCommand deleteSubCommand1 = new DeleteSubCommand(INDEX_THIRD_PERSON, INDEX_FIRST_SUBJECT);
        DeleteSubCommand deleteSubCommand2 = new DeleteSubCommand(INDEX_THIRD_PERSON, INDEX_SECOND_SUBJECT);

        // same object -> returns true
        assertEquals(deleteSubCommand1, deleteSubCommand1);

        // same values -> returns true
        DeleteSubCommand deleteSubCommand1Copy = new DeleteSubCommand(INDEX_THIRD_PERSON, INDEX_FIRST_SUBJECT);
        assertEquals(deleteSubCommand1, deleteSubCommand1Copy);

        // different types -> returns false
        assertNotEquals(deleteSubCommand1, 1);

        // null -> returns false
        assertNotEquals(deleteSubCommand1, null);

        // different command -> returns false
        assertNotEquals(deleteSubCommand1, deleteSubCommand2);
    }

    /**
     * Simulates and returns a new {@code Person} created by DeleteSubCommand.
     */
    private Person simulateDeleteSubCommand(Person personTarget, Index subjectIndex) {
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());
        subjects.remove(subjectIndex.getZeroBased());

        Set<Subject> newSubjects = new HashSet<>(subjects);

        return SubjectsUtil.createPersonWithNewSubjects(personTarget, newSubjects);
    }

}
