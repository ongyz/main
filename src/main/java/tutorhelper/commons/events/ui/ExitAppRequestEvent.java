package tutorhelper.commons.events.ui;

import tutorhelper.commons.events.BaseEvent;

/**
 * Indicates a request for App termination
 */
public class ExitAppRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
