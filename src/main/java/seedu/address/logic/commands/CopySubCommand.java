package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Copies a subject and its syllabus topics from one student to another.
 */
public class CopySubCommand extends Command {

    public static final String COMMAND_WORD = "copysub";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Copies a subject and its syllabus topics from one student to another. "
            + "New syllabus topics will be appended in if the student is already taking the subject.\n"
            + "Parameters: "
            + "SOURCE_STUDENT_INDEX (must be a positive integer) "
            + "SUBJECT_INDEX (must be a positive integer) "
            + "TARGET_STUDENT_INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2 4";

    public static final String MESSAGE_COPYSUB_SUCCESS = "Copied syllabus to Student: %1$s";
    public static final String MESSAGE_COPYSUB_FAILED_SAME_STUDENT =
            "Copying subject to the same student is not allowed: %1$s";

    private final Index sourceStudentIndex;
    private final Index subjectIndex;
    private final Index targetStudentIndex;

    /**
     * Creates a CopySubCommand to copy the subject at the {@code subjectIndex} from the student at the
     * {@code sourceStudentIndex} to the student at the {@code targetStudentIndex}.
     */
    public CopySubCommand(Index sourceStudentIndex, Index subjectIndex, Index targetStudentIndex) {
        this.sourceStudentIndex = sourceStudentIndex;
        this.subjectIndex = subjectIndex;
        this.targetStudentIndex = targetStudentIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (sourceStudentIndex.getZeroBased() >= lastShownList.size()
            || targetStudentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentSource = lastShownList.get(sourceStudentIndex.getZeroBased());
        Student studentTarget = lastShownList.get(targetStudentIndex.getZeroBased());

        if (sourceStudentIndex.equals(targetStudentIndex)) {
            throw new CommandException(String.format(MESSAGE_COPYSUB_FAILED_SAME_STUDENT, studentSource));
        }

        if (subjectIndex.getZeroBased() >= studentSource.getSubjects().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SUBJECT_INDEX);
        }

        Subject selectedSubject = SubjectsUtil.copySubjectFrom(studentSource, subjectIndex);
        Set<Subject> updatedSubjects = updateSubjectsFor(studentTarget, selectedSubject);
        Student studentUpdated = SubjectsUtil.createStudentWithNewSubjects(studentTarget, updatedSubjects);

        model.updateStudent(studentTarget, studentUpdated);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.commitTutorHelper();
        return new CommandResult(String.format(MESSAGE_COPYSUB_SUCCESS, studentUpdated));
    }

    /**
     * Add subject into studentTarget. If studentTarget already has the same subject, append the content
     * instead.
     * @param studentTarget the student to be updated
     * @param newSubject the subject to be added
     * @return a new {@code Set<Subject>} with updated subjects
     */
    private Set<Subject> updateSubjectsFor(Student studentTarget, Subject newSubject) {
        List<Subject> targetSubjects = new ArrayList<>(studentTarget.getSubjects());

        if (SubjectsUtil.hasSubject(studentTarget, newSubject.getSubjectType())) {
            Index index = SubjectsUtil.findSubjectIndex(studentTarget, newSubject.getSubjectType()).get();
            Subject updatedSubject = targetSubjects.get(index.getZeroBased())
                                                   .append(newSubject.getSubjectContent());
            targetSubjects.set(index.getZeroBased(), updatedSubject);
        } else {
            targetSubjects.add(newSubject);
        }

        return new HashSet<>(targetSubjects);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CopySubCommand // instanceof handles nulls
                && sourceStudentIndex.equals(((CopySubCommand) other).sourceStudentIndex)
                && targetStudentIndex.equals(((CopySubCommand) other).targetStudentIndex)
                && subjectIndex.equals(((CopySubCommand) other).subjectIndex));
    }

    /**
     * Stores the details of CopySyll command format.
     */
    public static class CopySubFormatChecker {
        public static final int SOURCE_STUDENT_INDEX = 0;
        public static final int SUBJECT_INDEX = 1;
        public static final int TARGET_STUDENT_INDEX = 2;
        public static final int NUMBER_OF_ARGS = 3;
    }
}
