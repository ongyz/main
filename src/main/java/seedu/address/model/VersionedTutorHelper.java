package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code TutorHelper} that keeps track of its own history.
 */
public class VersionedTutorHelper extends TutorHelper {

    private final List<ReadOnlyTutorHelper> TutorHelperStateList;
    private int currentStatePointer;

    public VersionedTutorHelper(ReadOnlyTutorHelper initialState) {
        super(initialState);

        TutorHelperStateList = new ArrayList<>();
        TutorHelperStateList.add(new TutorHelper(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code TutorHelper} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        TutorHelperStateList.add(new TutorHelper(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        TutorHelperStateList.subList(currentStatePointer + 1, TutorHelperStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(TutorHelperStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(TutorHelperStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < TutorHelperStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedTutorHelper)) {
            return false;
        }

        VersionedTutorHelper otherVersionedTutorHelper = (VersionedTutorHelper) other;

        // state check
        return super.equals(otherVersionedTutorHelper)
                && TutorHelperStateList.equals(otherVersionedTutorHelper.TutorHelperStateList)
                && currentStatePointer == otherVersionedTutorHelper.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of TutorHelperState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of TutorHelperState list, unable to redo.");
        }
    }
}
