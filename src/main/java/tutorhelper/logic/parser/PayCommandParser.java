package tutorhelper.logic.parser;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tutorhelper.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.commands.PayCommand;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.student.Payment;

/**
 * Parses input arguments and creates a new PayCommand object
 */
public class PayCommandParser implements Parser<PayCommand> {

    private static final int FOUR_ARGUMENTS = 4;

    private Index studentIndex;
    private int amount;
    private int month;
    private int year;

    /**
     * Parses the given {@code String} of arguments in the context of the PayCommand
     * and returns an PayCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PayCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedPayment = args.trim();
        String[] separatedPayment = trimmedPayment.split("\\s");

        if (separatedPayment.length != FOUR_ARGUMENTS) { //invalid number of arguments
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));
        }
        //Put the arguments into ParserUtil to check for validity
        try {
            this.studentIndex = ParserUtil.parseIndex(separatedPayment[0]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_INDEX, PayCommand.MESSAGE_USAGE), e);
        }
        try {
            this.amount = ParserUtil.parseAmount(separatedPayment[1]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(Payment.MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS, PayCommand.MESSAGE_USAGE), e);
        }
        try {
            this.month = ParserUtil.parseMonth(separatedPayment[2]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS, PayCommand.MESSAGE_USAGE), e);
        }
        try {
            this.year = ParserUtil.parseYear(separatedPayment[3]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS, PayCommand.MESSAGE_USAGE), e);
        }

        //all input are valid and can be added
        Payment payment = new Payment(studentIndex, amount, month, year);
        return new PayCommand(payment);
    }

}
