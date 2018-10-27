package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.TutorHelperBuilder;

public class VersionedTutorHelperTest {

    private final ReadOnlyTutorHelper tutorHelperWithAmy = new TutorHelperBuilder().withPerson(AMY).build();
    private final ReadOnlyTutorHelper tutorHelperWithBob = new TutorHelperBuilder().withPerson(BOB).build();
    private final ReadOnlyTutorHelper tutorHelperWithCarl = new TutorHelperBuilder().withPerson(CARL).build();
    private final ReadOnlyTutorHelper emptyTutorHelper = new TutorHelperBuilder().build();

    @Test
    public void commit_singleTutorHelper_noStatesRemovedCurrentStateSaved() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(emptyTutorHelper);

        versionedTutorHelper.commit();
        assertTutorHelperListStatus(versionedTutorHelper,
                Collections.singletonList(emptyTutorHelper),
                emptyTutorHelper,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleTutorHelperPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);

        versionedTutorHelper.commit();
        assertTutorHelperListStatus(versionedTutorHelper,
                Arrays.asList(emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob),
                tutorHelperWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleTutorHelperPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 2);

        versionedTutorHelper.commit();
        assertTutorHelperListStatus(versionedTutorHelper,
                Collections.singletonList(emptyTutorHelper),
                emptyTutorHelper,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleTutorHelperPointerAtEndOfStateList_returnsTrue() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);

