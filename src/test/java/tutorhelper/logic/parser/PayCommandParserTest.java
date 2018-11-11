package tutorhelper.logic.parser;

import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseFailure;
import static tutorhelper.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;

import org.junit.Test;

import tutorhelper.commons.core.Messages;
import tutorhelper.logic.commands.PayCommand;
import tutorhelper.model.student.Payment;

public class PayCommandParserTest {

    @Test
    public void parse_allFieldPresent_success() {

        String input = "1 200 8 2008";

        PayCommandParser pay = new PayCommandParser();
        Payment expectedPayment = new Payment(INDEX_FIRST_STUDENT, 200, 8, 2008);
        assertParseSuccess(pay, input, new PayCommand(expectedPayment));
    }

    @Test
    public void parse_compulsoryFieldAbsent_failure() {

        PayCommandParser pay = new PayCommandParser();

        String indexInput = "200 8 2018";

        assertParseFailure(pay, indexInput, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String amountInput = "1 8 2018";
        assertParseFailure(pay, amountInput, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String monthInput = "1 200 2018";
        assertParseFailure(pay, monthInput, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

        String yearInput = "1 200 8";

        assertParseFailure(pay, yearInput, String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PayCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_wrongFieldInput_failure() {

        PayCommandParser pay = new PayCommandParser();

        String wrongIndexInput = "-1 200 8 2018";
        assertParseFailure(pay, wrongIndexInput, String.format(ParserUtil.MESSAGE_INVALID_INDEX,
                PayCommand.MESSAGE_USAGE));

        String wrongAmountInput = "1 $200 8 2018";
        assertParseFailure(pay, wrongAmountInput, String.format(Payment.MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String outOfBoundAmountInput = "1 100001 8 2018";
        assertParseFailure(pay, outOfBoundAmountInput, String.format(Payment.MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String wrongMonthInput = "1 200 16 2018";
        assertParseFailure(pay, wrongMonthInput, String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String outOfBoundMonthInput = "1 200 2147483648 2018";
        assertParseFailure(pay, outOfBoundMonthInput, String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String wrongYearInput = "1 200 8 #2018";
        assertParseFailure(pay, wrongYearInput, String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String wrongYearInputDigits = "1 200 8 018";
        assertParseFailure(pay, wrongYearInputDigits, String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));

        String outOfBoundYearInputDigits = "1 200 8 20018";
        assertParseFailure(pay, outOfBoundYearInputDigits, String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS,
                PayCommand.MESSAGE_USAGE));
    }
}
