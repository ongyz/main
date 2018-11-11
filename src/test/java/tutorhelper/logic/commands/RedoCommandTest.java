package tutorhelper.logic.commands;

import static tutorhelper.logic.commands.CommandTestUtil.assertCommandFailure;
import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.logic.commands.CommandTestUtil.deleteFirstStudent;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelper;

import org.junit.Before;
import org.junit.Test;

import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.UserPrefs;

public class RedoCommandTest {

    private final Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        // set up of both models' undo/redo history
        deleteFirstStudent(model);
        deleteFirstStudent(model);
        model.undoTutorHelper();
        model.undoTutorHelper();

        deleteFirstStudent(expectedModel);
        deleteFirstStudent(expectedModel);
        expectedModel.undoTutorHelper();
        expectedModel.undoTutorHelper();
    }

    @Test
    public void execute() {
        // multiple redoable states in model
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // single redoable state in model
        expectedModel.redoTutorHelper();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);

        // no redoable state in model
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }
}
