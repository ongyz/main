package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.AddSubCommand.MESSAGE_ADDSUB_SUCCESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddSubCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

public class AddSubCommandSystemTest extends TutorHelperSystemTest {

    public static final String ADDSUB_TEST_SUBJECT = "Physics";

    private static final String MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddSubCommand.MESSAGE_USAGE);

    @Test
    public void addsub() {
        /* -------------- Performing addsub operation while an unfiltered list is being shown ------------------*/

        /* Case: add a subject to the first person in the list
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        String command = "     " + AddSubCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_SUBJECT + "Physics       ";
        Subject subjectTest = Subject.makeSubject("Physics");
        Person newPerson = addSubPerson(
                expectedModel, INDEX_FIRST_PERSON, subjectTest);
        String expectedResultMessage = String.format(MESSAGE_ADDSUB_SUCCESS, newPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: add subject to the last person in the list -> success */
        Model modelBeforeAppendLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeAppendLast);
        assertCommandSuccess(lastPersonIndex, subjectTest);

        /* Case: undo command the last person in the list -> first person subject reverted */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* Case: redo command the last person in the list -> first person subject restored again */
        command = RedoCommand.COMMAND_WORD;
        addSubPerson(modelBeforeAppendLast, lastPersonIndex, subjectTest);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* ----------------- Performing addsub operation while a filtered list is being shown ------------------  */

        /* Case: filtered person list, person index within bounds of TutorHelper and person list -> success */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index personIndex = INDEX_FIRST_PERSON;
        assertTrue(personIndex.getZeroBased() < expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(personIndex, subjectTest);

        /* Case: filtered person list, person index within bounds of TutorHelper but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getPersonList().size();
        command = AddSubCommand.COMMAND_WORD + " " + invalidIndex
                + " " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* ------------------------------- Performing invalid addsub operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = AddSubCommand.COMMAND_WORD + " 0 " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = AddSubCommand.COMMAND_WORD + " -1 " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getPersonList().size() + 1);
        command = AddSubCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased()
                + " " + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT;
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(AddSubCommand.COMMAND_WORD + " a "
                + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(AddSubCommand.COMMAND_WORD + " 1 1 "
                + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT, MESSAGE_INVALID_ADDSUB_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("aDdSuB 1 "
                + PREFIX_SUBJECT + ADDSUB_TEST_SUBJECT, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Adds a subject {@code subject} to the {@code Person}
     * at the specified {@code index} in {@code model}'s TutorHelper.
     * @return the removed person
     */
    private Person addSubPerson(Model model, Index targetPersonIndex, Subject subject) {
        Person personTarget = getPerson(model, targetPersonIndex);
        List<Subject> targetSubjects = new ArrayList<>(personTarget.getSubjects());
        targetSubjects.add(subject);

        Set<Subject> newSubjects = new HashSet<>(targetSubjects);

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
    private void assertCommandSuccess(Index sourcePersonIndex, Subject subject) {
        Model expectedModel = getModel();
        String command = AddSubCommand.COMMAND_WORD
                + " " + sourcePersonIndex.getOneBased()
                + " " + PREFIX_SUBJECT + subject.getSubjectName();
        Person newPerson = addSubPerson(
                expectedModel, sourcePersonIndex, subject);
        String expectedResultMessage = String.format(MESSAGE_ADDSUB_SUCCESS, newPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
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
