package tutorhelper.model.student;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static tutorhelper.testutil.TypicalStudents.ALICE;
import static tutorhelper.testutil.TypicalStudents.BOB;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tutorhelper.testutil.StudentBuilder;
import tutorhelper.logic.commands.CommandTestUtil;

public class StudentTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().withTags(CommandTestUtil.VALID_TAG_EXAM).build();
        thrown.expect(UnsupportedOperationException.class);
        student.getTags().remove(0);
    }

    @Test
    public void isSameStudent() {
        // same object -> returns true
        assertTrue(ALICE.isSameStudent(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameStudent(null));

        // different phone and email and address -> returns false
        Student editedAlice = new StudentBuilder(ALICE)
                .withPhone(CommandTestUtil.VALID_PHONE_BOB)
                .withEmail(CommandTestUtil.VALID_EMAIL_BOB)
                .withAddress(CommandTestUtil.VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));


        // different name -> returns false
        editedAlice = new StudentBuilder(ALICE).withName(CommandTestUtil.VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));

        // same name, same phone, different attributes -> returns false
        editedAlice = new StudentBuilder(ALICE).withEmail(CommandTestUtil.VALID_EMAIL_BOB).withAddress(CommandTestUtil.VALID_ADDRESS_BOB)
                .withTags(CommandTestUtil.VALID_TAG_WEAK).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // same name, same email, different attributes -> returns false
        editedAlice = new StudentBuilder(ALICE).withPhone(CommandTestUtil.VALID_PHONE_BOB).withAddress(CommandTestUtil.VALID_ADDRESS_BOB)
                .withTags(CommandTestUtil.VALID_TAG_WEAK).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // same name, same phone, same email, different attributes -> returns false
        editedAlice = new StudentBuilder(ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB).withTags(CommandTestUtil.VALID_TAG_WEAK).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

    }

    @Test
    public void equals() {
        // same values -> returns true
        Student aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different student -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Student editedAlice = new StudentBuilder(ALICE).withName(CommandTestUtil.VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new StudentBuilder(ALICE).withPhone(CommandTestUtil.VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new StudentBuilder(ALICE).withEmail(CommandTestUtil.VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new StudentBuilder(ALICE).withAddress(CommandTestUtil.VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new StudentBuilder(ALICE).withTags(CommandTestUtil.VALID_TAG_WEAK).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
