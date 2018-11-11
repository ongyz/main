package tutorhelper.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.ArrayList;
import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Payment;
import tutorhelper.model.student.Student;


/**
 * Adds a payment record of a student.
 */
public class PayCommand extends Command {
    public static final String COMMAND_WORD = "paid";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a payment record of a student.\n"
            + "Parameters: "
            + "INDEX (must be a positive integer) "
            + "AMOUNT (must be a positive integer between 0 to 10 000) "
            + "MONTH (must be an integer from 1 to 12, inclusive) "
            + "YEAR (must be a 4-digit integer)\n"
            + "Example: " + COMMAND_WORD + " 1 200 08 2018 ";

    public static final String MESSAGE_PAYMENT_SUCCESS = "Payment for this student is added: %1$s";
    public static final String MESSAGE_EDIT_PAYMENT_SUCCESS = "Payment for this student has been edited: %1$s";

    private static final int MIN_VALUE = -1;
    private static final int MAX_PAYMENTS_DISPLAYED = 5;

    private Index targetIndex;
    private Payment newPayment;

    public PayCommand(Payment payment) {
        this.targetIndex = payment.getIndex();
        this.newPayment = payment;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        boolean editEntry;
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= lastShownList.size() || targetIndex.getZeroBased() <= MIN_VALUE) {
            throw new CommandException(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentTarget = lastShownList.get(targetIndex.getZeroBased());

        List<Payment> pay = studentTarget.getPayments();

        editEntry = findPaymentToUpdate(pay, newPayment);

        if (!editEntry) {
            if (pay.size() > MAX_PAYMENTS_DISPLAYED) {
                pay.remove(0);
            }
            pay = updatePayment(studentTarget.getPayments(), newPayment);
        } else {
            pay = editPaymentField(pay, newPayment);
        }

        Student studentToPay = new Student(studentTarget.getName(), studentTarget.getPhone(),
                studentTarget.getEmail(), studentTarget.getAddress(), studentTarget.getSubjects(),
                studentTarget.getTuitionTiming(), studentTarget.getTags(), pay);

        if (editEntry) {
            model.updateStudentInternalField(studentTarget, studentToPay);
            model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
            model.commitTutorHelper();
            return new CommandResult(String.format(MESSAGE_EDIT_PAYMENT_SUCCESS, studentToPay));
        } else {
            model.updateStudent(studentTarget, studentToPay);
            model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
            model.commitTutorHelper();
            return new CommandResult(String.format(MESSAGE_PAYMENT_SUCCESS, studentToPay));
        }
    }

    /**
     * Update payment for this student and returns a new instance of this student.
     * @return the same student but updated with payment.
     */
    public List<Payment> updatePayment(List<Payment> oldPayments, Payment newPayment) {
        List<Payment> updatedPayment = new ArrayList<>(oldPayments);
        updatedPayment.add(newPayment);
        return updatedPayment;
    }

    /**
     * Checks if student to edit has the payment field.
     * @param payments the list of payment to check for entry with same details.
     * @param toFind the payment entry to edit.
     * @return true if payment field has already existed; false otherwise.
     */
    private boolean findPaymentToUpdate(List<Payment> payments, Payment toFind) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getMonth() == toFind.getMonth() && payments.get(i).getYear() == toFind.getYear()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates payment field in list of payments.
     * @param payments the list of payment to check for entry with same details.
     * @param toEdit the payment entry to edit.
     * @return the edited list of payments.
     */
    private List<Payment> editPaymentField(List<Payment> payments, Payment toEdit) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getMonth() == toEdit.getMonth() && payments.get(i).getYear() == toEdit.getYear()) {
                payments.set(i, toEdit);
                break;
            }
        }
        return payments;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PayCommand)) {
            return false;
        }

        // state check
        PayCommand e = (PayCommand) other;
        return targetIndex.equals(e.targetIndex)
                && newPayment.equals(e.newPayment);
    }
}
