package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_SUBJECT = new Prefix("s/");
    public static final Prefix PREFIX_DAYANDTIME = new Prefix("d/");
    public static final Prefix PREFIX_PAYMENT = new Prefix("idx/");
    public static final Prefix PREFIX_PAYMENTAMOUNT = new Prefix("amt/");
    public static final Prefix PREFIX_PAYMENTMONTH = new Prefix("mth/");
    public static final Prefix PREFIX_PAYMENTYEAR = new Prefix("yr/");
}
