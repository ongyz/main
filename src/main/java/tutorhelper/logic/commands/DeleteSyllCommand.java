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
 * Removes a syllabus topic from a specified subject under the selected student.
 */
public class DeleteSyllCommand extends Command {

    public static final String COMMAND_WORD = "deletesyll";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a syllabus topic from a subject of a student.\n"
            + "Parameters: "
            + "STUDENT_INDEX (must be a positive integer) "
            + "SUBJECT_INDEX (must be a positive integer) "
            + "SYLLABUS_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 1 2";

    public static final String MESSAGE_DELETESYLL_SUCCESS = "Removed selected syllabus from Student: %1$s";

    private final Index studentIndex;
    private final Index subjectIndex;
    private final Index syllabusIndex;

    public DeleteSyllCommand(Index studentIndex, Index subjectIndex, Index syllabusIndex) {
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

        Set<Subject> removedSubjectContent = removeSubjectContentFrom(studentTarget);
        Student studentSubjUpdated = createStudentWithNewSubjects(studentTarget, removedSubjectContent);

        model.updateStudentInternalField(studentTarget, studentSubjUpdated);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.commitTutorHelper();
        return new CommandResult(String.format(MESSAGE_DELETESYLL_SUCCESS, studentSubjUpdated));
    }

    /**
     * Removes syllabus topic from the specified student.
     * @param studentTarget The student to remove from.
     * @return a new {@code Set<Subject>} with the specified syllabus removed
     * @throws CommandException if the index to remove from is invalid
     */
    private Set<Subject> removeSubjectContentFrom(Student studentTarget) throws CommandException {
        List<Subject> subjects = studentTarget.getSubjects().stream().collect(Collectors.toList());

        if (hasExceededNumberOfSubjects(subjects)) {
            throw new CommandException(MESSAGE_INVALID_SUBJECT_INDEX);
        }

        if (hasExceededNumberOfSyllabus(subjects.get(subjectIndex.getZeroBased()))) {
            throw new CommandException(MESSAGE_INVALID_SYLLABUS_INDEX);
        }

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).remove(syllabusIndex);
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
                || (other instanceof DeleteSyllCommand // instanceof handles nulls
                && studentIndex.equals(((DeleteSyllCommand) other).studentIndex))
                && subjectIndex.equals(((DeleteSyllCommand) other).subjectIndex)
                && syllabusIndex.equals(((DeleteSyllCommand) other).syllabusIndex); // state check
    }
}
