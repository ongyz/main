package seedu.address.logic.commands;

import org.junit.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Payment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

public class PayCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    
    @Test
    public void equals() {

        Payment alicePayment = new Payment(INDEX_FIRST_PERSON, 200, 10, 2018);
        Payment bobPayment = new Payment(INDEX_SECOND_PERSON, 200, 10, 2018);

        PayCommand payAliceCommand = new PayCommand(alicePayment);
        PayCommand payBobCommand = new PayCommand(bobPayment);

        // same object -> returns true
        assertTrue(payAliceCommand.equals(payAliceCommand));

        // same values -> returns true
        PayCommand payAliceCommandCopy = new PayCommand(alicePayment);
        assertTrue(payAliceCommand.equals(payAliceCommandCopy));

        // different types -> returns false
        assertFalse(payAliceCommand.equals(1));

        // null -> returns false
        assertFalse(payAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(payAliceCommand.equals(payBobCommand));
    }

}