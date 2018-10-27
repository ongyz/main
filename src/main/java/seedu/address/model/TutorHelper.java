package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.tuitiontiming.TuitionTiming;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class TutorHelper implements ReadOnlyTutorHelper {

    private final UniquePersonList persons;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
    }

    public TutorHelper() {}

    /**
     * Creates an TutorHelper using the Persons in the {@code toBeCopied}
     */
    public TutorHelper(ReadOnlyTutorHelper toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Resets the existing data of this {@code TutorHelper} with {@code newData}.
     */
    public void resetData(ReadOnlyTutorHelper newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void updatePerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code TutorHelper}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Sorts the list based on day.
     */
    public void sortByDay() {
        ObservableList<Person> personList = persons.asModifiableObservableList();
        personList.sort(((p1, p2) -> {
            int p1Day = p1.getTuitionTiming().day.getValue();
            int p2Day = p2.getTuitionTiming().day.getValue();

            if (p1Day - p2Day < 0) {
                return -1;
            } else if (p2Day - p1Day < 0) {
                return 1;
            } else {
                return 0;
            }
        }));
    }

    /**
     * Sorts the list based on time.
     */
    public void sortByTime() {
        ObservableList<Person> personList = persons.asModifiableObservableList();
        personList.sort((p1, p2) -> {
            String time1 = TuitionTiming.convertTwelveHourToTwentyFourHour(p1.getTuitionTiming().time);
            String time2 = TuitionTiming.convertTwelveHourToTwentyFourHour(p2.getTuitionTiming().time);

            if (time1.compareTo(time2) < 1) {
                return -1;
            } else if (time2.compareTo(time1) < 1) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asUnmodifiableObservableList().size() + " persons";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TutorHelper // instanceof handles nulls
                && persons.equals(((TutorHelper) other).persons));
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
