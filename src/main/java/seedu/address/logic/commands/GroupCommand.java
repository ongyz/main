package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.time.DayOfWeek;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.tuitionTiming.TuitionTimingContainsKeywordsPredicate;

/**
 * Sorts the students in TutorHelper based on their tuition timings.
 */
public class GroupCommand extends Command {
    public static final String COMMAND_WORD = "group";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Groups all students based on the specified DAY "
            + "or TIME and displays them as a list.\n"
            + "The input can also be null and the students will be grouped according to the current day and time.\n"
            + "Parameters: KEYWORD\n"
            + "Examples: " + COMMAND_WORD + " " + PREFIX_DATE + "Monday\n"
            + COMMAND_WORD + " " + PREFIX_TIME + "12:00pm\n"
            + COMMAND_WORD + " " + PREFIX_DATE + "Monday" + " " + PREFIX_TIME + "12:00pm\n";

    public static final String MESSAGE_SUCCESS = "Grouped all students";

    private TuitionTimingContainsKeywordsPredicate dayPredicate;
    private TuitionTimingContainsKeywordsPredicate timePredicate;
    private boolean dayPresent;
    private boolean timePresent;

    public GroupCommand(DayOfWeek day, String time, boolean dayPresent, boolean timePresent) {
        this.dayPresent = dayPresent;
        this.timePresent = timePresent;

        //create TuitionTimingContainsKeywordsPredicate
        if (dayPresent == true && timePresent == true) {
            this.dayPredicate = new TuitionTimingContainsKeywordsPredicate(day.toString());
            this.timePredicate = new TuitionTimingContainsKeywordsPredicate(time);

        } else if (dayPresent == true && timePresent == false) {
            this.dayPredicate = new TuitionTimingContainsKeywordsPredicate(day.toString());
            this.timePredicate = null;

        } else if (dayPresent == false && timePresent == true) {
            this.timePredicate = new TuitionTimingContainsKeywordsPredicate(time);
            this.dayPredicate = null;
        }

    }
    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);

        if (this.dayPresent == true && this.timePresent == true) {
            model.updateFilteredPersonList(dayPredicate);
        } else if (this.dayPresent == true) {
            model.updateFilteredPersonList(dayPredicate);
        } else if (this.timePresent == true) {
            model.updateFilteredPersonList(timePredicate);
        }
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size())
        );
    }
}
