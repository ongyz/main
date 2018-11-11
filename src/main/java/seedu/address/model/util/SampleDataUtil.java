package seedu.address.model.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.ReadOnlyTutorHelper;
import seedu.address.model.TutorHelper;
import seedu.address.model.student.Address;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Phone;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.Syllabus;
import seedu.address.model.tag.Tag;
import seedu.address.model.tuitiontiming.TuitionTiming;

/**
 * Contains utility methods for populating {@code TutorHelper} with sample data.
 */
public class SampleDataUtil {
    public static Student[] getSampleStudents() {
        return new Student[] {
            new Student(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"), getSubjectSet("Mathematics"),
                new TuitionTiming("Monday 6:00pm"), getTagSet("weak")),
            new Student(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), getSubjectSet("Economics"),
                new TuitionTiming("Friday 5:30pm"), getTagSet("smart", "exam")),
            new Student(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), getSubjectSet("Chemistry", "Mathematics"),
                new TuitionTiming("Tuesday 6:00pm"), getTagSet("neighbours")),
            new Student(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), getSubjectSet("Mathematics"),
                new TuitionTiming("Wednesday 7:00pm"), getTagSet("family")),
            new Student(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), getSubjectSet("Physics", "Biology"),
                new TuitionTiming("Thursday 3:00pm"), getTagSet("exam")),
            new Student(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), getSubjectSet("Chemistry"),
                new TuitionTiming("Saturday 10:00am"), getTagSet("smart"))
        };
    }

    public static ReadOnlyTutorHelper getSampleTutorHelper() {
        TutorHelper sampleAb = new TutorHelper();
        for (Student sampleStudent : getSampleStudents()) {
            sampleAb.addStudent(sampleStudent);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a subject set containing the list of strings given.
     */
    public static Set<Subject> getSubjectSet(String... strings) {
        return Arrays.stream(strings)
                .map(Subject::makeSubject)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of syllabus containing the list of strings given.
     */
    public static List<Syllabus> getSyllabusList(String... strings) {
        return Arrays.stream(strings)
                .map(Syllabus::makeSyllabus)
                .collect(Collectors.toList());
    }

}
