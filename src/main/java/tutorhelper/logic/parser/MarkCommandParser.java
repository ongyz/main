package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.MarkCommand;
import tutorhelper.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new MarkCommand object
 */
public class MarkCommandParser implements Parser<MarkCommand> {

    public static final int STUDENT_INDEX_LOCATION = 0;
    public static final int SUBJECT_INDEX_LOCATION = 1;
    public static final int SYLLABUS_INDEX_LOCATION = 2;
    public static final int MARK_NUMBER_OF_ARGS = 3;

    /**
     * Parses the given {@code String} of arguments in the context of the MarkCommand
     * and returns an MarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Index studentIndex;
        Index subjectIndex;
        Index syllabusIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != MARK_NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        studentIndex = getStudentIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);
        syllabusIndex = getSyllabusIndex(indexList);

        return new MarkCommand(studentIndex, subjectIndex, syllabusIndex);
    }

    private static Index getStudentIndex(List<Index> indexList) {
        return indexList.get(STUDENT_INDEX_LOCATION);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX_LOCATION);
    }

    private static Index getSyllabusIndex(List<Index> indexList) {
        return indexList.get(SYLLABUS_INDEX_LOCATION);
    }
}
