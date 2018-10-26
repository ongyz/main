package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CopySubCommand.MESSAGE_COPYSUB_SUCCESS;
import static seedu.address.logic.commands.EraseSyllCommand.MESSAGE_ERASESYLL_SUCCESS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SYLLABUS;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_SYLLABUS;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CopySubCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

public class CopySubCommandSystemTest extends AddressBookSystemTest {

    private static final String MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE);

    @Test
    public void copysub() throws CommandException {
        /* ---------------- Performing copysub operation while an unfiltered list is being shown ------------------- */

        /* Case: copy the first subject of the first person in the list,
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        String command = "     " + CopySubCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " "
                + INDEX_SECOND_PERSON.getOneBased() + "       ";

        Person newPerson = copySubPerson(
                expectedModel, INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);
        String expectedResultMessage = String.format(MESSAGE_COPYSUB_SUCCESS, newPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: copy first subject the last person in the list to the first person on the list -> success */
        Model modelBeforeMarkingLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeMarkingLast);
        assertCommandSuccess(lastPersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_PERSON);

        /* Case: undo command the last person in the list -> first person subject reverted */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: redo command the last person in the list -> first person subject restored again */
        command = RedoCommand.COMMAND_WORD;
        copySubPerson(modelBeforeMarkingLast, lastPersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_SYLLABUS);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeMarkingLast, expectedResultMessage);

        /* Case: copy first subject of the middle person in the list to first person in the list -> success */
        Index middlePersonIndex = getMidIndex(getModel());
        assertCommandSuccess(middlePersonIndex, INDEX_FIRST_SUBJECT, INDEX_FIRST_PERSON);

        /* ----------------- Performing copysub operation while a filtered list is being shown ------------------  */

        /* Case: filtered person list, person index within bounds of address book and person list -> success */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index personIndex = INDEX_FIRST_PERSON;
        assertTrue(personIndex.getZeroBased() < expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(personIndex, INDEX_FIRST_SUBJECT, INDEX_SECOND_PERSON);

        /* Case: filtered person list, person index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getAddressBook().getPersonList().size();
        command = CopySubCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SUBJECT.getOneBased()
                + " " + INDEX_SECOND_PERSON.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: filtered person list, person index within bounds but subject index is out of bounds
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getAddressBook().getPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()).getSubjects().size() + 1;
        command = CopySubCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                + " " + invalidIndex
                + " " + INDEX_SECOND_PERSON.getOneBased();
        assertCommandFailure(command, Messages.MESSAGE_INVALID_SUBJECT_INDEX);

        /* ------------------------------- Performing invalid copySub operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = CopySubCommand.COMMAND_WORD + " 0 0 0";
        assertCommandFailure(command, MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = CopySubCommand.COMMAND_WORD + " -1 -1 -1";
        assertCommandFailure(command, MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPersonList().size() + 1);
        command = CopySubCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 1 1";
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(
                CopySubCommand.COMMAND_WORD + " a b c", MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(
                CopySubCommand.COMMAND_WORD + " 1 a b c", MESSAGE_INVALID_COPYSUB_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("cOPYsub 1 1 1", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Person} at the specified {@code index} in {@code model}'s address book.
     * @return the removed person
     */
    private Person copySubPerson(Model model, Index sourcePersonIndex, Index subjectIndex, Index targetPersonIndex) {
        Person personSource = getPerson(model, sourcePersonIndex);
        Person personTarget = getPerson(model, targetPersonIndex);
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

        Set<Subject> newSubjects = new HashSet<>(updatedSubjects);
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
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Index sourcePersonIndex, Index subjectIndex, Index targetPersonIndex) {
        Model expectedModel = getModel();
        Person copySubPerson = copySubPerson(expectedModel, sourcePersonIndex, subjectIndex, targetPersonIndex);
        String expectedResultMessage = String.format(MESSAGE_COPYSUB_SUCCESS, copySubPerson);
        assertCommandSuccess(CopySubCommand.COMMAND_WORD
                + " " + sourcePersonIndex.getOneBased() + " " + subjectIndex.getOneBased()
                + " " + targetPersonIndex.getOneBased(), expectedModel, expectedResultMessage);
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
