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

public class UndoCommandTest {

    private final Model model = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalTutorHelper(), new UserPrefs());
    private final CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        // set up of models' undo/redo history
        deleteFirstStudent(model);
        deleteFirstStudent(model);

        deleteFirstStudent(expectedModel);
        deleteFirstStudent(expectedModel);
    }

    @Test
    public void execute() {
        // multiple undoable states in model
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // single undoable state in model
        expectedModel.undoTutorHelper();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // no undoable states in model
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
    }
}
