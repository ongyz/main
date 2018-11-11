package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.AddSubCommand;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.subject.Subject;

/**
 * Parses input arguments and creates a new AddSubCommand object.
 */
public class AddSubCommandParser implements Parser<AddSubCommand> {

    public static final int STUDENT_INDEX = 0;
    public static final int NUMBER_OF_ARGS = 1;

    /**
     * Parses the given {@code String} of arguments in the context of the AddSubCommand
     * and returns an AddSubCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddSubCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_SUBJECT);

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

        if (!argMultimap.getValue(CliSyntax.PREFIX_SUBJECT).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddSubCommand.MESSAGE_USAGE));
        }

        Subject subject = ParserUtil.parseSubject(argMultimap.getValue(CliSyntax.PREFIX_SUBJECT).get());
        return new AddSubCommand(studentIndex, subject);
    }

    private static Index getStudentIndex(List<Index> indexList) {
        return indexList.get(STUDENT_INDEX);
    }

}
