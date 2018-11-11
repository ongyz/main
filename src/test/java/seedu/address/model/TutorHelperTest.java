package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_WEAK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_BOB;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.getTypicalTutorHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.student.Student;
import seedu.address.model.student.exceptions.DuplicateStudentException;
import seedu.address.testutil.StudentBuilder;

public class TutorHelperTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final TutorHelper tutorHelper = new TutorHelper();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), tutorHelper.getStudentList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        tutorHelper.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyTutorHelper_replacesData() {
        TutorHelper newData = getTypicalTutorHelper();
        tutorHelper.resetData(newData);
        assertEquals(newData, tutorHelper);
    }

    @Test
    public void resetData_withDuplicateStudents_throwsDuplicateStudentException() {
        // Two students with the same identity fields
        Student editedAlice = new StudentBuilder(ALICE).withTuitionTiming(VALID_TUITION_TIMING_BOB)
                .withTags(VALID_TAG_WEAK).build();
        List<Student> newStudents = Arrays.asList(ALICE, editedAlice);

        thrown.expect(DuplicateStudentException.class);
        TutorHelperStub newData = new TutorHelperStub(newStudents);

        tutorHelper.resetData(newData);
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        tutorHelper.hasStudent(null);
    }

    @Test
    public void hasStudent_studentNotInTutorHelper_returnsFalse() {
        assertFalse(tutorHelper.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInTutorHelper_returnsTrue() {
        tutorHelper.addStudent(ALICE);
        assertTrue(tutorHelper.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentWithSameIdentityFieldsInTutorHelper_returnsTrue() {
        tutorHelper.addStudent(ALICE);
        Student editedAlice = new StudentBuilder(ALICE).withTuitionTiming(VALID_TUITION_TIMING_BOB)
                .withTags(VALID_TAG_WEAK).build();
        assertTrue(tutorHelper.hasStudent(editedAlice));
    }

    @Test
    public void getStudentList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        tutorHelper.getStudentList().remove(0);
    }

    /**
     * A stub ReadOnlyTutorHelper whose students list can violate interface constraints.
     */
    private static class TutorHelperStub implements ReadOnlyTutorHelper {
        private final ObservableList<Student> students = FXCollections.observableArrayList();

        TutorHelperStub(Collection<Student> students) throws DuplicateStudentException {
            if (hasDuplicateStudents(students)) {
                throw new DuplicateStudentException();
            }
            this.students.setAll(students);
        }

        @Override
        public ObservableList<Student> getStudentList() {
            return students;
        }

        /**
         * Returns true if there is multiple student in the given collection.
         */
        private boolean hasDuplicateStudents (Collection<Student> students) {
            List<Student> studentsList = students.stream().collect(Collectors.toList());
            if (studentsList.size() <= 1) {
                return false;
            }
            for (int i = 0; i < studentsList.size(); i++) {
                for (int j = i + 1; j < studentsList.size(); j++) {
                    if (studentsList.get(i).isSameStudent(studentsList.get(j))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
