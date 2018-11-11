package tutorhelper.logic.commands;

import static tutorhelper.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import org.junit.Before;
import org.junit.Test;

import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.UserPrefs;
import tutorhelper.model.student.Student;
import tutorhelper.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    }

    @Test
    public void execute_newStudent_success() {
        Student validStudent = new StudentBuilder().build();

        Model expectedModel = new ModelManager(model.getTutorHelper(), new UserPrefs());
        expectedModel.addStudent(validStudent);
        expectedModel.commitTutorHelper();

        assertCommandSuccess(new AddCommand(validStudent), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validStudent), expectedModel);
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        Student studentInList = model.getTutorHelper().getStudentList().get(0);
        assertCommandFailure(new AddCommand(studentInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_STUDENT);
    }

}
