package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PayCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {

        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        assertExecutionSuccess(INDEX_FIRST_PERSON);
        assertExecutionSuccess(INDEX_SECOND_PERSON);
        assertExecutionSuccess(INDEX_THIRD_PERSON);
        assertExecutionSuccess(lastPersonIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_updatePaymentMethod_success() {
        Payment originalPayment = new Payment(INDEX_FIRST_PERSON, 100, 11, 1998);
        PayCommand originalPayCommand = new PayCommand(originalPayment);

        List<Payment> oldPaymentList = new ArrayList<>();
        oldPaymentList.add(originalPayment);

        Payment newPayment = new Payment(INDEX_FIRST_PERSON, 100, 11, 2018);
        List<Payment> actualReturnedPayment = originalPayCommand.updatePayment(oldPaymentList, newPayment);

        List<Payment> expectedReturnedPaymentList = new ArrayList<>();
        expectedReturnedPaymentList.add(originalPayment);
        expectedReturnedPaymentList.add(newPayment);

        assertEquals(actualReturnedPayment, expectedReturnedPaymentList);

    }
    /**
     * Executes a {@code PayCommand} with the given {@code index}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {

        Payment payment = new Payment(index, 200, 9, 2020);
        PayCommand payCommand = new PayCommand(payment);

        Person personOriginal = model.getFilteredPersonList().get(index.getZeroBased());
        Person personOriginalClone = new PersonBuilder(personOriginal).build();
        Person expectedPerson = new PersonBuilder(personOriginalClone).withPayments(payment).build();

        String expectedMessage = String.format(PayCommand.MESSAGE_PAYMENT_SUCCESS, expectedPerson);
        expectedModel.updatePerson(personOriginal, expectedPerson);
        expectedModel.commitAddressBook();

        assertCommandSuccess(payCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Executes a {@code PayCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        Payment payment = new Payment(index, 200, 9, 2020);
        PayCommand payCommand = new PayCommand(payment);
        assertCommandFailure(payCommand, model, commandHistory, expectedMessage);
    }

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

        // different payment -> returns false
        assertFalse(payAliceCommand.equals(payBobCommand));
    }
}
