package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalTutorHelper;

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
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class TutorHelperTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final TutorHelper tutorHelper = new TutorHelper();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), tutorHelper.getPersonList());
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
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withTuitionTiming(VALID_TUITION_TIMING_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);

        thrown.expect(DuplicatePersonException.class);
        TutorHelperStub newData = new TutorHelperStub(newPersons);

        tutorHelper.resetData(newData);
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        tutorHelper.hasPerson(null);
    }

    @Test
    public void hasPerson_personNotInTutorHelper_returnsFalse() {
        assertFalse(tutorHelper.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInTutorHelper_returnsTrue() {
        tutorHelper.addPerson(ALICE);
        assertTrue(tutorHelper.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInTutorHelper_returnsTrue() {
        tutorHelper.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withTuitionTiming(VALID_TUITION_TIMING_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(tutorHelper.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        tutorHelper.getPersonList().remove(0);
    }

    /**
     * A stub ReadOnlyTutorHelper whose persons list can violate interface constraints.
     */
    private static class TutorHelperStub implements ReadOnlyTutorHelper {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        TutorHelperStub(Collection<Person> persons) throws DuplicatePersonException {
            if (hasDuplicatePersons(persons)) {
                throw new DuplicatePersonException();
            }
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        /**
         * Returns true if there is multiple person in the given collection.
         */
        private boolean hasDuplicatePersons (Collection<Person> persons) {
            List<Person> personsList = persons.stream().collect(Collectors.toList());
            if (personsList.size() <= 1) {
                return false;
            }
            for (int i = 0; i < personsList.size(); i++) {
                for (int j = i + 1; j < personsList.size(); j++) {
                    if (personsList.get(i).isSamePerson(personsList.get(j))) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
