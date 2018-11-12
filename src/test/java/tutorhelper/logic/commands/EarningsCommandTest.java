package tutorhelper.logic.commands;

import static tutorhelper.logic.commands.CommandTestUtil.assertCommandSuccess;
import static tutorhelper.testutil.TypicalStudents.getTypicalTutorHelperWithPayments;

import org.junit.jupiter.api.Test;

import tutorhelper.logic.CommandHistory;
import tutorhelper.model.Model;
import tutorhelper.model.ModelManager;
import tutorhelper.model.UserPrefs;

public class EarningsCommandTest {

    private Model model = new ModelManager(getTypicalTutorHelperWithPayments(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validMonthYear_success() {

        //Assuming no entry for November 1998 was entered
        EarningsCommand earnings = new EarningsCommand(11, 1998);
        String expectedMessage = "Earnings: $0";
        assertCommandSuccess(earnings, model, commandHistory, expectedMessage, model);

        EarningsCommand earnings2 = new EarningsCommand(3, 2018);
        String expectedMessage2 = "Earnings: $700";
        assertCommandSuccess(earnings2, model, commandHistory, expectedMessage2, model);

    }
}
