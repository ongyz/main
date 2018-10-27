package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalTutorHelperWithPayments;

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
