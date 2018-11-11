package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.logic.commands.DeleteSubCommand.DeleteSubFormatChecker.NUMBER_OF_ARGS;
import static tutorhelper.logic.commands.DeleteSubCommand.DeleteSubFormatChecker.STUDENT_INDEX;
import static tutorhelper.logic.commands.DeleteSubCommand.DeleteSubFormatChecker.SUBJECT_INDEX;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.DeleteSubCommand;
import tutorhelper.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteSubCommand object.
 */
public class DeleteSubCommandParser implements Parser<DeleteSubCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteSubCommand
     * and returns an DeleteSubCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public DeleteSubCommand parse(String args) throws ParseException {
        requireNonNull(args);

        Index studentIndex;
        Index subjectIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(args);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSubCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteSubCommand.MESSAGE_USAGE));
        }

        studentIndex = getStudentIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);

        return new DeleteSubCommand(studentIndex, subjectIndex);
    }

    private static Index getStudentIndex(List<Index> indexList) {
        return indexList.get(STUDENT_INDEX);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX);
    }

}
