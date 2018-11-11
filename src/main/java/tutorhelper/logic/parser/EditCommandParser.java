package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_DAY_AND_TIME;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_NAME;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_PHONE;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_TAG;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.EditCommand;
import tutorhelper.logic.commands.EditCommand.EditStudentDescriptor;
import tutorhelper.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_SUBJECT, PREFIX_DAY_AND_TIME, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        EditStudentDescriptor editStudentDescriptor = new EditStudentDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editStudentDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editStudentDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editStudentDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editStudentDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_DAY_AND_TIME).isPresent()) {
            editStudentDescriptor.setTuitionTiming(
                    ParserUtil.parseTuitionTiming(argMultimap.getValue(PREFIX_DAY_AND_TIME).get()));
        }
        if (argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
            editStudentDescriptor.setSubjects(ParserUtil.parseSubjects(argMultimap.getValue(PREFIX_SUBJECT).get()));
        }
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            editStudentDescriptor.setTags(ParserUtil.parseTags(argMultimap.getValue(PREFIX_TAG).get()));
        }

        if (!editStudentDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editStudentDescriptor);
    }

}
