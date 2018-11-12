package tutorhelper.logic.commands;

import static java.util.Objects.requireNonNull;

import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.TutorHelper;

/**
 * Clears the TutorHelper.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "TutorHelper has been cleared!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.resetData(new TutorHelper());
        model.commitTutorHelper();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
