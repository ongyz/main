package tutorhelper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code TutorHelper} that keeps track of its own history.
 */
public class VersionedTutorHelper extends TutorHelper {

    private final List<ReadOnlyTutorHelper> tutorHelperStateList;
    private int currentStatePointer;

    public VersionedTutorHelper(ReadOnlyTutorHelper initialState) {
        super(initialState);

        tutorHelperStateList = new ArrayList<>();
        tutorHelperStateList.add(new TutorHelper(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code TutorHelper} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        tutorHelperStateList.add(new TutorHelper(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        tutorHelperStateList.subList(currentStatePointer + 1, tutorHelperStateList.size()).clear();
    }

    /**
     * Restores the TutorHelper to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(tutorHelperStateList.get(currentStatePointer));
    }

    /**
     * Restores the TutorHelper to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(tutorHelperStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has TutorHelper states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has TutorHelper states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < tutorHelperStateList.size() - 1;
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
                && tutorHelperStateList.equals(otherVersionedTutorHelper.tutorHelperStateList)
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
