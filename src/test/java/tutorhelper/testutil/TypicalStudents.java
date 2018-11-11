package tutorhelper.testutil;

import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_ALCYONE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_BILLY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_CABBAGE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_CATHY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_ADDRESS_DAISY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_ALCYONE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_BILLY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_CABBAGE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_CATHY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_EMAIL_DAISY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_ALCYONE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_BILLY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_CABBAGE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_CATHY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_NAME_DAISY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_ALCYONE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_BILLY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_CABBAGE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_CATHY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_PHONE_DAISY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SUBJECT_ALCYONE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SUBJECT_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SUBJECT_BILLY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SUBJECT_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SUBJECT_CABBAGE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SUBJECT_CATHY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_SUBJECT_DAISY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TAG_EXAM;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TAG_WEAK;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_ALCYONE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_AMY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_BILLY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_BOB;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_CABBAGE;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_CATHY;
import static tutorhelper.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_DAISY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tutorhelper.commons.core.index.Index;
import tutorhelper.model.TutorHelper;
import tutorhelper.model.student.Student;

/**
 * A utility class containing a list of {@code Student} objects to be used in tests.
 */
public class TypicalStudents {

    public static final Student
            ALICE = new StudentBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withSubjects("Mathematics")
            .withSyllabus(Index.fromOneBased(1), "Integration")
            .withTuitionTiming("Tuesday 8:00pm").withTags("exam").build();
    public static final Student BENSON = new StudentBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25").withEmail("johnd@example.com")
            .withPhone("98765432").withSubjects("Chemistry")
            .withSyllabus(Index.fromOneBased(1), "Kinetics", "Organic Chemistry")
            .withTuitionTiming("Monday 5:00pm")
            .withTags("owesMoney", "smart").withPayments("2 200 11 2018", "2 300 12 2018").build();
    public static final Student CARL = new StudentBuilder().withName("Carl Kurz")
            .withPhone("95352563").withEmail("heinz@example.com")
            .withAddress("Wall street").withSubjects("Mathematics", "Physics")
            .withSyllabus(Index.fromOneBased(1), "Calculus II", "Statistics I")
            .withTuitionTiming("Saturday 1:00pm").build();
    public static final Student DANIEL = new StudentBuilder().withName("Daniel Meier")
            .withPhone("87652533").withEmail("cornelia@example.com")
            .withAddress("10th street").withSubjects("Mathematics", "Physics")
            .withSyllabus(Index.fromOneBased(1), "Calculus II", "Statistics I")
            .withTuitionTiming("Saturday 3:00pm")
            .withTags("exam").build();
    public static final Student ELLE = new StudentBuilder().withName("Elle Meyer")
            .withPhone("94822240").withEmail("werner@example.com")
            .withAddress("Michegan ave").withSubjects("Economics")
            .withSyllabus(Index.fromOneBased(1), "Demand and Supply")
            .withTuitionTiming("Sunday 11:00am").build();
    public static final Student FIONA = new StudentBuilder().withName("Fiona Kunz")
            .withPhone("94824270").withEmail("lydia@example.com")
            .withAddress("little tokyo").withSubjects("Chemistry")
            .withSyllabus(Index.fromOneBased(1), "Organic Chemistry", "Acids and Bases")
            .withTuitionTiming("Thursday 3:30pm").build();
    public static final Student GEORGE = new StudentBuilder().withName("George Best")
            .withPhone("94824420").withEmail("anna@example.com")
            .withAddress("4th street").withSubjects("Economics")
            .withSyllabus(Index.fromOneBased(1), "Macroeconomics")
            .withTuitionTiming("Friday 5:00pm").build();

    // Manually added
    public static final Student HOON = new StudentBuilder().withName("Hoon Meier").withPhone("84824240")
            .withEmail("stefan@example.com").withAddress("little india")
            .withSubjects("Mathematics").withTuitionTiming("Monday 8:00pm").build();
    public static final Student IDA = new StudentBuilder().withName("Ida Mueller").withPhone("84821310")
            .withEmail("hans@example.com").withAddress("chicago ave")
            .withSubjects("Physics").withTuitionTiming("Wednesday 6:00pm").build();

