package tutorhelper.logic.parser;

import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import tutorhelper.logic.commands.AddCommand;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.student.Address;
import tutorhelper.model.student.Email;
import tutorhelper.model.student.Name;
import tutorhelper.model.student.Phone;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;
import tutorhelper.model.tag.Tag;
import tutorhelper.model.tuitiontiming.TuitionTiming;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        try {
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_PHONE, CliSyntax.PREFIX_EMAIL,
                            CliSyntax.PREFIX_ADDRESS, CliSyntax.PREFIX_SUBJECT, CliSyntax.PREFIX_DAY_AND_TIME, CliSyntax.PREFIX_TAG);

            if (!arePrefixesPresent(argMultimap, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_PHONE, CliSyntax.PREFIX_EMAIL,
                    CliSyntax.PREFIX_ADDRESS, CliSyntax.PREFIX_SUBJECT, CliSyntax.PREFIX_DAY_AND_TIME)
                    || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
            }

            Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get());
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(CliSyntax.PREFIX_PHONE).get());
            Email email = ParserUtil.parseEmail(argMultimap.getValue(CliSyntax.PREFIX_EMAIL).get());
            Address address = ParserUtil.parseAddress(argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).get());
            Set<Subject> subjects = ParserUtil.parseSubjects(argMultimap.getValue(CliSyntax.PREFIX_SUBJECT).get());
            TuitionTiming tuitionTiming = ParserUtil.parseTuitionTiming(
                    argMultimap.getValue(CliSyntax.PREFIX_DAY_AND_TIME).get());
            Set<Tag> tagList = new HashSet<>();
            if (argMultimap.getValue(CliSyntax.PREFIX_TAG).isPresent()) {
                tagList = ParserUtil.parseTags(argMultimap.getValue(CliSyntax.PREFIX_TAG).get());
            }
            Student student = new Student(name, phone, email, address, subjects, tuitionTiming, tagList);
            return new AddCommand(student);

        } catch (ParseException e) {
            throw new ParseException(e.getMessage());
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
