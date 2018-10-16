package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.EraseSyllCommand.EraseSyllFormatChecker.PERSON_INDEX_LOCATION;
import static seedu.address.logic.commands.EraseSyllCommand.EraseSyllFormatChecker.RMTODO_NUMBER_OF_ARGS;
import static seedu.address.logic.commands.EraseSyllCommand.EraseSyllFormatChecker.SUBJECT_INDEX_LOCATION;
import static seedu.address.logic.commands.EraseSyllCommand.EraseSyllFormatChecker.SYLLABUS_INDEX_LOCATION;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EraseSyllCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EraseSyllCommand object
 */
public class EraseSyllCommandParser implements Parser<EraseSyllCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AppendSyllCommand
     * and returns an AppendSyllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EraseSyllCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Index personIndex;
        Index subjectIndex;
        Index syllabusIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EraseSyllCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != RMTODO_NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EraseSyllCommand.MESSAGE_USAGE));
        }

        personIndex = getPersonIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);
        syllabusIndex = getSyllabusIndex(indexList);

        return new EraseSyllCommand(personIndex, subjectIndex, syllabusIndex);
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
