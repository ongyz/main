package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CopySubCommand.CopySubFormatChecker.SUBJECT_INDEX;
import static seedu.address.logic.commands.CopySubCommand.CopySubFormatChecker.NUMBER_OF_ARGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CopySubCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new Appemark ndSyllCommand object
 */
public class CopySubCommandParser implements Parser<CopySubCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AppendSyllCommand
     * and returns an AppendSyllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CopySubCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME);

        Index subjectIndex;

        try {
            indexList = ParserUtil.parseIndexes(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE));
        }

        personIndex = getPersonIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);

        if (!argMultimap.getValue(PREFIX_SYLLABUS).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AppendSyllCommand.MESSAGE_USAGE));
        }

        return new AppendSyllCommand(personIndex, subjectIndex, syllabus);
    }

    private static Index getPersonIndex(List<Index> indexList) {
        return indexList.get(PERSON_INDEX);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX);
    }

}
