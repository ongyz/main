package tutorhelper.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import tutorhelper.commons.core.ComponentManager;
import tutorhelper.commons.core.LogsCenter;
import tutorhelper.logic.commands.Command;
import tutorhelper.logic.commands.CommandResult;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.logic.parser.TutorHelperParser;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandHistory history;
    private final TutorHelperParser tutorHelperParser;

    public LogicManager(Model model) {
        this.model = model;
        history = new CommandHistory();
        tutorHelperParser = new TutorHelperParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        try {
            Command command = tutorHelperParser.parseCommand(commandText);
            return command.execute(model, history);
        } finally {
            history.add(commandText);
        }
    }

    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return model.getFilteredStudentList();
    }

    @Override
    public ListElementPointer getHistorySnapshot() {
        return new ListElementPointer(history.getHistory());
    }
}
