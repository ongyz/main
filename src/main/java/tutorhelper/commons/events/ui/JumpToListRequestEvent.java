package tutorhelper.commons.events.ui;

import tutorhelper.commons.core.index.Index;
import tutorhelper.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of students
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToListRequestEvent(Index targetIndex) {
        this.targetIndex = targetIndex.getZeroBased();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
