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
import seedu.address.model.student.Student;

/**
 * Represents the in-memory model of the TutorHelper data.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedTutorHelper versionedTutorHelper;
    private final FilteredList<Student> filteredStudents;

    /**
     * Initializes a ModelManager with the given TutorHelper and userPrefs.
     */
    public ModelManager(ReadOnlyTutorHelper tutorHelper, UserPrefs userPrefs) {
        super();
        requireAllNonNull(tutorHelper, userPrefs);

        logger.fine("Initializing with tutor helper: " + tutorHelper + " and user prefs " + userPrefs);

        versionedTutorHelper = new VersionedTutorHelper(tutorHelper);
        filteredStudents = new FilteredList<>(versionedTutorHelper.getStudentList());
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
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return versionedTutorHelper.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        versionedTutorHelper.removeStudent(target);
        indicateTutorHelperChanged();
    }

    @Override
    public void addStudent(Student student) {
        versionedTutorHelper.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        indicateTutorHelperChanged();
    }

    @Override
    public void updateStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        versionedTutorHelper.updateStudent(target, editedStudent);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
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

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code versionedTutorHelper}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {

        return FXCollections.unmodifiableObservableList(filteredStudents);
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    /**
     * Updates the filtered students with 2 predicates.
     * @param predicate1 The first predicate.
     * @param predicate2 The second predicate.
     */
    public void updateFilteredStudentList(Predicate<Student> predicate1, Predicate<Student> predicate2) {
        requireNonNull(predicate1);
        requireNonNull(predicate2);
        filteredStudents.setPredicate(predicate1);
        filteredStudents.setPredicate(predicate2);
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
                && filteredStudents.equals(other.filteredStudents);
    }

}
