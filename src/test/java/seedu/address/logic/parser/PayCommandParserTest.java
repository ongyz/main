package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_MONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_YEAR;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.PayCommand;
import seedu.address.model.person.Payment;

public class PayCommandParserTest {

    @Test
    public void parse_allFieldPresent_success() {

        String input = " " + PREFIX_PAYMENT + String.valueOf(1)
                + " " + PREFIX_PAYMENT_AMOUNT + String.valueOf(200)
                + " " + PREFIX_PAYMENT_MONTH + String.valueOf(8)
                + " " + PREFIX_PAYMENT_YEAR + String.valueOf(2008);

        PayCommandParser pay = new PayCommandParser();
        Payment expectedPayment = new Payment(INDEX_FIRST_PERSON, 200, 8, 2008);
        assertParseSuccess(pay, input, new PayCommand(expectedPayment));
    }

    @Test
    public void parse_compulsoryFieldAbsent_failure() {

        PayCommandParser pay = new PayCommandParser();

        String indexInput = " " + PREFIX_PAYMENT_AMOUNT + String.valueOf(200)
                + " " + PREFIX_PAYMENT_MONTH + String.valueOf(8)
                + " " + PREFIX_PAYMENT_YEAR + String.valueOf(2018);

        assertParseFailure(pay, indexInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String amountInput = " " + PREFIX_PAYMENT + String.valueOf(1)
                + " " + PREFIX_PAYMENT_MONTH + String.valueOf(8)
                + " " + PREFIX_PAYMENT_YEAR + String.valueOf(2018);
        assertParseFailure(pay, amountInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String monthInput = " " + PREFIX_PAYMENT + String.valueOf(1)
                + " " + PREFIX_PAYMENT_AMOUNT + String.valueOf(200)
                + " " + PREFIX_PAYMENT_YEAR + String.valueOf(2018);
        assertParseFailure(pay, monthInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String yearInput = " " + PREFIX_PAYMENT + String.valueOf(1)
                + " " + PREFIX_PAYMENT_AMOUNT + String.valueOf(200)
                + " " + PREFIX_PAYMENT_MONTH + String.valueOf(8);

        assertParseFailure(pay, yearInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

    }
}
