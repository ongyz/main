package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.EarningsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EarningsCommand object
 */
public class EarningsCommandParser implements Parser<EarningsCommand> {

    private int month;
    private int year;
    /**
     * Parses the given {@code String} of arguments in the context of the EarningsCommand
     * and returns an EarningsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EarningsCommand parse(String args) throws ParseException {

        String trimmedEarnings = args.trim();
        String[] separatedEarnings = trimmedEarnings.split("\\s");

        if (separatedEarnings.length != 2) { //invalid number of fields
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EarningsCommand.MESSAGE_USAGE));
        }
        String inputMonth = separatedEarnings[0];
        String inputYear = separatedEarnings[1];

        //Put the arguments into ParserUtil to check for validity
        this.month = ParserUtil.parseMonth(inputMonth);
        this.year = ParserUtil.parseYear(inputYear);
        return new EarningsCommand(month, year);
    }
}
