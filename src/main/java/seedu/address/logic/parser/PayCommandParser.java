package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PayCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Payment;

/**
 * Parses input arguments and creates a new PayCommand object
 */
public class PayCommandParser implements Parser<PayCommand> {

    private Index personIndex;
    private int amount;
    private int month;
    private int year;

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PayCommand parse(String args) throws ParseException {
        requireNonNull(args);

        try {
            String trimmedPayment = args.trim();
            String[] separatedPayment = trimmedPayment.split("\\s");

            if (separatedPayment.length != 4) { //invalid number of arguments
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));
            }
            String inputpersonIndex = (separatedPayment[0]);
            String inputAmount = separatedPayment[1];
            String inputMonth = separatedPayment[2];
            String inputYear = separatedPayment[3];

            //Put the arguments into ParserUtil to check for validity
            this.personIndex = ParserUtil.parseIndex(inputpersonIndex);
            this.amount = ParserUtil.parseAmount(inputAmount);
            this.month = ParserUtil.parseMonth(inputMonth);
            this.year = ParserUtil.parseYear(inputYear);

            //all input are valid and can be added
            Payment payment = new Payment(personIndex, amount, month, year);
            return new PayCommand(payment);

        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE), pe);
        }
    }

}
