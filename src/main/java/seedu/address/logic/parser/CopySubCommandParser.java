package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CopySubCommand.CopySubFormatChecker.NUMBER_OF_ARGS;
import static seedu.address.logic.commands.CopySubCommand.CopySubFormatChecker.SOURCE_STUDENT_INDEX;
import static seedu.address.logic.commands.CopySubCommand.CopySubFormatChecker.SUBJECT_INDEX;
import static seedu.address.logic.commands.CopySubCommand.CopySubFormatChecker.TARGET_STUDENT_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CopySubCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CopySubCommand object
 */
public class CopySubCommandParser implements Parser<CopySubCommand> {

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
