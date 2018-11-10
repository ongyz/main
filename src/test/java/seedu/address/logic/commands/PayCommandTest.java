package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TutorHelper;
import seedu.address.model.UserPrefs;
import seedu.address.model.student.Payment;
import seedu.address.model.student.Student;
import seedu.address.testutil.StudentBuilder;

public class PayCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastStudentIndex = Index.fromOneBased(model.getFilteredStudentList().size());
        assertExecutionSuccess(INDEX_FIRST_STUDENT);
        assertExecutionSuccess(INDEX_SECOND_STUDENT);
        assertExecutionSuccess(INDEX_THIRD_STUDENT);
        assertExecutionSuccess(lastStudentIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        Index integerOverflowIndex = Index.fromOneBased(Integer.MAX_VALUE);
        assertExecutionFailure(integerOverflowIndex, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_updatePaymentMethod_success() {
        Payment originalPayment = new Payment(INDEX_FIRST_STUDENT, 100, 11, 1998);
        PayCommand originalPayCommand = new PayCommand(originalPayment);

        List<Payment> oldPaymentList = new ArrayList<>();
        oldPaymentList.add(originalPayment);

        Payment newPayment = new Payment(INDEX_FIRST_STUDENT, 100, 11, 2018);
        List<Payment> actualReturnedPayment = originalPayCommand.updatePayment(oldPaymentList, newPayment);

        List<Payment> expectedReturnedPaymentList = new ArrayList<>();
        expectedReturnedPaymentList.add(originalPayment);
        expectedReturnedPaymentList.add(newPayment);

        assertEquals(actualReturnedPayment, expectedReturnedPaymentList);

    }

    @Test
    public void execute_editPaymentMethod_success() {

        Payment existingPayment = new Payment(INDEX_FIRST_STUDENT, 100, 11, 1998);
        Payment editedPayment = new Payment(INDEX_FIRST_STUDENT, 300, 11, 1998);

        Student existingStudent = new StudentBuilder(ALICE).withPayments(existingPayment).build();
        Student editedStudent = new StudentBuilder(ALICE).withPayments(editedPayment).build();
        PayCommand editPayCommand = new PayCommand(editedPayment);

        String expectedMessage = String.format(PayCommand.MESSAGE_EDITPAYMENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new TutorHelper(model.getTutorHelper()), new UserPrefs());
      
        expectedModel.updateStudentInternalField(model.getFilteredStudentList().get(0), editedStudent);
        model.updateStudentInternalField(model.getFilteredStudentList().get(0), existingStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(editPayCommand, model, commandHistory, expectedMessage, expectedModel);

    }

    /**
     * Executes a {@code PayCommand} with the given {@code index}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {

        Payment payment = new Payment(index, 200, 9, 2020);
        PayCommand payCommand = new PayCommand(payment);

        Student studentOriginal = model.getFilteredStudentList().get(index.getZeroBased());
        Student studentOriginalClone = new StudentBuilder(studentOriginal).build();
        Student expectedStudent = new StudentBuilder(studentOriginalClone).withPayments(payment).build();

        String expectedMessage = String.format(PayCommand.MESSAGE_PAYMENT_SUCCESS, expectedStudent);
        expectedModel.updateStudent(studentOriginal, expectedStudent);
        expectedModel.commitTutorHelper();

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

        Payment alicePayment = new Payment(INDEX_FIRST_STUDENT, 200, 10, 2018);
        Payment bobPayment = new Payment(INDEX_SECOND_STUDENT, 200, 10, 2018);

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
