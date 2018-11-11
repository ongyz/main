package tutorhelper.model;

import javafx.collections.ObservableList;
import tutorhelper.model.student.Student;

/**
 * Unmodifiable view of the TutorHelper
 */
public interface ReadOnlyTutorHelper {

    /**
     * Returns an unmodifiable view of the students list.
     * This list will not contain any duplicate students.
     */
    ObservableList<Student> getStudentList();

}
