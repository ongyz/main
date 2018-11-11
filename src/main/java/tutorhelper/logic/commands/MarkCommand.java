package tutorhelper.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SUBJECT_INDEX;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_SYLLABUS_INDEX;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static tutorhelper.model.util.SubjectsUtil.createStudentWithNewSubjects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;

/**
 * Toggles the marked state of a syllabus topic of a subject of a student.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Toggles the marked state of a syllabus topic of a subject of a student. \n"
            + "Parameters: "
            + "STUDENT_INDEX (must be a positive integer) "
            + "SUBJECT_INDEX (must be a positive integer) "
            + "SYLLABUS_INDEX (must be a positive integer)\n"
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
            throw new CommandException(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentTarget = lastShownList.get(studentIndex.getZeroBased());

        Set<Subject> updatedSubjectContent = markSubjectContentFrom(studentTarget);
        Student studentSubjUpdated = createStudentWithNewSubjects(studentTarget, updatedSubjectContent);

        model.updateStudentInternalField(studentTarget, studentSubjUpdated);
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
            throw new CommandException(MESSAGE_INVALID_SUBJECT_INDEX);
        }

        if (hasExceededNumberOfSyllabus(subjects.get(subjectIndex.getZeroBased()))) {
            throw new CommandException(MESSAGE_INVALID_SYLLABUS_INDEX);
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
     * Stores the details of the mark command format.
     */
    public static class MarkFormatChecker {
        public static final int STUDENT_INDEX_LOCATION = 0;
        public static final int SUBJECT_INDEX_LOCATION = 1;
        public static final int SYLLABUS_INDEX_LOCATION = 2;
        public static final int MARK_NUMBER_OF_ARGS = 3;
    }
}
