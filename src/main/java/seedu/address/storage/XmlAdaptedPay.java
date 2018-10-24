package seedu.address.storage;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.PayCommand;
import seedu.address.model.person.Payment;

/**
 * JAXB-friendly adapted version of Pay
 */
public class XmlAdaptedPay {

    private Index studentIndex;
    @XmlElement(required = true)
    private String amount;
    @XmlElement(required = true)
    private String year;
    @XmlElement(required = true)
    private String month;

    /*
     * Constructs an XmlAdaptedPay.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPay(){}

    /**
     * Constructs a {@code XmlAdaptedPay} with the given {@code index},
     * {@code amount}, {@code month}, {@code year}
     * @param index index of the student
     * @param amount amount the student paid
     * @param month month in which payment is made
     * @param year year in which payment is made
     */
    public XmlAdaptedPay(Index index, String amount, String month, String year) {
        this.studentIndex = index;
        this.amount = String.valueOf(amount);
        this.month = String.valueOf(month);
        this.year = String.valueOf(year);
    }

    /**
     * Converts a given Pay into this class for JAXB use.
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedPay(Payment source) {
        this.studentIndex = source.getIndex();
        this.amount = String.valueOf(source.getAmount());
        this.month = String.valueOf(source.getMonth());
        this.year = String.valueOf(source.getYear());
    }

    /**
     * Converts this jaxb-friendly adapted payment object into the model's payment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Payment toModelType() throws IllegalValueException {

        int integerAmount;
        int integerMonth;
        int integerYear;

        //check if can convert string to integer
        try {
            integerAmount = Integer.valueOf(amount);
            integerMonth = Integer.valueOf(month);
            integerYear = Integer.valueOf(year);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(PayCommand.MESSAGE_USAGE);
        }

        //check if the integer value is of a valid form
        if (!Payment.isValidAmount(integerAmount)) {
            throw new IllegalValueException(Payment.MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS);
        } else if (!Payment.isValidMonth(integerMonth)) {
            throw new IllegalValueException(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS);
        } else if (!Payment.isValidYear(integerYear)) {
            throw new IllegalValueException(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS);
        }

        return new Payment(studentIndex, Integer.valueOf(amount),
                Integer.valueOf(month), Integer.valueOf(year));
    }

    /**
     * Creates a list of {@code XmlAdaptedPay} for testing.
     * @return list of {@code XmlAdaptedPay}.
     */
    public static List<XmlAdaptedPay> setUpTestPaymentValid() {
        XmlAdaptedPay validPayment = new XmlAdaptedPay(Index.fromZeroBased(1), "200", "2" , "2018");
        return Collections.singletonList(validPayment);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPay)) {
            return false;
        }


        return amount.equals(((XmlAdaptedPay) other).amount)
                && month.equals(((XmlAdaptedPay) other).month)
                && year.equals(((XmlAdaptedPay) other).year);
    }

    public int hashCode() {
        return Objects.hash(studentIndex, amount, month, year);
    }
}
