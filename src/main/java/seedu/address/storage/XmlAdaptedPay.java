package seedu.address.storage;

import java.util.Objects;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Payment;

/**
 * JAXB-friendly adapted version of Pay
 */
public class XmlAdaptedPay {

    private static final Logger logger = LogsCenter.getLogger(XmlAdaptedPay.class);

    private Index studentIndex;
    @XmlElement(required = true)
    private int amount;
    @XmlElement(required = true)
    private int year;
    @XmlElement(required = true)
    private int month;

    /*
     * Constructs an XmlAdaptedPay.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPay(){

    }

    /**
     * Constructs a {@code XmlAdaptedPay} with the given {@code index},
     * {@code amount}, {@code month}, {@code year}
     * @param index index of the student
     * @param amount amount the student paid
     * @param month month in which payment is made
     * @param year year in which payment is made
     */
    public XmlAdaptedPay(Index index, int amount, int month, int year) {
        this.studentIndex = index;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    /**
     * Converts a given Pay into this class for JAXB use.
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedPay(Payment source) {
        this.studentIndex = source.getIndex();
        this.amount = source.getAmount();
        this.month = source.getMonth();
        this.year = source.getYear();
    }

    /**
     * Converts this jaxb-friendly adapted payment object into the model's payment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Payment toModelType() throws IllegalValueException {
        if (!Payment.isValidAmount(amount)) {
            throw new IllegalValueException(Payment.MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS);
        } else if (!Payment.isValidMonth(month)) {
            throw new IllegalValueException(Payment.MESSAGE_PAYMENT_MONTH_CONSTRAINTS);
        } else if (!Payment.isValidYear(year)) {
            throw new IllegalValueException(Payment.MESSAGE_PAYMENT_YEAR_CONSTRAINTS);
        }
        return new Payment(studentIndex, amount, month, year);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPay)) {
            return false;
        }

        return studentIndex.equals(((XmlAdaptedPay) other).studentIndex)
                && amount == (((XmlAdaptedPay) other).amount)
                && month == (((XmlAdaptedPay) other).month)
                && year == (((XmlAdaptedPay) other).year);
    }

    public int hashCode() {
        return Objects.hash(studentIndex, amount, month, year);
    }
}
