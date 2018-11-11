package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.AddSubCommand.AddSubFormatChecker.NUMBER_OF_ARGS;
import static seedu.address.logic.commands.AddSubCommand.AddSubFormatChecker.STUDENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddSubCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.subject.Subject;

/**
 * Parses input arguments and creates a new AddSubCommand object.
 */
public class AddSubCommandParser implements Parser<AddSubCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddSubCommand
     * and returns an AddSubCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddSubCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT);

        Index studentIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSubCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSubCommand.MESSAGE_USAGE));
        }

        studentIndex = getStudentIndex(indexList);

        if (!argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSubCommand.MESSAGE_USAGE));
        }

        Subject subject = ParserUtil.parseSubject(argMultimap.getValue(PREFIX_SUBJECT).get());
        return new AddSubCommand(studentIndex, subject);
    }

    private static Index getStudentIndex(List<Index> indexList) {
        return indexList.get(STUDENT_INDEX);
    }

}
