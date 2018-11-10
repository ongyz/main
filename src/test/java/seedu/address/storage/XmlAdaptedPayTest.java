package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.PayCommand;
import seedu.address.model.student.Payment;
import seedu.address.testutil.Assert;

public class XmlAdaptedPayTest {

    private static final String INVALID_AMOUNT = "$233";
    private static final String INVALID_MONTH_NOT_INTEGER = "NOVEMBER";
    private static final String INVALID_MONTH_WRONG_RANGE = "16";
    private static final String INVALID_YEAR = "TwoThousand";

    private static final Index VALID_INDEX = Index.fromOneBased(1);
    private static final String VALID_AMOUNT = "200";
    private static final String VALID_MONTH = "11";
    private static final String VALID_YEAR = "2018";

    @Test
    public void toModelType_validStudentDetails_returnsStudent() throws Exception {
        Payment pay = new Payment(Index.fromOneBased(1), 200, 11, 2018);
        XmlAdaptedPay payment = new XmlAdaptedPay(VALID_INDEX, VALID_AMOUNT, VALID_MONTH, VALID_YEAR);
        assertEquals(pay, payment.toModelType());
    }

    @Test
    public void toModelType_invalidAmount_throwsIllegalValueException() {

        XmlAdaptedPay pay = new XmlAdaptedPay(VALID_INDEX, INVALID_AMOUNT, VALID_MONTH, VALID_YEAR);
        String expectedMessage = PayCommand.MESSAGE_USAGE;
        Assert.assertThrows(NumberFormatException.class, expectedMessage, pay::toModelType);

    }

    @Test
    public void toModelType_nullAmount_throwsIllegalValueException() {

        XmlAdaptedPay pay = new XmlAdaptedPay(VALID_INDEX, null, VALID_MONTH, VALID_YEAR);
        String expectedMessage = PayCommand.MESSAGE_USAGE;
        Assert.assertThrows(NumberFormatException.class, expectedMessage, pay::toModelType);

    }

    @Test
    public void toModelType_invalidMonth_throwsIllegalValueException() {

        XmlAdaptedPay pay = new XmlAdaptedPay(VALID_INDEX, VALID_AMOUNT, INVALID_MONTH_NOT_INTEGER, VALID_YEAR);
        String expectedMessage = PayCommand.MESSAGE_USAGE;
        Assert.assertThrows(NumberFormatException.class, expectedMessage, pay::toModelType);

        XmlAdaptedPay pay2 = new XmlAdaptedPay(VALID_INDEX, VALID_AMOUNT, INVALID_MONTH_WRONG_RANGE, VALID_YEAR);
        String expectedMessage2 = Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage2, pay2::toModelType);

    }

    @Test
    public void toModelType_nullMonth_throwsIllegalValueException() {

        XmlAdaptedPay pay = new XmlAdaptedPay(VALID_INDEX, VALID_AMOUNT, null, VALID_YEAR);
        String expectedMessage = PayCommand.MESSAGE_USAGE;
        Assert.assertThrows(NumberFormatException.class, expectedMessage, pay::toModelType);

    }

    @Test
    public void toModelType_invalidYear_throwsIllegalValueException() {

        XmlAdaptedPay pay = new XmlAdaptedPay(VALID_INDEX, VALID_AMOUNT, VALID_MONTH, INVALID_YEAR);
        String expectedMessage = PayCommand.MESSAGE_USAGE;
        Assert.assertThrows(NumberFormatException.class, expectedMessage, pay::toModelType);

    }

    @Test
    public void toModelType_nullYear_throwsIllegalValueException() {

        XmlAdaptedPay pay = new XmlAdaptedPay(VALID_INDEX, VALID_AMOUNT, VALID_MONTH, INVALID_YEAR);
        String expectedMessage = PayCommand.MESSAGE_USAGE;
        Assert.assertThrows(NumberFormatException.class, expectedMessage, pay::toModelType);

    }



}
