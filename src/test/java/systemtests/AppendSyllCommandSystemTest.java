package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.AppendSyllCommand.MESSAGE_APPENDSYLL_SUCCESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYLLABUS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AppendSyllCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.Syllabus;
import seedu.address.model.util.SubjectsUtil;

public class AppendSyllCommandSystemTest extends TutorHelperSystemTest {

    private static final String MESSAGE_INVALID_APPENDSYLL_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE);
    public static final String APPEND_SYLL_SYLLABUS_STRING = "AppendSyllCommandTest";

    @Test
    public void appendsyll() throws CommandException {
        /* ---------------- Performing appendsyll operation while an unfiltered list is being shown ------------------- */

        /* Case: append the first subject of the first person in the list with a new syllabus
         * command with leading spaces and trailing spaces -> success
         */
        Model expectedModel = getModel();
        String command = "     " + AppendSyllCommand.COMMAND_WORD + "      "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + INDEX_FIRST_SUBJECT.getOneBased() + " "
                + PREFIX_SYLLABUS + "AppendSyllCommandSystemTest       ";
        Syllabus syllabusTest = Syllabus.makeSyllabus("AppendSyllCommandSystemTest");
        Person newPerson = appendSyllPerson(
                expectedModel, INDEX_FIRST_PERSON, INDEX_FIRST_SUBJECT, syllabusTest);
        String expectedResultMessage = String.format(MESSAGE_APPENDSYLL_SUCCESS, newPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: append first subject the last person in the list -> success */
        Model modelBeforeAppendLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeAppendLast);
        assertCommandSuccess(lastPersonIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        /* Case: undo command the last person in the list -> first person subject reverted */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* Case: redo command the last person in the list -> first person subject restored again */
        command = RedoCommand.COMMAND_WORD;
        appendSyllPerson(modelBeforeAppendLast, lastPersonIndex, INDEX_FIRST_SUBJECT, syllabusTest);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAppendLast, expectedResultMessage);

        /* Case: append to the first subject of the middle person in the list -> success */
        Index middlePersonIndex = getMidIndex(getModel());
        assertCommandSuccess(middlePersonIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        /* ----------------- Performing appendsyll operation while a filtered list is being shown ------------------  */

        /* Case: filtered person list, person index within bounds of address book and person list -> success */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index personIndex = INDEX_FIRST_PERSON;
        assertTrue(personIndex.getZeroBased() < expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(personIndex, INDEX_FIRST_SUBJECT, syllabusTest);

        /* Case: filtered person list, person index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getPersonList().size();
        command = AppendSyllCommand.COMMAND_WORD + " " + invalidIndex
                + " " + INDEX_FIRST_SUBJECT.getOneBased()
                + " " + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: filtered person list, person index within bounds but subject index is out of bounds
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased()).getSubjects().size() + 1;
        command = AppendSyllCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                + " " + invalidIndex
                + " " + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING;
        assertCommandFailure(command, Messages.MESSAGE_INVALID_SUBJECT_INDEX);

        /* ------------------------------ Performing duplicate appendsyll operation --------------------------------- */

        /* Case: the first person on the unfiltered list already has the test syllabus. -> rejected */
        command = AppendSyllCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                + " " + INDEX_FIRST_SUBJECT.getOneBased()
                + " " + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING;
        Person personTarget = getPerson(getModel(), INDEX_FIRST_PERSON);
        assertCommandFailure(command,
                String.format(AppendSyllCommand.MESSAGE_DUPLICATE_SYLLABUS, personTarget));

        /* ------------------------------- Performing invalid appendsyll operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = AppendSyllCommand.COMMAND_WORD + " 0 0 " + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_APPENDSYLL_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = AppendSyllCommand.COMMAND_WORD + " -1 -1 " + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_APPENDSYLL_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getPersonList().size() + 1);
        command = AppendSyllCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased()
                + " " + INDEX_FIRST_SUBJECT.getOneBased() + " " + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING;
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(AppendSyllCommand.COMMAND_WORD + " a b "
                + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING, MESSAGE_INVALID_APPENDSYLL_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(AppendSyllCommand.COMMAND_WORD + " 1 1 1 "
                + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING, MESSAGE_INVALID_APPENDSYLL_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("appENDsyLL 1 1 "
                + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Removes the {@code Person} at the specified {@code index} in {@code model}'s address book.
     * @return the removed person
     */
    private Person appendSyllPerson(Model model, Index targetPersonIndex, Index subjectIndex, Syllabus syllabus) {
        Person personTarget = getPerson(model, targetPersonIndex);
        List<Subject> targetSubjects = new ArrayList<>(personTarget.getSubjects());

        Subject updatedSubject = targetSubjects.get(subjectIndex.getZeroBased()).add(syllabus);
        targetSubjects.set(subjectIndex.getZeroBased(), updatedSubject);
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
    private void assertCommandSuccess(Index sourcePersonIndex, Index subjectIndex, Syllabus syllabus) {
        Model expectedModel = getModel();
        Person appendedPerson = appendSyllPerson(expectedModel, sourcePersonIndex, subjectIndex, syllabus);
        String expectedResultMessage = String.format(MESSAGE_APPENDSYLL_SUCCESS, appendedPerson);
        assertCommandSuccess(AppendSyllCommand.COMMAND_WORD
                + " " + sourcePersonIndex.getOneBased()
                + " " + subjectIndex.getOneBased()
                + " " + PREFIX_SYLLABUS + APPEND_SYLL_SYLLABUS_STRING, expectedModel, expectedResultMessage);
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
