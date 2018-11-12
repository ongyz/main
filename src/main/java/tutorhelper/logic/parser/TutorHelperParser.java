package tutorhelper.logic.parser;

import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tutorhelper.logic.commands.AddCommand;
import tutorhelper.logic.commands.AddSubCommand;
import tutorhelper.logic.commands.AddSyllCommand;
import tutorhelper.logic.commands.ClearCommand;
import tutorhelper.logic.commands.Command;
import tutorhelper.logic.commands.CopySubCommand;
import tutorhelper.logic.commands.DeleteCommand;
import tutorhelper.logic.commands.DeleteSubCommand;
import tutorhelper.logic.commands.DeleteSyllCommand;
import tutorhelper.logic.commands.EarningsCommand;
import tutorhelper.logic.commands.EditCommand;
import tutorhelper.logic.commands.EditSyllCommand;
import tutorhelper.logic.commands.ExitCommand;
import tutorhelper.logic.commands.FindCommand;
import tutorhelper.logic.commands.GroupCommand;
import tutorhelper.logic.commands.HelpCommand;
import tutorhelper.logic.commands.HistoryCommand;
import tutorhelper.logic.commands.ListCommand;
import tutorhelper.logic.commands.MarkCommand;
import tutorhelper.logic.commands.PayCommand;
import tutorhelper.logic.commands.RedoCommand;
import tutorhelper.logic.commands.SelectCommand;
import tutorhelper.logic.commands.UndoCommand;
import tutorhelper.logic.parser.exceptions.ParseException;


/**
 * Parses user input.
 */
public class TutorHelperParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case PayCommand.COMMAND_WORD:
            return new PayCommandParser().parse(arguments);

        case EarningsCommand.COMMAND_WORD:
            return new EarningsCommandParser().parse(arguments);

        case AddSyllCommand.COMMAND_WORD:
            return new AddSyllCommandParser().parse(arguments);

        case DeleteSyllCommand.COMMAND_WORD:
            return new DeleteSyllCommandParser().parse(arguments);

        case MarkCommand.COMMAND_WORD:
            return new MarkCommandParser().parse(arguments);

        case CopySubCommand.COMMAND_WORD:
            return new CopySubCommandParser().parse(arguments);

        case GroupCommand.COMMAND_WORD:
            return new GroupCommandParser().parse(arguments);

        case EditSyllCommand.COMMAND_WORD:
            return new EditSyllCommandParser().parse(arguments);

        case AddSubCommand.COMMAND_WORD:
            return new AddSubCommandParser().parse(arguments);

        case DeleteSubCommand.COMMAND_WORD:
            return new DeleteSubCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
