package tutorhelper.logic;

import javafx.collections.ObservableList;
import tutorhelper.logic.commands.CommandResult;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.logic.parser.exceptions.ParseException;
import tutorhelper.model.student.Student;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered list of students */
    ObservableList<Student> getFilteredStudentList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();
}
