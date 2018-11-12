package tutorhelper.logic.commands;

import static java.util.Objects.requireNonNull;
import static tutorhelper.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static tutorhelper.logic.parser.CliSyntax.PREFIX_SYLLABUS;
import static tutorhelper.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static tutorhelper.model.util.SubjectsUtil.createStudentWithNewSubjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tutorhelper.commons.core.index.Index;
import tutorhelper.logic.CommandHistory;
import tutorhelper.logic.commands.exceptions.CommandException;
import tutorhelper.model.Model;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;
import tutorhelper.model.subject.Syllabus;

/**
 * Edits a syllabus topic of a subject of a student.
 */
public class EditSyllCommand extends Command {
    public static final String COMMAND_WORD = "editsyll";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits a syllabus topic of a subject of a student.\n"
            + "Parameters: "
            + "STUDENT_INDEX (must be a positive integer) "
            + "SUBJECT_INDEX (must be a positive integer) "
            + "SYLLABUS_INDEX (must be a positive integer) "
            + PREFIX_SYLLABUS + "SYLLABUS\n"
            + "Example: " + COMMAND_WORD + " 1 1 1 " + PREFIX_SYLLABUS + "Integration";

    public static final String MESSAGE_EDITSYLL_SUCCESS = "Edited syllabus to Student: %1$s";
    public static final String MESSAGE_SUBJECT_NOT_FOUND = "Subject index not found in Student";
    public static final String MESSAGE_DUPLICATE_SYLLABUS = "This syllabus already exists in the TutorHelper.";

    private final Index studentIndex;
    private final Index subjectIndex;
    private final Index syllabusIndex;
    private final Syllabus syllabusEdit;

    public EditSyllCommand(Index studentIndex, Index subjectIndex, Index syllabusIndex, Syllabus syllabusEdit) {
        this.studentIndex = studentIndex;
        this.subjectIndex = subjectIndex;
        this.syllabusIndex = syllabusIndex;
        this.syllabusEdit = syllabusEdit;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentTarget = lastShownList.get(studentIndex.getZeroBased());
        List<Subject> subjects = new ArrayList<>(studentTarget.getSubjects());

        if (subjects.size() < subjectIndex.getOneBased()) {
            throw new CommandException(String.format(MESSAGE_SUBJECT_NOT_FOUND, studentTarget));
        }



        Subject selectedSubject = subjects.get(subjectIndex.getZeroBased());

        if (selectedSubject.contains(syllabusEdit)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_SYLLABUS, studentTarget));
        }

        subjects.set(subjectIndex.getZeroBased(), selectedSubject.edit(syllabusEdit, syllabusIndex));
        Set<Subject> editedSubjectContent = new HashSet<>(subjects);

        Student studentSubjUpdated = createStudentWithNewSubjects(studentTarget, editedSubjectContent);

        model.updateStudentInternalField(studentTarget, studentSubjUpdated);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.commitTutorHelper();
        return new CommandResult(String.format(MESSAGE_EDITSYLL_SUCCESS, studentSubjUpdated));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EditSyllCommand // instanceof handles nulls
                && studentIndex.equals(((EditSyllCommand) other).studentIndex)
                && subjectIndex.equals(((EditSyllCommand) other).subjectIndex)
                && syllabusIndex.equals(((EditSyllCommand) other).syllabusIndex)
                && syllabusEdit.equals(((EditSyllCommand) other).syllabusEdit)); // state check
    }
}
