package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.EditSyllCommand.EditSyllFormatChecker.NUMBER_OF_ARGS;
import static seedu.address.logic.commands.EditSyllCommand.EditSyllFormatChecker.STUDENT_INDEX;
import static seedu.address.logic.commands.EditSyllCommand.EditSyllFormatChecker.SUBJECT_INDEX;
import static seedu.address.logic.commands.EditSyllCommand.EditSyllFormatChecker.SYLLABUS_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYLLABUS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditSyllCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.subject.Syllabus;

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
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSyllCommand.MESSAGE_USAGE));
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
