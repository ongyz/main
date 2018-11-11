package tutorhelper.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_DAY_AND_TIME;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_EMAIL;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_NAME;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_PHONE;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_TAG;

import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;

/**
 * Adds a student to the TutorHelper.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a student to the TutorHelper.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_SUBJECT + "SUBJECT "
            + PREFIX_DAY_AND_TIME + "TUITION TIMING "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_SUBJECT + "Mathematics "
            + PREFIX_DAY_AND_TIME + "Monday 6:00pm "
            + PREFIX_TAG + "friends";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_STUDENT = "This student already exists in the TutorHelper";

    private final Student toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Student}.
     */
    public AddCommand(Student student) {
        requireNonNull(student);
        toAdd = student;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        if (model.hasStudent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_STUDENT);
        }

        model.addStudent(toAdd);
        model.commitTutorHelper();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
