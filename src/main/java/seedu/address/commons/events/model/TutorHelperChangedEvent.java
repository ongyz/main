package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.ReadOnlyTutorHelper;

/** Indicates the TutorHelper in the model has changed*/
public class TutorHelperChangedEvent extends BaseEvent {

    public final ReadOnlyTutorHelper data;

    public TutorHelperChangedEvent(ReadOnlyTutorHelper data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of students " + data.getStudentList().size();
    }
}
