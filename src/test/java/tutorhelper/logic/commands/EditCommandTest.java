package tutorhelper.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.logic.commands.CommandTestUtil.DESC_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.DESC_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TAG_WEAK;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.logic.commands.CommandTestUtil.showStudentAtIndex;
import static tutorhelper.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static tutorhelper.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import org.junit.Assert;
import org.junit.Test;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.EditCommand.EditStudentDescriptor;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.TutorHelper;
import tutorhelper.model.UserPrefs;
import tutorhelper.model.student.Student;
import tutorhelper.testutil.EditStudentDescriptorBuilder;
import tutorhelper.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code EditCommand}.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Student editedStudent = new StudentBuilder().build();
        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(editedStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new TutorHelper(model.getTutorHelper()), new UserPrefs());
        expectedModel.updateStudent(model.getFilteredStudentList().get(0), editedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastStudent = Index.fromOneBased(model.getFilteredStudentList().size());
        Student lastStudent = model.getFilteredStudentList().get(indexLastStudent.getZeroBased());

        StudentBuilder studentInList = new StudentBuilder(lastStudent);
        Student editedStudent = studentInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_WEAK).build();

        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_WEAK).build();
        EditCommand editCommand = new EditCommand(indexLastStudent, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new TutorHelper(model.getTutorHelper()), new UserPrefs());
        expectedModel.updateStudent(lastStudent, editedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, new EditCommand.EditStudentDescriptor());
        Student editedStudent = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new TutorHelper(model.getTutorHelper()), new UserPrefs());
        expectedModel.commitTutorHelper();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        Student studentInFilteredList = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Student editedStudent = new StudentBuilder(studentInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT,
                new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_STUDENT_SUCCESS, editedStudent);

        Model expectedModel = new ModelManager(new TutorHelper(model.getTutorHelper()), new UserPrefs());
        expectedModel.updateStudent(model.getFilteredStudentList().get(0), editedStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateStudentUnfilteredList_failure() {
        Student firstStudent = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(firstStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_STUDENT, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void execute_duplicateStudentFilteredList_failure() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);

        // edit student in filtered list into a duplicate in TutorHelper
        Student studentInList = model.getTutorHelper().getStudentList().get(INDEX_SECOND_STUDENT.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT,
                new EditStudentDescriptorBuilder(studentInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_STUDENT);
    }

    @Test
    public void execute_invalidStudentIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of TutorHelper
     */
    @Test
    public void execute_invalidStudentIndexFilteredList_failure() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);
        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of TutorHelper list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTutorHelper().getStudentList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Student editedStudent = new StudentBuilder().build();
        Student studentToEdit = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(editedStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, descriptor);
        Model expectedModel = new ModelManager(new TutorHelper(model.getTutorHelper()), new UserPrefs());
        expectedModel.updateStudent(studentToEdit, editedStudent);
        expectedModel.commitTutorHelper();

        // edit -> first student edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered student list to show all students
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first student edited again
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> TutorHelper state not added into model
        assertCommandFailure(editCommand, model, commandHistory, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);

        // single TutorHelper state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Student} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited student in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the student object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameStudentEdited() throws Exception {
        Student editedStudent = new StudentBuilder().withPayments("2 200 11 2018", "2 300 12 2018").build();
        EditCommand.EditStudentDescriptor descriptor = new EditStudentDescriptorBuilder(editedStudent).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_STUDENT, descriptor);
        Model expectedModel = new ModelManager(new TutorHelper(model.getTutorHelper()), new UserPrefs());

        showStudentAtIndex(model, INDEX_SECOND_STUDENT);
        Student studentToEdit = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        expectedModel.updateStudent(studentToEdit, editedStudent);
        expectedModel.commitTutorHelper();

        // edit -> edits second student in unfiltered student list / first student in filtered student list
        editCommand.execute(model, commandHistory);

        // undo -> reverts TutorHelper back to previous state and filtered student list to show all students
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        Assert.assertNotEquals(model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased()), studentToEdit);
        // redo -> edits same second student in unfiltered student list
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_STUDENT, DESC_AMY);

        // same values -> returns true
        EditCommand.EditStudentDescriptor copyDescriptor = new EditCommand.EditStudentDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_STUDENT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_STUDENT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_STUDENT, DESC_BOB)));
    }

}
