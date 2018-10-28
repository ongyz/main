package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_ALCYONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BILLY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_CABBAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_CATHY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_DAISY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_ALCYONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BILLY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_CABBAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_CATHY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_DAISY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_ALCYONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BILLY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CABBAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_CATHY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_DAISY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_ALCYONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BILLY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_CABBAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_CATHY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_DAISY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_ALCYONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_BILLY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_CABBAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_CATHY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_DAISY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_ALCYONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_BILLY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_CABBAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_CATHY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_DAISY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.model.TutorHelper;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person
            ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withSubjects("Mathematics")
            .withSyllabus(Index.fromOneBased(1), "Integration")
            .withTuitionTiming("Tuesday 8:00pm").withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25").withEmail("johnd@example.com")
            .withPhone("98765432").withSubjects("Chemistry")
            .withSyllabus(Index.fromOneBased(1), "Kinetics", "Organic Chemistry")
            .withTuitionTiming("Monday 5:00pm")
            .withTags("owesMoney", "friends").withPayments("2 200 11 2018", "2 300 12 2018").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz")
            .withPhone("95352563").withEmail("heinz@example.com")
            .withAddress("Wall street").withSubjects("Physics")
            .withSyllabus(Index.fromOneBased(1), "Kinematics", "Forces")
            .withTuitionTiming("Saturday 1:00pm").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier")
            .withPhone("87652533").withEmail("cornelia@example.com")
            .withAddress("10th street").withSubjects("Mathematics")
            .withSyllabus(Index.fromOneBased(1), "Calculus II", "Statistics I")
            .withTuitionTiming("Saturday 3:00pm")
            .withTags("friends").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer")
            .withPhone("9482224").withEmail("werner@example.com")
            .withAddress("Michegan ave").withSubjects("Economics")
            .withSyllabus(Index.fromOneBased(1), "Demand and Supply")
            .withTuitionTiming("Sunday 11:00am").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz")
            .withPhone("9482427").withEmail("lydia@example.com")
            .withAddress("little tokyo").withSubjects("Chemistry")
            .withSyllabus(Index.fromOneBased(1), "Organic Chemistry", "Acids and Bases")
            .withTuitionTiming("Thursday 3:30pm").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best")
            .withPhone("9482442").withEmail("anna@example.com")
            .withAddress("4th street").withSubjects("Economics")
            .withSyllabus(Index.fromOneBased(1), "Macroeconomics")
            .withTuitionTiming("Friday 5:00pm").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india")
            .withSubjects("Mathematics").withTuitionTiming("Monday 8:00pm").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave")
            .withSubjects("Physics").withTuitionTiming("Wednesday 6:00pm").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
            .withSubjects(VALID_SUBJECT_AMY).withTuitionTiming(VALID_TUITION_TIMING_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withSubjects(VALID_SUBJECT_BOB).withTuitionTiming(VALID_TUITION_TIMING_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();
    public static final Person CATHY = new PersonBuilder().withName(VALID_NAME_CATHY).withPhone(VALID_PHONE_CATHY)
            .withEmail(VALID_EMAIL_CATHY).withAddress(VALID_ADDRESS_CATHY)
            .withSubjects(VALID_SUBJECT_CATHY).withTuitionTiming(VALID_TUITION_TIMING_CATHY)
            .withTags(VALID_TAG_FRIEND)
            .build();
    //For Payments
    public static final Person ALCYONE = new PersonBuilder().withName(VALID_NAME_ALCYONE).withPhone(VALID_PHONE_ALCYONE)
            .withEmail(VALID_EMAIL_ALCYONE).withAddress(VALID_ADDRESS_ALCYONE)
            .withSubjects(VALID_SUBJECT_ALCYONE).withTuitionTiming(VALID_TUITION_TIMING_ALCYONE)
            .withTags(VALID_TAG_FRIEND).withPayments("1 200 2 2018").build();
    public static final Person BILLY = new PersonBuilder().withName(VALID_NAME_BILLY).withPhone(VALID_PHONE_BILLY)
            .withEmail(VALID_EMAIL_BILLY).withAddress(VALID_ADDRESS_BILLY)
            .withSubjects(VALID_SUBJECT_BILLY).withTuitionTiming(VALID_TUITION_TIMING_BILLY)
            .withPayments("2 200 2 2018")
            .build();
    public static final Person CABBAGE = new PersonBuilder().withName(VALID_NAME_CABBAGE).withPhone(VALID_PHONE_CABBAGE)
            .withEmail(VALID_EMAIL_CABBAGE).withAddress(VALID_ADDRESS_CABBAGE)
            .withSubjects(VALID_SUBJECT_CABBAGE).withTuitionTiming(VALID_TUITION_TIMING_CABBAGE)
            .withTags(VALID_TAG_FRIEND)
            .withPayments("3 300 3 2018")
            .build();
    public static final Person DAISY = new PersonBuilder().withName(VALID_NAME_DAISY).withPhone(VALID_PHONE_DAISY)
            .withEmail(VALID_EMAIL_DAISY).withAddress(VALID_ADDRESS_DAISY)
            .withSubjects(VALID_SUBJECT_DAISY).withTuitionTiming(VALID_TUITION_TIMING_DAISY)
            .withTags(VALID_TAG_FRIEND)
            .withPayments("4 400 3 2018")
            .build();


    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code TutorHelper} with all the typical persons.
     */
    public static TutorHelper getTypicalTutorHelper() {
        TutorHelper ab = new TutorHelper();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    /**
     * Returns an {@code TutorHelper} with all the typical persons.
     */
    public static TutorHelper getTypicalTutorHelperWithPayments() {
        TutorHelper ab = new TutorHelper();
        for (Person person : getTypicalPersonsWithPayments()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersonsWithPayments() {
        return new ArrayList<>(Arrays.asList(ALCYONE, BILLY, CABBAGE, DAISY));
    }

    public static List<Index> getSameSubjectPersonsIndexes() {
        return new ArrayList<>(Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(3), Index.fromOneBased(4)));
    }

    public static List<Index> getDifferentSubjectPersonIndexes() {
        return new ArrayList<>(Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(5)));
    }
}
