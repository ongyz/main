package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Adds a subject for a student in the TutorHelper.
 */
public class AddSubCommand extends Command {

    public static final String COMMAND_WORD = "addsub";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a subject for a student in the TutorHelper. "
            + "Parameters: "
            + "STUDENT_INDEX "
            + PREFIX_SUBJECT + "SUBJECT\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_SUBJECT + "Physics ";

    public static final String MESSAGE_ADDSUB_SUCCESS = "Added subject to student: %1$s";
    public static final String MESSAGE_DUPLICATE_SUBJECT = "Subject is already taken by student: %1$s";

    private final Index studentIndex;
    private final Subject subject;

    public AddSubCommand(Index studentIndex, Subject subject) {
        this.studentIndex = studentIndex;
        this.subject = subject;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person studentTarget = lastShownList.get(studentIndex.getZeroBased());
        Set<Subject> newSubjects = addSubjectTo(studentTarget, subject);
        Person updatedStudent = SubjectsUtil.createPersonWithNewSubjects(studentTarget, newSubjects);

        model.updatePerson(studentTarget, updatedStudent);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitTutorHelper();
        return new CommandResult(String.format(MESSAGE_ADDSUB_SUCCESS, studentTarget));
    }

    /**
     * Add a subject to a student.
     * @param studentTarget The student to add the subject to.
     * @param subject The subject to add to the student.
     * @return A new {@code Set<Subject>} with the specified subject added.
     */
    private Set<Subject> addSubjectTo(Person studentTarget, Subject subject)
            throws CommandException {
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());

        if (subjects.contains(subject)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_SUBJECT, studentTarget));
        }

        List<Subject> updatedSubjects = subjects;
        updatedSubjects.add(subject);
        return new HashSet<>(subjects);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddSubCommand // instanceof handles nulls
                && studentIndex.equals(((AddSubCommand) other).studentIndex))
                && subject.equals(((AddSubCommand) other).subject); // state check
    }

    /**
     * Stores the format of the AddSub Command.
     */
    public static class AddSubFormatChecker {
        public static final int STUDENT_INDEX = 0;
        public static final int NUMBER_OF_ARGS = 1;
    }
}
