package tutorhelper.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;

import java.util.List;

import tutorhelper.commons.core.EventsCenter;
import tutorhelper.commons.core.index.Index;
import tutorhelper.commons.events.ui.JumpToListRequestEvent;
import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;

/**
 * Selects a student identified using it's displayed index from the TutorHelper.
 */
public class SelectCommand extends Command {

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the student identified by the index number used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_STUDENT_SUCCESS = "Selected Student: %1$s";

    private final Index targetIndex;

    public SelectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        List<Student> filteredStudentList = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= filteredStudentList.size()) {
            throw new CommandException(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_STUDENT_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCommand // instanceof handles nulls
                && targetIndex.equals(((SelectCommand) other).targetIndex)); // state check
    }
}
