package tutorhelper.logic.commands;

import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import org.junit.Test;

import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.TutorHelper;
import tutorhelper.model.UserPrefs;

public class ClearCommandTest {

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_emptyTutorHelper_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();
        expectedModel.commitTutorHelper();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyTutorHelper_success() {
        Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
        expectedModel.resetData(new TutorHelper());
        expectedModel.commitTutorHelper();

        assertCommandSuccess(new ClearCommand(), model, commandHistory, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