        assertTrue(versionedTutorHelper.canUndo());
    }

    @Test
    public void canUndo_multipleTutorHelperPointerAtStartOfStateList_returnsTrue() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 1);

        assertTrue(versionedTutorHelper.canUndo());
    }

    @Test
    public void canUndo_singleTutorHelper_returnsFalse() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(emptyTutorHelper);

        assertFalse(versionedTutorHelper.canUndo());
    }

    @Test
    public void canUndo_multipleTutorHelperPointerAtStartOfStateList_returnsFalse() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 2);

        assertFalse(versionedTutorHelper.canUndo());
    }

    @Test
    public void canRedo_multipleTutorHelperPointerNotAtEndOfStateList_returnsTrue() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 1);

        assertTrue(versionedTutorHelper.canRedo());
    }

    @Test
    public void canRedo_multipleTutorHelperPointerAtStartOfStateList_returnsTrue() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 2);

        assertTrue(versionedTutorHelper.canRedo());
    }

    @Test
    public void canRedo_singleTutorHelper_returnsFalse() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(emptyTutorHelper);

        assertFalse(versionedTutorHelper.canRedo());
    }

    @Test
    public void canRedo_multipleTutorHelperPointerAtEndOfStateList_returnsFalse() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);

        assertFalse(versionedTutorHelper.canRedo());
    }

    @Test
    public void undo_multipleTutorHelperPointerAtEndOfStateList_success() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);

        versionedTutorHelper.undo();
        assertTutorHelperListStatus(versionedTutorHelper,
                Collections.singletonList(emptyTutorHelper),
                tutorHelperWithAmy,
                Collections.singletonList(tutorHelperWithBob));
    }

    @Test
    public void undo_multipleTutorHelperPointerNotAtStartOfStateList_success() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 1);

        versionedTutorHelper.undo();
        assertTutorHelperListStatus(versionedTutorHelper,
                Collections.emptyList(),
                emptyTutorHelper,
                Arrays.asList(tutorHelperWithAmy, tutorHelperWithBob));
    }

    @Test
    public void undo_singleTutorHelper_throwsNoUndoableStateException() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(emptyTutorHelper);

        assertThrows(VersionedTutorHelper.NoUndoableStateException.class, versionedTutorHelper::undo);
    }

    @Test
    public void undo_multipleTutorHelperPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 2);

        assertThrows(VersionedTutorHelper.NoUndoableStateException.class, versionedTutorHelper::undo);
    }

    @Test
    public void redo_multipleTutorHelperPointerNotAtEndOfStateList_success() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 1);

        versionedTutorHelper.redo();
        assertTutorHelperListStatus(versionedTutorHelper,
                Arrays.asList(emptyTutorHelper, tutorHelperWithAmy),
                tutorHelperWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleTutorHelperPointerAtStartOfStateList_success() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 2);

        versionedTutorHelper.redo();
        assertTutorHelperListStatus(versionedTutorHelper,
                Collections.singletonList(emptyTutorHelper),
                tutorHelperWithAmy,
                Collections.singletonList(tutorHelperWithBob));
    }

    @Test
    public void redo_singleTutorHelper_throwsNoRedoableStateException() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(emptyTutorHelper);

        assertThrows(VersionedTutorHelper.NoRedoableStateException.class, versionedTutorHelper::redo);
    }

    @Test
    public void redo_multipleTutorHelperPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(
                emptyTutorHelper, tutorHelperWithAmy, tutorHelperWithBob);

        assertThrows(VersionedTutorHelper.NoRedoableStateException.class, versionedTutorHelper::redo);
    }

    @Test
    public void equals() {
        VersionedTutorHelper versionedTutorHelper = prepareTutorHelperList(tutorHelperWithAmy, tutorHelperWithBob);

        // same values -> returns true
        VersionedTutorHelper copy = prepareTutorHelperList(tutorHelperWithAmy, tutorHelperWithBob);
        assertTrue(versionedTutorHelper.equals(copy));

        // same object -> returns true
        assertTrue(versionedTutorHelper.equals(versionedTutorHelper));

        // null -> returns false
        assertFalse(versionedTutorHelper.equals(null));

        // different types -> returns false
        assertFalse(versionedTutorHelper.equals(1));

        // different state list -> returns false
        VersionedTutorHelper differentTutorHelperList = prepareTutorHelperList(tutorHelperWithBob, tutorHelperWithCarl);
        assertFalse(versionedTutorHelper.equals(differentTutorHelperList));

        // different current pointer index -> returns false
        VersionedTutorHelper differentCurrentStatePointer = prepareTutorHelperList(
                tutorHelperWithAmy, tutorHelperWithBob);
        shiftCurrentStatePointerLeftwards(versionedTutorHelper, 1);
        assertFalse(versionedTutorHelper.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedTutorHelper} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedTutorHelper#currentStatePointer} is equal to {@code expectedStatesBeforePointer},
     * and states after {@code versionedTutorHelper#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertTutorHelperListStatus(VersionedTutorHelper versionedTutorHelper,
                                             List<ReadOnlyTutorHelper> expectedStatesBeforePointer,
                                             ReadOnlyTutorHelper expectedCurrentState,
                                             List<ReadOnlyTutorHelper> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new TutorHelper(versionedTutorHelper), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedTutorHelper.canUndo()) {
            versionedTutorHelper.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyTutorHelper expectedTutorHelper : expectedStatesBeforePointer) {
            assertEquals(expectedTutorHelper, new TutorHelper(versionedTutorHelper));
            versionedTutorHelper.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyTutorHelper expectedTutorHelper : expectedStatesAfterPointer) {
            versionedTutorHelper.redo();
            assertEquals(expectedTutorHelper, new TutorHelper(versionedTutorHelper));
        }

        // check that there are no more states after pointer
        assertFalse(versionedTutorHelper.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedTutorHelper.undo());
    }

    /**
     * Creates and returns a {@code VersionedTutorHelper} with the {@code TutorHelperStates} added into it, and the
     * {@code VersionedTutorHelper#currentStatePointer} at the end of list.
     */
    private VersionedTutorHelper prepareTutorHelperList(ReadOnlyTutorHelper... tutorHelperStates) {
        assertFalse(tutorHelperStates.length == 0);

        VersionedTutorHelper versionedTutorHelper = new VersionedTutorHelper(tutorHelperStates[0]);
        for (int i = 1; i < tutorHelperStates.length; i++) {
            versionedTutorHelper.resetData(tutorHelperStates[i]);
            versionedTutorHelper.commit();
        }

        return versionedTutorHelper;
    }

    /**
     * Shifts the {@code versionedTutorHelper#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedTutorHelper versionedTutorHelper, int count) {
        for (int i = 0; i < count; i++) {
            versionedTutorHelper.undo();
        }
    }
}
