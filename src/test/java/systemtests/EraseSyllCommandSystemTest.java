package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.EraseSyllCommand.MESSAGE_ERASESYLL_SUCCESS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SYLLABUS;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EraseSyllCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

public class EraseSyllCommandSystemTest extends TutorHelperSystemTest {

    private static final String MESSAGE_INVALID_ERASESYLL_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EraseSyllCommand.MESSAGE_USAGE);

    @Test
    public void erasesyll() throws CommandException {
        /* ----------------- Performing erase operation while an unfiltered list is being shown -------------------- */

        /* Case: erase the syllabus of first subject of the first person in the list,
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        String command = "     " + EraseSyllCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " "
                + INDEX_FIRST_SYLLABUS.getOneBased() + "       ";

        Person erasedSyllPerson = eraseSyllPerson(
                expectedModel, INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        String expectedResultMessage = String.format(MESSAGE_ERASESYLL_SUCCESS, erasedSyllPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: erase ths syllabus of first subject the last person in the list -> success */
        Model modelBeforeMarkingLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeMarkingLast);
        assertCommandSuccess(lastPersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* Case: undo command the last person in the list -> last person restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: redo command the last person in the list -> last person syllabus is erased again */
        command = RedoCommand.COMMAND_WORD;
        eraseSyllPerson(modelBeforeMarkingLast, lastPersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: erase syllabus of first subject of the middle person in the list -> success */
        Index middlePersonIndex = getMidIndex(getModel());
        assertCommandSuccess(middlePersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* ----------------- Performing erasesyll operation while a filtered list is being shown ------------------  */

        /* Case: filtered person list, person index within bounds of address book and person list -> success */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index personIndex = INDEX_FIRST_PERSON;
        assertTrue(personIndex.getZeroBased() < expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(personIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);

        /* Case: filtered person list, person index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getPersonList().size();
        command = EraseSyllCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SYLLABUS.getOneBased()
                + " " + INDEX_FIRST_SYLLABUS.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: filtered person list, person index within bounds but syllabus index is out of bounds
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getPersonList().size();
        command = EraseSyllCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SYLLABUS.getOneBased()
                + " " + INDEX_THIRD_SYLLABUS.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* ------------------------------- Performing invalid erasesyll operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = EraseSyllCommand.COMMAND_WORD + " 0 0 0";
        assertCommandFailure(command, MESSAGE_INVALID_ERASESYLL_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = EraseSyllCommand.COMMAND_WORD + " -1 -1 -1";
        assertCommandFailure(command, MESSAGE_INVALID_ERASESYLL_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getPersonList().size() + 1);
        command = EraseSyllCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 1 1";
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(
                EraseSyllCommand.COMMAND_WORD + " a b c", MESSAGE_INVALID_ERASESYLL_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(
                EraseSyllCommand.COMMAND_WORD + " 1 a b c", MESSAGE_INVALID_ERASESYLL_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ERasESYll 1 1 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Person} at the specified {@code index} in {@code model}'s address book.
     * @return the removed person
     */
    private Person eraseSyllPerson(Model model, Index personIndex, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        Person personTarget = getPerson(model, personIndex);
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).remove(syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);

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
    private void assertCommandSuccess(Index personIndex, Index subjectIndex, Index syllabusIndex)
            throws CommandException {
        Model expectedModel = getModel();
        Person erasedSyllPerson = eraseSyllPerson(expectedModel, personIndex, subjectIndex, syllabusIndex);
        String expectedResultMessage = String.format(MESSAGE_ERASESYLL_SUCCESS, erasedSyllPerson);
        assertCommandSuccess(EraseSyllCommand.COMMAND_WORD
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
