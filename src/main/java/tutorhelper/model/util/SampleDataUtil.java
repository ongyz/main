package tutorhelper.model.util;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import tutorhelper.model.ReadOnlyTutorHelper;
import tutorhelper.model.TutorHelper;
import tutorhelper.model.student.Address;
import tutorhelper.model.student.Email;
import tutorhelper.model.student.Name;
import tutorhelper.model.student.Phone;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;
import tutorhelper.model.subject.Syllabus;
import tutorhelper.model.tag.Tag;
import tutorhelper.model.tuitiontiming.TuitionTiming;

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
                new TuitionTiming("Tuesday 6:00pm"), getTagSet("owesMoney")),
            new Student(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), getSubjectSet("Mathematics"),
                new TuitionTiming("Wednesday 7:00pm"), getTagSet("weak")),
            new Student(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"), getSubjectSet("Physics", "Biology"),
                new TuitionTiming("Thursday 3:00pm"), getTagSet("exam")),
            new Student(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"), getSubjectSet("Chemistry"),
                new TuitionTiming("Saturday 10:00am"), getTagSet("smart")),
            new Student(new Name("Amari Barrett"), new Phone("99355129"), new Email("amarib@example.com"),
                new Address("Blk 12 Toa Payoh Ind Pk Lor 8, #01-1195"), getSubjectSet("German"),
                new TuitionTiming("Friday 1:00pm"), getTagSet("owesMoney")),
            new Student(new Name("Walid Garrison"), new Phone("64230610"), new Email("garrweild@example.com"),
                new Address("6 Battery Road #22-00"), getSubjectSet("English", "Mathematics"),
                new TuitionTiming("Thursday 9:00pm"), getTagSet("weak")),
            new Student(new Name("Ozan Mccartney"), new Phone("93388703"), new Email("ozanmc@example.com"),
                new Address("9 Penang Road #10-05 Park Mall, 238459"),
                getSubjectSet("Physics", "Literature", "Chemistry"),
                new TuitionTiming("Monday 9:00pm"), getTagSet("weak")),
            new Student(new Name("Amelia Rose"), new Phone("67384043"), new Email("melrose@example.com"),
                new Address("290 Orchard Road #01-30/31"), getSubjectSet("Malay"),
                new TuitionTiming("Sunday 3:00pm"), getTagSet("smart")),
            new Student(new Name("Cornish"), new Phone("93336178"), new Email("corn@example.com"),
                new Address("Suntec City Mall 3 Temasek Boulevard, #01-199"),
                getSubjectSet("Art", "Literature"),
                new TuitionTiming("Tuesday 4:00pm"), getTagSet("smart", "exam")),
            new Student(new Name("Skylar Shelton"), new Phone("97376131"), new Email("sshelton@example.com"),
                new Address("20 Kramat Lane 04-07"), getSubjectSet("Japanese", "Chinese"),
                new TuitionTiming("Friday 7:00pm"), getTagSet("smart", "exam")),
            new Student(new Name("Grady Coleman"), new Phone("93170707"), new Email("gradman@example.com"),
                new Address("8 Shenton Way. #10-01"), getSubjectSet("Computing", "Physics", "Mathematics"),
                new TuitionTiming("Sunday 5:00pm"), getTagSet("sleepy")),
            new Student(new Name("Aaron Hampton"), new Phone("94547582"), new Email("aahamp@example.com"),
                new Address("105 Airport Cargo Road #452A"), getSubjectSet("Chinese"),
                new TuitionTiming("Tuesday 5:00pm"), getTagSet("weak")),
            new Student(new Name("Lilly Delacruz"), new Phone("92475990"), new Email("lilcruz@example.com"),
                new Address("Blk 514 Chai Chee Lane, #03-02"), getSubjectSet("Art", "Japanese"),
                new TuitionTiming("Wednesday 11:00am"), getTagSet("exam")),
            new Student(new Name("Chanel Allan"), new Phone("97529556"), new Email("chanal@example.com"),
                new Address("10 Admiralty Street, #05-47"), getSubjectSet("Physics"),
                new TuitionTiming("Monday 12:00pm"), getTagSet("sleepy")),
            new Student(new Name("Asher Leonard"), new Phone("94836163"), new Email("ashleo@example.com"),
                new Address("52 Serangoon Nth Ave 4, #03-02"), getSubjectSet("Literature", "Geography"),
                new TuitionTiming("Saturday 2:00pm"), getTagSet("smart", "sleepy")),
            new Student(new Name("Yuvraj Carrilo"), new Phone("93348517"), new Email("yuvraj@example.com"),
                new Address("182 Clemenceau Avenue #04-00"), getSubjectSet("English", "Tamil"),
                new TuitionTiming("Tuesday 11:00am"), getTagSet("weak", "exam")),
            new Student(new Name("Fannie Sweene"), new Phone("97550821"), new Email("fanswee@example.com"),
                new Address("190 Yishun Avenue 7"), getSubjectSet("German"),
                new TuitionTiming("Wednesday 10:00am"), getTagSet("smart")),
            new Student(new Name("Hiba Randolph"), new Phone("64641274"), new Email("hibar@example.com"),
                new Address("18 Boon Lay Way Tradehub-21 10-117"), getSubjectSet("Economics"),
                new TuitionTiming("Wednesday 3:00pm"), getTagSet("exam")),
            new Student(new Name("Hayley Byrne"), new Phone("96686468"), new Email("hayleyb@example.com"),
                new Address("Toa Payoh Lor 8, #01-1477"), getSubjectSet("Music", "Literature"),
                new TuitionTiming("Thursday 8:00am"), getTagSet("smart", "exam")),
            new Student(new Name("Darlene Wilcox"), new Phone("92705453"), new Email("darwil@example.com"),
                new Address("22 Sin Ming Lane, #16-76"), getSubjectSet("Biology", "Chemistry"),
                new TuitionTiming("Wednesday 9:00am"), getTagSet("sleepy", "exam")),
            new Student(new Name("May Buck"), new Phone("64715333"), new Email("bukcm@example.com"),
                new Address("77 Commonwealth Drive, #01-523"), getSubjectSet("Computing", "German"),
                new TuitionTiming("Tuesday 1:00pm"), getTagSet("owesMoney")),
            new Student(new Name("Danish Boyer"), new Phone("93838080"), new Email("danishboy@example.com"),
                new Address("31 Pandan Road"), getSubjectSet("French", "Mathematics"),
                new TuitionTiming("Saturday 12:00pm"), getTagSet("owesMoney", "exam")),
            new Student(new Name("Annabelle Terry"), new Phone("67375488"), new Email("belleter@example.com"),
                new Address("501 Orchard Road, #07-02"), getSubjectSet("Biology"),
                new TuitionTiming("Thursday 9:00am"), getTagSet("smart")),
            new Student(new Name("Giuseppe Naylor"), new Phone("98422006"), new Email("giunay@example.com"),
                new Address("71 Lor 23 Geylang, #08-01"), getSubjectSet("Chinese", "Physics"),
                new TuitionTiming("Friday 10:00am"), getTagSet("weak"))
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
