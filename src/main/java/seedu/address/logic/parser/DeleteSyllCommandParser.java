package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.DeleteSyllCommand.DeleteSyllFormatChecker.NUMBER_OF_ARGS;
import static seedu.address.logic.commands.DeleteSyllCommand.DeleteSyllFormatChecker.STUDENT_INDEX;
import static seedu.address.logic.commands.DeleteSyllCommand.DeleteSyllFormatChecker.SUBJECT_INDEX;
import static seedu.address.logic.commands.DeleteSyllCommand.DeleteSyllFormatChecker.SYLLABUS_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteSyllCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteSyllCommand object
 */
public class DeleteSyllCommandParser implements Parser<DeleteSyllCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddSyllCommand
     * and returns an AddSyllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteSyllCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Index studentIndex;
        Index subjectIndex;
        Index syllabusIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSyllCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSyllCommand.MESSAGE_USAGE));
        }

        studentIndex = getStudentIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);
        syllabusIndex = getSyllabusIndex(indexList);

        return new DeleteSyllCommand(studentIndex, subjectIndex, syllabusIndex);
    }

    private static Index getStudentIndex(List<Index> indexList) {
        return indexList.get(STUDENT_INDEX);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX);
    }

    private static Index getSyllabusIndex(List<Index> indexList) {
        return indexList.get(SYLLABUS_INDEX);
    }
}
