package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.DeleteSyllCommand;
import tutorhelper.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteSyllCommand object
 */
public class DeleteSyllCommandParser implements Parser<DeleteSyllCommand> {

    public static final int STUDENT_INDEX = 0;
    public static final int SUBJECT_INDEX = 1;
    public static final int SYLLABUS_INDEX = 2;
    public static final int NUMBER_OF_ARGS = 3;

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
