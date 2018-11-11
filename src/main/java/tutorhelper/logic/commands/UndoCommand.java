package tutorhelper.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;

/**
 * Reverts the {@code model}'s TutorHelper to its previous state.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more commands to undo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoTutorHelper()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoTutorHelper();
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
