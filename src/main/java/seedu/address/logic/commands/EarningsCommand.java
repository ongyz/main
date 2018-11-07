package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Payment;
import seedu.address.model.student.Student;

/**
 * Displays earning of the month, year.
 */
public class EarningsCommand extends Command {
    public static final String COMMAND_WORD = "earnings";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows earnings of the month\n"
            + "Parameters: "
            + "MONTH YEAR \n"
            + "Example: " + COMMAND_WORD + " 08 2018 ";

    private int month;
    private int year;
    private long earnings = 0;

    public EarningsCommand(int month, int year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {

        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        for (int i = 0; i < lastShownList.size(); i++) {
            List<Payment> studentPaymentsList = lastShownList.get(i).getPayments();
            for (int j = 0; j < studentPaymentsList.size(); j++) {
                Payment thisPayment = studentPaymentsList.get(j);
                if (thisPayment.getMonth() == this.month && thisPayment.getYear() == this.year) {
                    this.earnings += (thisPayment.getAmount());
                }
            }
        }
        return new CommandResult("Earnings: $" + String.valueOf(earnings));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EarningsCommand)) {
            return false;
        }

        // state check
        EarningsCommand e = (EarningsCommand) other;
        return month == ((EarningsCommand) other).month
                && year == ((EarningsCommand) other).year;
    }
}
