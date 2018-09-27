package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.RmtodoCommand.RmtodoFormatChecker.PERSON_INDEX_LOCATION;
import static seedu.address.logic.commands.RmtodoCommand.RmtodoFormatChecker.RMTODO_NUMBER_OF_ARGS;
import static seedu.address.logic.commands.RmtodoCommand.RmtodoFormatChecker.SYLLABUS_INDEX_LOCATION;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RmtodoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class RmtodoCommandParser implements Parser<RmtodoCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TodoCommand
     * and returns an TodoCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RmtodoCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Index index;
        Index syllabusIndex;
        List<Index> indexList;

        try {
            indexList = parseIndexesForRmtodo(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RmtodoCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != RMTODO_NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RmtodoCommand.MESSAGE_USAGE));
        }

        index = getPersonIndex(indexList);
        syllabusIndex = getSyllabusIndex(indexList);

        return new RmtodoCommand(index, syllabusIndex);
    }

    /**
     * Parses {@code oneBasedIndexes} into a list of {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    private static List<Index> parseIndexesForRmtodo(String oneBasedIndexes) throws ParseException {
        String trimmedIndexes = oneBasedIndexes.trim();
        String[] separatedIndexes = trimmedIndexes.split("\\s");
        List<Index> listIndexes = new ArrayList<Index>();

        for (String indexCandidate : separatedIndexes) {
            listIndexes.add(ParserUtil.parseIndex(indexCandidate));
        }

        return listIndexes;
    }

    private static Index getPersonIndex(List<Index> indexList) {
        return indexList.get(PERSON_INDEX_LOCATION);
    }

    private static Index getSyllabusIndex(List<Index> indexList) {
        return indexList.get(SYLLABUS_INDEX_LOCATION);
    }
}
