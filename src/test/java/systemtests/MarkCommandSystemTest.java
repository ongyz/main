package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.MarkCommand.MESSAGE_MARK_SUCCESS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

public class MarkCommandSystemTest extends AddressBookSystemTest {

    private static final String MESSAGE_INVALID_MARK_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE);

    @Test
    public void mark() throws CommandException {
        /* ----------------- Performing mark operation while an unfiltered list is being shown -------------------- */

        /* Case: mark the first person in the list, command with leading spaces and trailing spaces -> marked */
        Model expectedModel = getModel();
        String command = "     " + MarkCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " "
                + INDEX_FIRST_SYLLABUS.getOneBased() + "       ";

        Person markedPerson = markPerson(expectedModel, INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        String expectedResultMessage = String.format(MESSAGE_MARK_SUCCESS, markedPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: mark the last person in the list -> marked */
        Model modelBeforeMarkingLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeMarkingLast);
        assertCommandSuccess(lastPersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* Case: undo marking the last person in the list -> last person restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: redo marking the last person in the list -> last person marked again */
        command = RedoCommand.COMMAND_WORD;
        markPerson(modelBeforeMarkingLast, lastPersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: marking the middle person in the list -> marked */
        Index middlePersonIndex = getMidIndex(getModel());
        assertCommandSuccess(middlePersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* ------------------ Performing mark operation while a filtered list is being shown ---------------------- */

        /* Case: filtered person list, mark index within bounds of address book and person list -> marked */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index personIndex = INDEX_FIRST_PERSON;
        assertTrue(personIndex.getZeroBased() < expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(personIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS, KEYWORD_MATCHING_MEIER);

        /* Case: filtered person list, mark index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getAddressBook().getPersonList().size();
        command = MarkCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SYLLABUS.getOneBased()
                + " " + INDEX_FIRST_SYLLABUS.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* --------------------------------- Performing invalid delete operation ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = MarkCommand.COMMAND_WORD + " 0 0 0";
        assertCommandFailure(command, MESSAGE_INVALID_MARK_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = MarkCommand.COMMAND_WORD + " -1 -1 -1";
        assertCommandFailure(command, MESSAGE_INVALID_MARK_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPersonList().size() + 1);
        command = MarkCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 1 1";
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(MarkCommand.COMMAND_WORD + " a b c", MESSAGE_INVALID_MARK_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(MarkCommand.COMMAND_WORD + " 1 a b c", MESSAGE_INVALID_MARK_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("mARk 1 1 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Person} at the specified {@code index} in {@code model}'s address book.
     * @return the removed person
     */
    private Person markPerson(Model model, Index personIndex, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        Person personTarget = getPerson(model, personIndex);
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).toggleState(syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

        Set<Subject> newSubjects = new HashSet<>(subjects);
        Person personUpdated = SubjectsUtil.createPersonWithNewSubjects(personTarget, newSubjects);

        model.updatePerson(personTarget, personUpdated);
        return personUpdated;
    }

    private void assertCommandSuccess(Index personIndex, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        assertCommandSuccess(personIndex, subjectIndex, syllabusIndex, null);
    }

    private void assertCommandSuccess(Index personIndex, Index subjectIndex, Index syllabusIndex, String filter)
            throws CommandException {
        Model expectedModel = getModel();
        Person markedPerson = markPerson(expectedModel, personIndex, subjectIndex, syllabusIndex);
        String expectedResultMessage = String.format(MESSAGE_MARK_SUCCESS, markedPerson);
        assertCommandSuccess(MarkCommand.COMMAND_WORD
                + " " + personIndex.getOneBased() + " " + subjectIndex.getOneBased()
                + " " + syllabusIndex.getOneBased(), expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,
     * 1. Asserts that the command box displays an empty string.
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.
     * 3. Asserts that the browser url and selected card remains unchanged.
     * 4. Asserts that the status bar's sync status changes.
     * 5. Asserts that the command box has the default style class.
     * Verifications 1 and 2 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
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
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
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
