package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.PayCommand;
import seedu.address.model.person.Payment;

public class PayCommandParserTest {

    @Test
    public void parse_allFieldPresent_success() {

        String input = "1 200 8 2008";

        PayCommandParser pay = new PayCommandParser();
        Payment expectedPayment = new Payment(INDEX_FIRST_PERSON, 200, 8, 2008);
        assertParseSuccess(pay, input, new PayCommand(expectedPayment));
    }

    @Test
    public void parse_compulsoryFieldAbsent_failure() {

        PayCommandParser pay = new PayCommandParser();

        String indexInput = "200 8 2018";

        assertParseFailure(pay, indexInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String amountInput = "1 8 2018";
        assertParseFailure(pay, amountInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String monthInput = "1 200 2018";
        assertParseFailure(pay, monthInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String yearInput = "1 200 8";

        assertParseFailure(pay, yearInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_wrongFieldInput_failure() {

        PayCommandParser pay = new PayCommandParser();

        String wrongIndexInput = "-1 200 8 2018";
        assertParseFailure(pay, wrongIndexInput, String.format(MESSAGE_INVALID_INDEX,
                PayCommand.MESSAGE_USAGE));

        String wrongAmountInput = "1 $200 8 2018";
        assertParseFailure(pay, wrongAmountInput, String.format(Payment.MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String wrongMonthInput = "1 200 16 2018";

        assertParseFailure(pay, wrongMonthInput, String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String wrongYearInput = "1 200 8 #2018";
        assertParseFailure(pay, wrongYearInput, String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

    }
}
