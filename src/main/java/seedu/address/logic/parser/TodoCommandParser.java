package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.TodoCommand.TodoFormatChecker.PERSON_INDEX_LOCATION;
import static seedu.address.logic.commands.TodoCommand.TodoFormatChecker.SUBJECT_INDEX_LOCATION;
import static seedu.address.logic.commands.TodoCommand.TodoFormatChecker.TODO_NUMBER_OF_ARGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYLLABUS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TodoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.subject.Syllabus;

/**
 * Parses input arguments and creates a new TodoCommand object
 */
public class TodoCommandParser implements Parser<TodoCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TodoCommand
     * and returns an TodoCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TodoCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SYLLABUS);

        Index personIndex;
        Index subjectIndex;
        List<Index> indexList;

        try {
            indexList = ParserUtil.parseIndexes(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TodoCommand.MESSAGE_USAGE), pe);
        }

        if (indexList.size() != TODO_NUMBER_OF_ARGS) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TodoCommand.MESSAGE_USAGE));
        }

        personIndex = getPersonIndex(indexList);
        subjectIndex = getSubjectIndex(indexList);

        if (!argMultimap.getValue(PREFIX_SYLLABUS).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TodoCommand.MESSAGE_USAGE));
        }

        Syllabus syllabus = ParserUtil.parseSyllabus(argMultimap.getValue(PREFIX_SYLLABUS).get());
        return new TodoCommand(personIndex, subjectIndex, syllabus);
    }

    private static Index getPersonIndex(List<Index> indexList) {
        return indexList.get(PERSON_INDEX_LOCATION);
    }

    private static Index getSubjectIndex(List<Index> indexList) {
        return indexList.get(SUBJECT_INDEX_LOCATION);
    }

}
