package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.time.DayOfWeek;
import java.util.stream.Stream;
import seedu.address.logic.commands.GroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tuitionTiming.TuitionTiming;
import seedu.address.model.tuitionTiming.TuitionTimingContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new GroupCommand object
 */
public class GroupCommandParser implements Parser<GroupCommand> {
    private DayOfWeek date = null;
    private String time = null;

    /**
     * Parses the given {@code String} of arguments in the context of the GroupCommand
     * and returns an GroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public GroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME);

        //if only contain date
        if (arePrefixesPresent(argMultimap, PREFIX_DATE) && !arePrefixesPresent(argMultimap, PREFIX_TIME)) {
            this.date = ParserUtil.parseDay(argMultimap.getValue(PREFIX_DATE).get());
            return new GroupCommand(date, time, true, false);
            //true indicates presence of date. false indicates absence of time.
        }
        //if only contain time
        else if (arePrefixesPresent(argMultimap, PREFIX_TIME) && !arePrefixesPresent(argMultimap, PREFIX_DATE)){
            this.time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get());
            return new GroupCommand(date, time, false, true);
        }
        //if both date and time are not present
        else if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));
        }
        //otherwise, means both data and time are present
        this.date = ParserUtil.parseDay(argMultimap.getValue(PREFIX_DATE).get());
        this.time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get());

        return new GroupCommand(date, time, true, true);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent (ArgumentMultimap argumentMultimap, Prefix...prefixes){
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

