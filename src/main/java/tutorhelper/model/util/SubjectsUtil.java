package tutorhelper.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tutorhelper.commons.core.index.Index;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;
import tutorhelper.model.subject.SubjectType;

/**
 * Contains utility methods for managing list of subjects of a {@code Student}
 */
public class SubjectsUtil {

    /**
     * Returns a copy {@code Subject} specified by {@code subjectIndex} from {@code Student}
     */
    public static Subject copySubjectFrom(Student student, Index subjectIndex) {
        List<Subject> sourceSubjects = new ArrayList<>(student.getSubjects());
        Subject selectedSubject = sourceSubjects.get(subjectIndex.getZeroBased());

        Subject copiedSubject = new Subject(selectedSubject.getSubjectType(),
                new ArrayList<>(selectedSubject.getSubjectContent()),
                selectedSubject.getCompletionRate());

        return copiedSubject;
    }

    /**
     * Returns true if {@code Student} has the same subject type as {@code SubjectType}
     */
    public static boolean hasSubject(Student student, SubjectType type) {
        return student.getSubjects().stream().anyMatch(subject -> subject.hasTypeOf(type));
    }

    /**
     * Returns an {@code Optional<Subject>} of the same given type from student
     */
    public static Optional<Index> findSubjectIndex(Student student, SubjectType type) {
        Optional<Index> index = Optional.empty();
        List<Subject> subjectList = new ArrayList<>(student.getSubjects());
        for (int i = 0; i < subjectList.size(); i++) {
            Subject subject = subjectList.get(i);
            if (subject.hasTypeOf(type)) {
                index = Optional.of(Index.fromZeroBased(i));
            }
        }
        return index;
    }

    /**
     * Creates and returns a {@code Student} with the details of {@code Student source}
     * with the updated {@code Set<Subject> subjects}.
     * @param source the student to be updated
     * @param subjects the updated subjects
     * @return a new {@code Student} with updated subjects
     */
    public static Student createStudentWithNewSubjects(Student source, Set<Subject> subjects) {
        return new Student(source.getName(), source.getPhone(),
                source.getEmail(), source.getAddress(), subjects,
                source.getTuitionTiming(), source.getTags(), source.getPayments());
    }
}
