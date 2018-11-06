package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.EarningsCommand;
import seedu.address.model.person.Payment;

public class EarningsCommandParserTest {

    @Test
    public void parse_allFieldPresent_success() {

        String input = "8 2018";
        EarningsCommandParser earnings = new EarningsCommandParser();
        assertParseSuccess(earnings, input, new EarningsCommand(8, 2018));
    }

    @Test
    public void parse_compulsoryFieldAbsent_failure() {

        EarningsCommandParser earning = new EarningsCommandParser();

        //month missing
        String monthInput = " 2018";
        assertParseFailure(earning, monthInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EarningsCommand.MESSAGE_USAGE));

        //year missing
        String yearInput = "9";
        assertParseFailure(earning, yearInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EarningsCommand.MESSAGE_USAGE));

        //all fields missing
        String monthYearInput = " ";
        assertParseFailure(earning, monthYearInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EarningsCommand.MESSAGE_USAGE));

    }

    @Test
    public void parse_wrongFieldInput_failure() {

        EarningsCommandParser earnings = new EarningsCommandParser();

        String wrongMonthNegativeInput = " -8 2018";
        assertParseFailure(earnings, wrongMonthNegativeInput, String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS));

        String wrongMonthOutOfRangeInput = " 16 2018";
        assertParseFailure(earnings, wrongMonthOutOfRangeInput,
                String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS));

        String wrongMonthOutOfIntegerRangeInput = " 2147483648 2018";
        assertParseFailure(earnings, wrongMonthOutOfIntegerRangeInput,
                String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS));

        String wrongMonthSymbolInput = " * 2018";
        assertParseFailure(earnings, wrongMonthSymbolInput, String.format(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS));

        String wrongYearNegativeInput = " 8 -2018";
        assertParseFailure(earnings, wrongYearNegativeInput, String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS));

        String wrongYearSymbolInput = " 8 &018";
        assertParseFailure(earnings, wrongYearSymbolInput, String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS));

        String wrongYearOutOfRangeInput = " 8 20090";
        assertParseFailure(earnings, wrongYearOutOfRangeInput, String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS));

        String wrongYearOutOfIntegerRangeInput = " 8 2147483648";
        assertParseFailure(earnings, wrongYearOutOfIntegerRangeInput,
                String.format(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS));

    }



}
