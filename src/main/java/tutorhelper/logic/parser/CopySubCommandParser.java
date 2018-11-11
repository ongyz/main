package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.CopySubCommand;
import tutorhelper.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CopySubCommand object
 */
public class CopySubCommandParser implements Parser<CopySubCommand> {

    public static final int SOURCE_STUDENT_INDEX = 0;
    public static final int SUBJECT_INDEX = 1;
    public static final int TARGET_STUDENT_INDEX = 2;
    public static final int NUMBER_OF_ARGS = 3;

    /**
     * Parses the given {@code String} of arguments in the context of the CopySubCommand
     * and returns an CopySubCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CopySubCommand parse(String args) throws ParseException {
        requireNonNull(args);
        List<Index> indexList;
        Index sourceStudentIndex;
        Index subjectIndex;
        Index targetStudentIndex;

        try {
            indexList = ParserUtil.parseIndexes(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopySubCommand.MESSAGE_USAGE));
        }

        sourceStudentIndex = getSourceStudentIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);
        targetStudentIndex = getTargetStudentIndex(indexList);

        return new CopySubCommand(sourceStudentIndex, subjectIndex, targetStudentIndex);
    }

    private static Index getSourceStudentIndex(List<Index> indexList) {
        return indexList.get(SOURCE_STUDENT_INDEX);
    }
    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX);
    }
    private static Index getTargetStudentIndex(List<Index> indexList) {
        return indexList.get(TARGET_STUDENT_INDEX);
    }
}
