package seedu.address.logic.commands;

    import java.util.List;
import java.util.logging.Logger;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.Payment;
import seedu.address.model.person.Person;

import static java.util.Objects.requireNonNull;

public class PayCommand extends Command {
    public static final String COMMAND_WORD = "paid";
    public static final String COMMAND_ALIAS = "p";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates if a person has paid.\n"
            + "Parameters: \n"
            + "INDEX(must be a positive integer)\n"
            + "AMOUNT(must be a positive integer)\n"
            + "MONTH(must be a valid month)\n"
            + "YEAR(must be a valid year)\n"
            + "Example: " + COMMAND_WORD + " idx/1 amt/200 mth/08 yr/2018";

    private final Logger logger = LogsCenter.getLogger(PayCommand.class);

    public static final String MESSAGE_PAYMENT_SUCCESS = "Payment for this person is added: %1$s";

    private Index targetIndex;
    private int amount;
    private int month;
    private int year;

    public PayCommand(Payment payment){
        this.targetIndex = payment.getIndex();
        this.amount = payment.getAmount();
        this.month = payment.getMonth();
        this.year = payment.getYear();
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {


        logger.info("----------------[paid index+++++++][" + targetIndex + "]");

        requireNonNull(model);

        List<Person> filteredPersonList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= filteredPersonList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_PAYMENT_SUCCESS, targetIndex.getOneBased()));

      //  return null;
    }

    /*
    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
    */
}
