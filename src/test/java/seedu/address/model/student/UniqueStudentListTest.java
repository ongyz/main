package seedu.address.model.student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_WEAK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_BOB;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.student.exceptions.DuplicateStudentException;
import seedu.address.model.student.exceptions.StudentNotFoundException;
import seedu.address.testutil.StudentBuilder;

public class UniqueStudentListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueStudentList uniqueStudentList = new UniqueStudentList();

    @Test
    public void contains_nullStudent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueStudentList.contains(null);
    }

    @Test
    public void contains_studentNotInList_returnsFalse() {
        assertFalse(uniqueStudentList.contains(ALICE));
    }

    @Test
    public void contains_studentInList_returnsTrue() {
        uniqueStudentList.add(ALICE);
        assertTrue(uniqueStudentList.contains(ALICE));
    }

    @Test
    public void contains_studentWithSameIdentityFieldsInList_returnsTrue() {
        uniqueStudentList.add(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withTuitionTiming(VALID_TUITION_TIMING_BOB)
                .withTags(VALID_TAG_WEAK).build();
        assertTrue(uniqueStudentList.contains(editedAlice));
    }

    @Test
    public void add_nullStudent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueStudentList.add(null);
    }

    @Test
    public void add_duplicateStudent_throwsDuplicateStudentException() {
        uniqueStudentList.add(ALICE);
        thrown.expect(DuplicateStudentException.class);
        uniqueStudentList.add(ALICE);
    }

    @Test
    public void setStudent_nullTargetStudent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueStudentList.setStudent(null, ALICE);
    }

    @Test
    public void setStudent_nullEditedStudent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueStudentList.setStudent(ALICE, null);
    }

    @Test
    public void setStudent_targetStudentNotInList_throwsStudentNotFoundException() {
        thrown.expect(StudentNotFoundException.class);
        uniqueStudentList.setStudent(ALICE, ALICE);
    }

    @Test
    public void setStudent_editedStudentIsSameStudent_success() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudent(ALICE, ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(ALICE);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudent_editedStudentHasSameIdentity_success() {
        uniqueStudentList.add(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_WEAK)
                .build();
        uniqueStudentList.setStudent(ALICE, editedAlice);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(editedAlice);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudent_editedStudentHasDifferentIdentity_success() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.setStudent(ALICE, BOB);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudent_editedStudentHasNonUniqueIdentity_throwsDuplicateStudentException() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.add(BOB);
        thrown.expect(DuplicateStudentException.class);
        uniqueStudentList.setStudent(ALICE, BOB);
    }

    @Test
    public void remove_nullStudent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueStudentList.remove(null);
    }

    @Test
    public void remove_studentDoesNotExist_throwsStudentNotFoundException() {
        thrown.expect(StudentNotFoundException.class);
        uniqueStudentList.remove(ALICE);
    }

    @Test
    public void remove_existingStudent_removesStudent() {
        uniqueStudentList.add(ALICE);
        uniqueStudentList.remove(ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudents_nullUniqueStudentList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueStudentList.setStudents((UniqueStudentList) null);
    }

    @Test
    public void setStudents_uniqueStudentList_replacesOwnListWithProvidedUniqueStudentList() {
        uniqueStudentList.add(ALICE);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        uniqueStudentList.setStudents(expectedUniqueStudentList);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudents_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueStudentList.setStudents((List<Student>) null);
    }

    @Test
    public void setStudents_list_replacesOwnListWithProvidedList() {
        uniqueStudentList.add(ALICE);
        List<Student> studentList = Collections.singletonList(BOB);
        uniqueStudentList.setStudents(studentList);
        UniqueStudentList expectedUniqueStudentList = new UniqueStudentList();
        expectedUniqueStudentList.add(BOB);
        assertEquals(expectedUniqueStudentList, uniqueStudentList);
    }

    @Test
    public void setStudents_listWithDuplicateStudents_throwsDuplicateStudentException() {
        List<Student> listWithDuplicateStudents = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateStudentException.class);
        uniqueStudentList.setStudents(listWithDuplicateStudents);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueStudentList.asUnmodifiableObservableList().remove(0);
    }
}
