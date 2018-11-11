package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.logic.commands.AddSyllCommand.AddSyllFormatChecker.NUMBER_OF_ARGS;
import static tutorhelper.logic.commands.AddSyllCommand.AddSyllFormatChecker.STUDENT_INDEX;
import static tutorhelper.logic.commands.AddSyllCommand.AddSyllFormatChecker.SUBJECT_INDEX;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_SYLLABUS;

import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.AddSyllCommand;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.subject.Syllabus;

/**
 * Parses input arguments and creates a new AddSyllCommand object
 */
public class AddSyllCommandParser implements Parser<AddSyllCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddSyllCommand
     * and returns an AddSyllCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddSyllCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SYLLABUS);

        Index studentIndex;
        Index subjectIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE));
        }

        studentIndex = getStudentIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);

        if (!argMultimap.getValue(PREFIX_SYLLABUS).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSyllCommand.MESSAGE_USAGE));
        }

        List<Syllabus> syllabuses = ParserUtil.parseSyllabuses(argMultimap.getValue(PREFIX_SYLLABUS).get());
        return new AddSyllCommand(studentIndex, subjectIndex, syllabuses);
    }

    private static Index getStudentIndex(List<Index> indexList) {
        return indexList.get(STUDENT_INDEX);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX);
    }

}
