package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalStudents.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PayCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.student.Payment;
import seedu.address.model.student.Student;
import seedu.address.testutil.StudentBuilder;


public class PayCommandSystemTest extends TutorHelperSystemTest {

    @Test
    public void pay() {
        Model model = getModel();


        /* ------------------------ Perform pay operations on the shown unfiltered list ----------------------------- */

        /* Case: Update payment for first student in a non-empty TutorHelper,
         * command with leading spaces and trailing spaces
         * -> pay
         */

        String command = "  paid 1 400 8 2018 ";

        Student toPay = model.getFilteredStudentList().get(0);

        Student paidStudent = new StudentBuilder().withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .withTuitionTiming("Tuesday 8:00pm")
                .withSubjects("Mathematics")
                .withSyllabus(Index.fromOneBased(1), "Integration")
                .withTags("friends")
                .withPayments("1 400 8 2018")
                .build();

        assertCommandSuccess(command, toPay, paidStudent);

        /* Case: undo paying Alice(First student) in the list -> Alice restored */

        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo paying Alice(First student) in the list -> Alice has paid again */

        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateStudent(
                getModel().getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased()), paidStudent);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: Update payment for last existing student in a non-empty TutorHelper */

        command = "paid 7 400 8 2018 ";

        toPay = model.getFilteredStudentList().get(6);

        paidStudent = new StudentBuilder().withName("George Best")
                .withPhone("94824420")
                .withEmail("anna@example.com")
                .withAddress("4th street")
                .withTuitionTiming("Friday 5:00pm")
                .withSubjects("Economics")
                .withSyllabus(Index.fromOneBased(1), "Macroeconomics")
                .withPayments("4 400 8 2018")
                .build();
        assertCommandSuccess(command, toPay, paidStudent);

        /* Case: Edit payment for first student in a non-empty TutorHelper -> edit pay */

        command = "  paid 1 500 8 2018 ";

        Index index = INDEX_FIRST_STUDENT;;

        Student editedStudent = new StudentBuilder().withName("Alice Pauline")
                .withPhone("94351253")
                .withEmail("alice@example.com")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .withTuitionTiming("Tuesday 8:00pm")
                .withSubjects("Mathematics")
                .withSyllabus(Index.fromOneBased(1), "Integration")
                .withTags("friends")
                .withPayments("1 500 8 2018")
                .build();

        assertEditPayCommandSuccess(command, index, editedStudent);

        /* ----------------- Performing payment operation while a filtered list is being shown ------------------  */

        /* Case: filtered student list, student index within bounds of TutorHelper and student list -> success */

        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        command = "paid 2 400 8 2018 ";
        toPay = model.getFilteredStudentList().get(3);

        paidStudent = new StudentBuilder().withName("Daniel Meier")
                .withPhone("87652533")
                .withEmail("cornelia@example.com")
                .withAddress("10th street")
                .withTuitionTiming("Saturday 3:00pm")
                .withSubjects("Mathematics", "Physics")
                .withSyllabus(Index.fromOneBased(1), "Calculus II")
                .withSyllabus(Index.fromOneBased(1), "Statistics I")
                .withPayments("4 400 8 2018")
                .withTags("friends")
                .build();
        assertCommandSuccess(command, toPay, paidStudent);

        /* Case: filtered student list, student index within bounds of TutorHelper and student list -> success */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        command = "  paid 2 100 8 2018 ";

        index = INDEX_SECOND_STUDENT;;

        editedStudent = new StudentBuilder().withName("Daniel Meier")
                .withPhone("87652533")
                .withEmail("cornelia@example.com")
                .withAddress("10th street")
                .withTuitionTiming("Saturday 3:00pm")
                .withSubjects("Mathematics", "Physics")
                .withSyllabus(Index.fromOneBased(1), "Calculus II")
                .withSyllabus(Index.fromOneBased(1), "Statistics I")
                .withPayments("4 500 8 2018")
                .withTags("friends")
                .build();

        assertEditPayCommandSuccess(command, index, editedStudent);


        /* Case: filtered student list, student index within bounds of TutorHelper but out of bounds of student list
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getTutorHelper().getStudentList().size();
        command = PayCommand.COMMAND_WORD + " " + invalidIndex
                + " 200 8 2018";
        assertCommandFailure(command, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* Case: filtered student list, student index within bounds but syllabus index is out of bounds
         * -> rejected
         */
        showStudentsWithName(KEYWORD_MATCHING_MEIER);
        invalidIndex = getModel().getTutorHelper().getStudentList().size();
        command = PayCommand.COMMAND_WORD + " " + invalidIndex
                + " 200 8 2018";
        assertCommandFailure(command, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        /* ------------------------------- Performing invalid payment operation ---------------------------------- */

        /* Case: invalid index (0) -> rejected */
        command = PayCommand.COMMAND_WORD + " 0 200 8 2018";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_INDEX,
                PayCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        command = PayCommand.COMMAND_WORD + " -1 200 8 2018";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_INDEX,
                PayCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getTutorHelper().getStudentList().size() + 1);
        command = PayCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased() + " 200 8 2018";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX,
                PayCommand.MESSAGE_USAGE));

        /* Case: invalid number of arguments (alphabets) -> rejected */
        assertCommandFailure(
                PayCommand.COMMAND_WORD + " a b c", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        PayCommand.MESSAGE_USAGE));

        /* Case: invalid number of arguments  -> rejected */
        assertCommandFailure(
                PayCommand.COMMAND_WORD + " 200 1 2018", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        PayCommand.MESSAGE_USAGE));

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(
                PayCommand.COMMAND_WORD + " 1 a b c", String.format(Payment.MESSAGE_PAYMENT_AMOUNT_CONSTRAINTS,
                        PayCommand.MESSAGE_USAGE));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("PAid 1 200 8 2018", MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Student)}. Executes {@code command}
     * instead.
     */
    private void assertCommandSuccess(String command, Student original, Student toPay) {
        Model expectedModel = getModel();
        expectedModel.updateStudent(original, toPay);
        String expectedResultMessage = String.format(PayCommand.MESSAGE_PAYMENT_SUCCESS, toPay);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Student)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code StudentListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see PayCommandSystemTest
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code StudentListPanel} remain unchang
     * ed.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see TutorHelperSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the student at index {@code toEdit} being
     * updated to values specified {@code editedStudent}.<br>
     * @param toEdit the index of the current model's filtered list.
     */
    private void assertEditPayCommandSuccess(String command, Index toEdit, Student editedStudent) {
        Model expectedModel = getModel();
        Student original = expectedModel.getFilteredStudentList().get(toEdit.getZeroBased());
        expectedModel.updateStudent(original, editedStudent);
        String expectedResultMessage = String.format(PayCommand.MESSAGE_EDITPAYMENT_SUCCESS, editedStudent);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }


}
