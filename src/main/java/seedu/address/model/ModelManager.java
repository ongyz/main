package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TutorHelperChangedEvent;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the TutorHelper data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedTutorHelper versionedTutorHelper;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given TutorHelper and userPrefs.
     */
    public ModelManager(ReadOnlyTutorHelper tutorHelper, UserPrefs userPrefs) {
        super();
        requireAllNonNull(tutorHelper, userPrefs);

        logger.fine("Initializing with tutor helper: " + tutorHelper + " and user prefs " + userPrefs);

        versionedTutorHelper = new VersionedTutorHelper(tutorHelper);
        filteredPersons = new FilteredList<>(versionedTutorHelper.getPersonList());
    }

    public ModelManager() {
        this(new TutorHelper(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyTutorHelper newData) {
        versionedTutorHelper.resetData(newData);
        indicateTutorHelperChanged();
    }

    @Override
    public ReadOnlyTutorHelper getTutorHelper() {
        return versionedTutorHelper;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTutorHelperChanged() {
        raise(new TutorHelperChangedEvent(versionedTutorHelper));
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedTutorHelper.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedTutorHelper.removePerson(target);
        indicateTutorHelperChanged();
    }

    @Override
    public void addPerson(Person person) {
        versionedTutorHelper.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateTutorHelperChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedTutorHelper.updatePerson(target, editedPerson);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateTutorHelperChanged();
    }

    @Override
    public void sortByDay() {
        versionedTutorHelper.sortByDay();
        indicateTutorHelperChanged();
    }

    @Override
    public void sortByTime() {
        versionedTutorHelper.sortByTime();
        indicateTutorHelperChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedTutorHelper}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {

        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    /**
     * Updates the filtered persons with 2 predicates.
     * @param predicate1 The first predicate.
     * @param predicate2 The second predicate.
     */
    public void updateFilteredPersonList(Predicate<Person> predicate1, Predicate<Person> predicate2) {
        requireNonNull(predicate1);
        requireNonNull(predicate2);
        filteredPersons.setPredicate(predicate1);
        filteredPersons.setPredicate(predicate2);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoTutorHelper() {
        return versionedTutorHelper.canUndo();
    }

    @Override
    public boolean canRedoTutorHelper() {
        return versionedTutorHelper.canRedo();
    }

    @Override
    public void undoTutorHelper() {
        versionedTutorHelper.undo();
        indicateTutorHelperChanged();
    }

    @Override
    public void redoTutorHelper() {
        versionedTutorHelper.redo();
        indicateTutorHelperChanged();
    }

    @Override
    public void commitTutorHelper() {
        versionedTutorHelper.commit();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedTutorHelper.equals(other.versionedTutorHelper)
                && filteredPersons.equals(other.filteredPersons);
    }

}
