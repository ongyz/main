package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.testutil.TypicalStudents.BENSON;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.student.NameContainsKeywordsPredicate;
import seedu.address.testutil.TutorHelperBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasStudent(null);
    }

    @Test
    public void hasStudent_studentNotInTutorHelper_returnsFalse() {
        assertFalse(modelManager.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInTutorHelper_returnsTrue() {
        modelManager.addStudent(ALICE);
        assertTrue(modelManager.hasStudent(ALICE));
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredStudentList().remove(0);
    }

    @Test
    public void equals() {
        TutorHelper tutorHelper = new TutorHelperBuilder().withStudent(ALICE).withStudent(BENSON).build();
        TutorHelper differentTutorHelper = new TutorHelper();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(tutorHelper, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(tutorHelper, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different tutorHelper -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentTutorHelper, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredStudentList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(tutorHelper, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setTutorHelperFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(tutorHelper, differentUserPrefs)));
    }
}
