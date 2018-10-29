package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
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
 * Deletes a subject for a student in the TutorHelper.
 */
public class DeleteSubCommand extends Command {

    public static final String COMMAND_WORD = "deletesub";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a subject for a student in the TutorHelper. "
            + "Parameters: "
            + "STUDENT_INDEX "
            + "SUBJECT_INDEX\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + "2";

    public static final String MESSAGE_DELETESUB_SUCCESS = "Deleted subject from student: %1$s";
    public static final String MESSAGE_DELETE_ONLY_SUBJECT = "At least one subject must be studied by student: %1$s";
    public static final String MESSAGE_SUBJECT_INDEX_OUT_OF_BOUNDS = "Subject does not exist.";

    private final Index studentIndex;
    private final Index subjectIndex;

    public DeleteSubCommand(Index studentIndex, Index subjectIndex) {
        this.studentIndex = studentIndex;
        this.subjectIndex = subjectIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person studentTarget = lastShownList.get(studentIndex.getZeroBased());
        Set<Subject> newSubjects = removeSubjectFrom(studentTarget);
        Person updatedStudent = SubjectsUtil.createPersonWithNewSubjects(studentTarget, newSubjects);

        model.updatePerson(studentTarget, updatedStudent);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitTutorHelper();
        return new CommandResult(String.format(MESSAGE_DELETESUB_SUCCESS, studentTarget));
    }

    /**
     * Deletes a subject from a student.
     * @param studentTarget The student to add the subject to.
     * @return A new {@code Set<Subject>} without the subject.
     */
    private Set<Subject> removeSubjectFrom(Person studentTarget)
            throws CommandException {
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());

        if (isSubjectIndexOutOfBounds(subjects)) {
            throw new CommandException(MESSAGE_SUBJECT_INDEX_OUT_OF_BOUNDS);
        }

        if (hasOneSubject(subjects)) {
            throw new CommandException(String.format(MESSAGE_DELETE_ONLY_SUBJECT, studentTarget));
        }

        List<Subject> updatedSubjects = subjects;
        updatedSubjects.remove(subjectIndex.getZeroBased());
        return new HashSet<>(subjects);
    }

    private boolean isSubjectIndexOutOfBounds(List<Subject> subjects) {
        return subjectIndex.getOneBased() > subjects.size();
    }

    private boolean hasOneSubject(List<Subject> subjects) {
        return subjects.size() == 1;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteSubCommand // instanceof handles nulls
                && studentIndex.equals(((DeleteSubCommand) other).studentIndex))
                && subjectIndex.equals(((DeleteSubCommand) other).subjectIndex); // state check
    }

    /**
     * Stores the format of the AddSub Command.
     */
    public static class DeleteSubFormatChecker {
        public static final int STUDENT_INDEX = 0;
        public static final int SUBJECT_INDEX = 1;
        public static final int NUMBER_OF_ARGS = 2;
    }
}