    // Manually added - Student's details found in {@code CommandTestUtil}
    public static final Student AMY = new StudentBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withSubjects(VALID_SUBJECT_AMY).withTuitionTiming(VALID_TUITION_TIMING_AMY)
            .withTags(VALID_TAG_EXAM).build();
    public static final Student BOB = new StudentBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withSubjects(VALID_SUBJECT_BOB).withTuitionTiming(VALID_TUITION_TIMING_BOB)
            .withTags(VALID_TAG_WEAK, VALID_TAG_EXAM).build();
    public static final Student CATHY = new StudentBuilder().withName(VALID_NAME_CATHY).withPhone(VALID_PHONE_CATHY)
            .withEmail(VALID_EMAIL_CATHY).withAddress(VALID_ADDRESS_CATHY)
            .withSubjects(VALID_SUBJECT_CATHY).withTuitionTiming(VALID_TUITION_TIMING_CATHY)
            .withTags(VALID_TAG_EXAM).build();

    //For Payments
    public static final Student ALCYONE = new StudentBuilder().withName(VALID_NAME_ALCYONE)
            .withPhone(VALID_PHONE_ALCYONE).withEmail(VALID_EMAIL_ALCYONE).withAddress(VALID_ADDRESS_ALCYONE)
            .withSubjects(VALID_SUBJECT_ALCYONE).withTuitionTiming(VALID_TUITION_TIMING_ALCYONE)
            .withTags(VALID_TAG_EXAM).withPayments("1 200 2 2018").build();
    public static final Student BILLY = new StudentBuilder().withName(VALID_NAME_BILLY).withPhone(VALID_PHONE_BILLY)
            .withEmail(VALID_EMAIL_BILLY).withAddress(VALID_ADDRESS_BILLY)
            .withSubjects(VALID_SUBJECT_BILLY).withTuitionTiming(VALID_TUITION_TIMING_BILLY)
            .withPayments("2 200 2 2018").build();
    public static final Student CABBAGE = new StudentBuilder().withName(VALID_NAME_CABBAGE)
            .withPhone(VALID_PHONE_CABBAGE).withEmail(VALID_EMAIL_CABBAGE).withAddress(VALID_ADDRESS_CABBAGE)
            .withSubjects(VALID_SUBJECT_CABBAGE).withTuitionTiming(VALID_TUITION_TIMING_CABBAGE)
            .withTags(VALID_TAG_EXAM)
            .withPayments("3 300 3 2018").build();
    public static final Student DAISY = new StudentBuilder().withName(VALID_NAME_DAISY).withPhone(VALID_PHONE_DAISY)
            .withEmail(VALID_EMAIL_DAISY).withAddress(VALID_ADDRESS_DAISY)
            .withSubjects(VALID_SUBJECT_DAISY).withTuitionTiming(VALID_TUITION_TIMING_DAISY)
            .withTags(VALID_TAG_EXAM)
            .withPayments("4 400 3 2018").build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER
    public static final String KEYWORD_MATCHING_ALICE = "Alice"; // A keyword that matches MEIER

    private TypicalStudents() {} // prevents instantiation

    /**
     * Returns an {@code TutorHelper} with all the typical students.
     */
    public static TutorHelper getTypicalTutorHelper() {
        TutorHelper ab = new TutorHelper();
        for (Student student : getTypicalStudents()) {
            ab.addStudent(student);
        }
        return ab;
    }

    public static List<Student> getTypicalStudents() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    /**
     * Returns an {@code TutorHelper} with all the typical students with payments inclusive.
     */
    public static TutorHelper getTypicalTutorHelperWithPayments() {
        TutorHelper ab = new TutorHelper();
        for (Student student : getTypicalStudentsWithPayments()) {
            ab.addStudent(student);
        }
        return ab;
    }

    public static List<Student> getTypicalStudentsWithPayments() {
        return new ArrayList<>(Arrays.asList(ALCYONE, BILLY, CABBAGE, DAISY));
    }

    public static List<Index> getSameSubjectStudentsIndexes() {
        return new ArrayList<>(Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(3), Index.fromOneBased(4)));
    }

    public static List<Index> getDifferentSubjectStudentIndexes() {
        return new ArrayList<>(Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(5)));
    }
}
