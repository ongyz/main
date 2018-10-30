package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.DeleteSubCommand.MESSAGE_DELETESUB_SUCCESS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteSubCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

public class DeleteSubCommandSystemTest extends TutorHelperSystemTest {

    private static final String MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteSubCommand.MESSAGE_USAGE);

    @Test
    public void deletesub() {
        /* ----------------- Performing erase operation while an unfiltered list is being shown -------------------- */

        /* Case: delete the first subject of the third person in the list,
         * command with leading spaces and trailing spaces -> success
         */
        Model lastModel = getModel();
        Model expectedModel = getModel();
        String command = "     " + DeleteSubCommand.COMMAND_WORD + "      "
                + INDEX_THIRD_PERSON.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + "       ";

        Person deletedSubPerson = deleteSubPerson(expectedModel, INDEX_THIRD_PERSON, INDEX_FIRST_SUBJECT);
        String expectedResultMessage = String.format(MESSAGE_DELETESUB_SUCCESS, deletedSubPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: undo command  -> third person restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, lastModel, expectedResultMessage);

        /* Case: redo command the last person in the list -> last person syllabus is erased again */
        command = RedoCommand.COMMAND_WORD;
        deleteSubPerson(lastModel, INDEX_THIRD_PERSON, INDEX_FIRST_SUBJECT);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, lastModel, expectedResultMessage);

        /* ----------------- Performing deletesub operation while a filtered list is being shown ------------------  */

        /* Case: filtered person list, person index within bounds of address book and person list -> success */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index personIndex = INDEX_SECOND_PERSON;
        assertTrue(personIndex.getZeroBased() < expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(personIndex, INDEX_FIRST_SUBJECT);

        /* Case: filtered person list, person index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getPersonList().size();
        command = DeleteSubCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SUBJECT.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: filtered person list, person index within bounds but subject index is out of bounds
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased()).getSubjects().size() + 1;
        command = DeleteSubCommand.COMMAND_WORD + " " + INDEX_SECOND_PERSON.getOneBased()
                + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_SUBJECT_INDEX);

        /* ------------------------------- Performing invalid deletesub operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = DeleteSubCommand.COMMAND_WORD + " 0 0";
        assertCommandFailure(command, MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = DeleteSubCommand.COMMAND_WORD + " -1 -1";
        assertCommandFailure(command, MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getPersonList().size() + 1);
        command = DeleteSubCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 1";
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(
                DeleteSubCommand.COMMAND_WORD + " a b", MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(
                DeleteSubCommand.COMMAND_WORD + " 1 a b", MESSAGE_INVALID_DELETESUB_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("dElEtEsUb 1 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Subject} at the specified {@code subjectIndex} for
     * the {@code Person} at the specified {@code personIndex} in {@code model}'s address book.
     * @return the updated person
     */
    private Person deleteSubPerson(Model model, Index personIndex, Index subjectIndex) {
        Person personTarget = getPerson(model, personIndex);
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());
        subjects.remove(subjectIndex.getZeroBased());

        Set<Subject> newSubjects = new HashSet<>(subjects);
        Person personUpdated = SubjectsUtil.createPersonWithNewSubjects(personTarget, newSubjects);

        model.updatePerson(personTarget, personUpdated);
        return personUpdated;
    }

    /**
     * Executes {@code command} and in addition,
     * 1. Asserts that the command box displays an empty string.
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.
     * 3. Asserts that the browser url and selected card remains unchanged.
     * 4. Asserts that the status bar's sync status changes.
     * 5. Asserts that the command box has the default style class.
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Index personIndex, Index subjectIndex) {
        Model expectedModel = getModel();
        String command = DeleteSubCommand.COMMAND_WORD
                + " " + personIndex.getOneBased()
                + " " + subjectIndex.getOneBased();
        Person deletedSubPerson = deleteSubPerson(expectedModel, personIndex, subjectIndex);
        String expectedResultMessage = String.format(MESSAGE_DELETESUB_SUCCESS, deletedSubPerson);
        assertCommandSuccess(command,
                expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,
     * 1. Asserts that the command box displays an empty string.
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.
     * 3. Asserts that the browser url and selected card remains unchanged.
     * 4. Asserts that the status bar's sync status changes.
     * 5. Asserts that the command box has the default style class.
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 4. Asserts that the command box has the error style.<br>
     * Verifications 1 and 2 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
