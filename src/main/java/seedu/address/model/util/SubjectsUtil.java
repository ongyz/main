package seedu.address.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.SubjectType;

/**
 * Contains utility methods for managing list of subjects of a {@code Person}
 */
public class SubjectsUtil {

    /**
     * Returns a copy {@code Subject} specified by {@code subjectIndex} from {@code Person}
     */
    public static Subject copySubjectFrom(Person person, Index subjectIndex) {
        List<Subject> sourceSubjects = new ArrayList<>(person.getSubjects());
        Subject selectedSubject = sourceSubjects.get(subjectIndex.getZeroBased());

        Subject copiedSubject = new Subject(selectedSubject.getSubjectType(),
                new ArrayList<>(selectedSubject.getSubjectContent()),
                selectedSubject.getCompletionRate());

        return copiedSubject;
    }

    /**
     * Returns true if {@code Person} has the same subject type as {@code SubjectType}
     */
    public static boolean hasSubject(Person person, SubjectType type) {
        return person.getSubjects().stream().anyMatch(subject -> subject.getSubjectType().equals(type));
    }

    /**
     * Returns an {@code Optional<Subject>} of the same given type from person
     */
    public static Optional<Index> findSubjectIndex(Person person, SubjectType type) {
        Optional<Index> index = Optional.empty();
        List<Subject> subjectList = new ArrayList<>(person.getSubjects());
        for (int i = 0; i < subjectList.size(); i++) {
            Subject subject = subjectList.get(i);
            if (subject.getSubjectType().equals(type)) {
                index = Optional.of(Index.fromZeroBased(i));
            }
        }
        return index;
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code Person source}
     * with the updated {@code Set<Subject> subjects}.
     * @param source the person to be updated
     * @param subjects the updated subjects
     * @return a new {@code Person} with updated subjects
     */
    public static Person createPersonWithNewSubjects(Person source, Set<Subject> subjects) {
        return new Person(source.getName(), source.getPhone(),
                source.getEmail(), source.getAddress(), subjects,
                source.getTuitionTiming(), source.getTags());
    }
}
