package tutorhelper.logic.parser;

import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import tutorhelper.logic.commands.GroupCommand;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.tuitiontiming.TuitionTiming;
import tutorhelper.model.tuitiontiming.TuitionTimingContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new GroupCommand object
 */
public class GroupCommandParser implements Parser<GroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the GroupCommand
     * and returns an GroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GroupCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.matches(TuitionTiming.DAY_REGEX)) {
            return new GroupCommand(
                    new TuitionTimingContainsKeywordsPredicate(trimmedArgs), true, false);
        } else if (trimmedArgs.matches(TuitionTiming.TIME_REGEX)) {
            return new GroupCommand(
                    new TuitionTimingContainsKeywordsPredicate(trimmedArgs), false, true);
        } else {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));
        }
    }
}
