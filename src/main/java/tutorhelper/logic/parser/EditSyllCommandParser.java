package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_SYLLABUS;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.EditSyllCommand;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.subject.Syllabus;

/**
 * Parses input arguments and creates a new EditSyllCommand object
 */
public class EditSyllCommandParser implements Parser<EditSyllCommand> {

    public static final int STUDENT_INDEX = 0;
    public static final int SUBJECT_INDEX = 1;
    public static final int SYLLABUS_INDEX = 2;
    public static final int NUMBER_OF_ARGS = 3;

    /**
     * Parses the given {@code String} of arguments in the context of the EditSyllCommand
     * and returns an EditSyllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditSyllCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SYLLABUS);

        Index studentIndex;
        Index subjectIndex;
        Index syllabusIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));
        }

        studentIndex = getStudentIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);
        syllabusIndex = getSyllabusIndex(indexList);

        if (!argMultimap.getValue(PREFIX_SYLLABUS).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditSyllCommand.MESSAGE_USAGE));
        }

        Syllabus syllabus = ParserUtil.parseSyllabus(argMultimap.getValue(PREFIX_SYLLABUS).get());
        return new EditSyllCommand(studentIndex, subjectIndex, syllabusIndex, syllabus);
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
