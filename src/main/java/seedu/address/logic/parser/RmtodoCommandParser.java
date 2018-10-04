package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.RmtodoCommand.RmtodoFormatChecker.PERSON_INDEX_LOCATION;
import static seedu.address.logic.commands.RmtodoCommand.RmtodoFormatChecker.RMTODO_NUMBER_OF_ARGS;
import static seedu.address.logic.commands.RmtodoCommand.RmtodoFormatChecker.SUBJECT_INDEX_LOCATION;
import static seedu.address.logic.commands.RmtodoCommand.RmtodoFormatChecker.SYLLABUS_INDEX_LOCATION;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RmtodoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RmtodoCommand object
 */
public class RmtodoCommandParser implements Parser<RmtodoCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TodoCommand
     * and returns an TodoCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RmtodoCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Index personIndex;
        Index subjectIndex;
        Index syllabusIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RmtodoCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != RMTODO_NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RmtodoCommand.MESSAGE_USAGE));
        }

        personIndex = getPersonIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);
        syllabusIndex = getSyllabusIndex(indexList);

        return new RmtodoCommand(personIndex, subjectIndex, syllabusIndex);
    }

    private static Index getPersonIndex(List<Index> indexList) {
        return indexList.get(PERSON_INDEX_LOCATION);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX_LOCATION);
    }

    private static Index getSyllabusIndex(List<Index> indexList) {
        return indexList.get(SYLLABUS_INDEX_LOCATION);
    }
}
