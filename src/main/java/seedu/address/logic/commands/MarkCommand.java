package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Finds all students whose name matches the keyword and add the to do element to the data.
 * Find is case-insensitive.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Toggles the selected syllabus of the "
            + "student identified by the index number used in the displayed student list. \n"
            + "Parameters: STUDENT_INDEX SUBJECT_INDEX SYLLABUS_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 1 2";

    public static final String MESSAGE_MARK_SUCCESS = "Changed selected syllabus from Student: %1$s";

    private final Index studentIndex;
    private final Index subjectIndex;
    private final Index syllabusIndex;

    public MarkCommand(Index studentIndex, Index subjectIndex, Index syllabusIndex) {
        this.studentIndex = studentIndex;
        this.subjectIndex = subjectIndex;
        this.syllabusIndex = syllabusIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentTarget = lastShownList.get(studentIndex.getZeroBased());

        Set<Subject> updatedSubjectContent = markSubjectContentFrom(studentTarget);
        Student studentSubjUpdated = SubjectsUtil.createStudentWithNewSubjects(studentTarget, updatedSubjectContent);

        model.updateStudent(studentTarget, studentSubjUpdated);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.commitTutorHelper();
        return new CommandResult(String.format(MESSAGE_MARK_SUCCESS, studentSubjUpdated));
    }

    /**
     * Returns a new {@code Set<Subject>} with the syllabus state changed from the student specified
     * @param studentTarget the student to change syllabus from
     * @return a new {@code Set<Subject>} with the specified syllabus state changed
     * @throws CommandException if the index to remove from is invalid
     */
    private Set<Subject> markSubjectContentFrom(Student studentTarget)
        throws CommandException {
        List<Subject> subjects = studentTarget.getSubjects().stream().collect(Collectors.toList());

        if (hasExceededNumberOfSubjects(subjects)) {
            throw new CommandException(Messages.MESSAGE_INVALID_SUBJECT_INDEX);
        }

        if (hasExceededNumberOfSyllabus(subjects.get(subjectIndex.getZeroBased()))) {
            throw new CommandException(Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
        }

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).toggleState(syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);
        return new HashSet<>(subjects);
    }

    private boolean hasExceededNumberOfSubjects(List<Subject> subjects) {
        return subjectIndex.getOneBased() > subjects.size();
    }

    private boolean hasExceededNumberOfSyllabus(Subject subject) {
        return syllabusIndex.getOneBased() > subject.getSubjectContent().size();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MarkCommand // instanceof handles nulls
                && studentIndex.equals(((MarkCommand) other).studentIndex))
                && subjectIndex.equals(((MarkCommand) other).subjectIndex)
                && syllabusIndex.equals(((MarkCommand) other).syllabusIndex); // state check
    }

    /**
     * Stores the details of rmtodo command format.
     */
    public static class MarkFormatChecker {
        public static final int STUDENT_INDEX_LOCATION = 0;
        public static final int SUBJECT_INDEX_LOCATION = 1;
        public static final int SYLLABUS_INDEX_LOCATION = 2;
        public static final int MARK_NUMBER_OF_ARGS = 3;
    }
}
