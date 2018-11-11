package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.EditSyllCommand;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.subject.Syllabus;
import tutorhelper.commons.core.Messages;

/**
 * Parses input arguments and creates a new EditSyllCommand object
 */
public class EditSyllCommandParser implements Parser<EditSyllCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditSyllCommand
     * and returns an EditSyllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditSyllCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_SYLLABUS);

        Index studentIndex;
        Index subjectIndex;
        Index syllabusIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != EditSyllCommand.EditSyllFormatChecker.NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));
        }

        studentIndex = getStudentIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);
        syllabusIndex = getSyllabusIndex(indexList);

        if (!argMultimap.getValue(CliSyntax.PREFIX_SYLLABUS).isPresent()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));
        }

        Syllabus syllabus = ParserUtil.parseSyllabus(argMultimap.getValue(CliSyntax.PREFIX_SYLLABUS).get());
        return new EditSyllCommand(studentIndex, subjectIndex, syllabusIndex, syllabus);
    }

    private static Index getStudentIndex(List<Index> indexList) {
        return indexList.get(EditSyllCommand.EditSyllFormatChecker.STUDENT_INDEX);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(EditSyllCommand.EditSyllFormatChecker.SUBJECT_INDEX);
    }

    private static Index getSyllabusIndex(List<Index> indexList) {
        return indexList.get(EditSyllCommand.EditSyllFormatChecker.SYLLABUS_INDEX);
    }
}
