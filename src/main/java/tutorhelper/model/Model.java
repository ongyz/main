package tutorhelper.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import tutorhelper.model.student.Student;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Student> PREDICATE_SHOW_ALL_STUDENTS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTutorHelper newData);

    /** Returns the TutorHelper */
    ReadOnlyTutorHelper getTutorHelper();

    /**
     * Returns true if a student with the same identity as {@code student} exists in the TutorHelper.
     */
    boolean hasStudent(Student student);

    /**
     * Deletes the given student.
     * The student must exist in the TutorHelper.
     */
    void deleteStudent(Student target);

    /**
     * Adds the given student.
     * {@code student} must not already exist in the TutorHelper.
     */
    void addStudent(Student student);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in the TutorHelper.
     * The student identity of {@code editedStudent} must not be the same as
     * another existing student in the TutorHelper.
     */
    void updateStudent(Student target, Student editedStudent);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in the TutorHelper.
     * The student identity of {@code editedStudent} must not be the same as
     * another existing student in the TutorHelper. This is used to update an
     * internal field of student.
     */
    void updateStudentInternalField(Student target, Student editedStudent);

    /**
     * Sorts the list by day.
     */
    void sortByDay();

    /**
     * Sorts the list by time.
     */
    void sortByTime();

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Updates the filter of the filtered student list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStudentList(Predicate<Student> predicate);

    /**
     * Returns true if the model has previous TutorHelper states to restore.
     */
    boolean canUndoTutorHelper();

    /**
     * Returns true if the model has undone TutorHelper states to restore.
     */
    boolean canRedoTutorHelper();

    /**
     * Restores the model's TutorHelper to its previous state.
     */
    void undoTutorHelper();

    /**
     * Restores the model's TutorHelper to its previously undone state.
     */
    void redoTutorHelper();

    /**
     * Saves the current TutorHelper state for undo/redo.
     */
    void commitTutorHelper();
}
