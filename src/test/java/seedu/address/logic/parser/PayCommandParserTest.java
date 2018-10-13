package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_MONTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_YEAR;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Assert;
import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PayCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;



public class PayCommandParserTest {

    @Test
    public void parse_AllFieldPresent_success(){

        Payment expectedPayment = new Payment(INDEX_FIRST_PERSON, 200, 8, 2008);
        PayCommand expectedCommand = new PayCommand(expectedPayment);
        
        PayCommand replyCommand = null;
        try {
            String input = " idx/1 amt/200 m/08 y/2008";
            System.out.println(input);
            replyCommand = new PayCommandParser().parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(expectedCommand,(replyCommand));
    }

    @Test
    public void parse_CompulsoryFieldAbsent_failure(){

    }

    @Test
    public void parse_validFieldSyntax_success(){

    }

    @Test
    public void parse_invalidFieldSyntax_failure(){

    }
}
