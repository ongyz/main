package tutorhelper.logic.commands;

import tutorhelper.commons.core.EventsCenter;
import tutorhelper.commons.events.ui.ExitAppRequestEvent;
import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting TutorHelper as requested ...";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        EventsCenter.getInstance().post(new ExitAppRequestEvent());
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

}
