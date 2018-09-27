package seedu.address.model;

import seedu.address.commons.core.index.Index;

public class Payment {

    public static final String MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS =
            "Amount of payment should only contain numbers";
    public static final String MESSAGE_PAYMENT_MONTH_CONSTRAINTS =
            "Month of payment should only contain numbers";
    public static final String MESSAGE_PAYMENT_YEAR_CONSTRAINTS =
            "Year of payment should only contain numbers";

    public static final String TAG_VALIDATION_REGEX = "(.)*(\\d)(.)*";

    private static Index studentIndex;
    private final int amount;
    private final int month;
    private final int year;

    /**
     * Constructs a {@code Tag}.
     *
     * @param index A valid index name.
     * @param amount A valid amount.
     * @param month A valid month value.
     * @param year A valid year value
     */
    public Payment(Index index, int amount, int month, int year ) {
        this.studentIndex = index;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    /**
     * Returns true if a given string is a valid number.
     */
    public static boolean isValidAmount(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }


    /**
     * Returns true if a given string is a valid month.
     */
    public static boolean isValidMonth(String test) {

        //Check if month is within the correct range of jan - dec
        if(test.matches(TAG_VALIDATION_REGEX)){
            int testMonth = Integer.parseInt(test);
            if(testMonth>=1 && testMonth<=12) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns student index
     */
    public Index getIndex(){
        return this.studentIndex;
    }

    /*
     * Return month of payment
     */
    public int getMonth(){
        return this.month;
    }

    /*
     * Returns year of payment
     */
    public int getYear(){
        return this.year;
    }

    /*
     * Return amount of payment
     */
    public int getAmount(){
        return this.amount;
    }

    /**
     * Returns true if a given string is a valid year.
     */
    public static boolean isValidYear(String test) {
        return test.matches(TAG_VALIDATION_REGEX);
    }


/*
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && .equals(((Tag) other).tagName)); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     *
    public String toString() {
        return '[' + tagName + ']';
    }
*/

}


