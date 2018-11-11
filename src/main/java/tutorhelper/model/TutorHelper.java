package tutorhelper.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import tutorhelper.model.student.Student;
import tutorhelper.model.student.UniqueStudentList;
import tutorhelper.model.tuitiontiming.TuitionTiming;

/**
 * Wraps all data at the TutorHelper level
 * Duplicates are not allowed (by .isSameStudent comparison)
 */
public class TutorHelper implements ReadOnlyTutorHelper {

    private final UniqueStudentList students;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        students = new UniqueStudentList();
    }

    public TutorHelper() {}

    /**
     * Creates an TutorHelper using the Students in the {@code toBeCopied}
     */
    public TutorHelper(ReadOnlyTutorHelper toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    /**
     * Resets the existing data of this {@code TutorHelper} with {@code newData}.
     */
    public void resetData(ReadOnlyTutorHelper newData) {
        requireNonNull(newData);

        setStudents(newData.getStudentList());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in the TutorHelper.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the TutorHelper.
     * The student must not already exist in the TutorHelper.
     */
    public void addStudent(Student p) {
        students.add(p);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the TutorHelper.
     * The student identity of {@code editedStudent} must not be the same as
     * another existing student in the TutorHelper.
     */
    public void updateStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);
        students.setStudent(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code TutorHelper}.
     * {@code key} must exist in the TutorHelper.
     */
    public void removeStudent(Student key) {
        students.remove(key);
    }

    /**
     * Sorts the list based on day.
     */
    public void sortByDay() {
        ObservableList<Student> studentList = students.asModifiableObservableList();
        studentList.sort(((p1, p2) -> {
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
        ObservableList<Student> studentList = students.asModifiableObservableList();
        studentList.sort((p1, p2) -> {
            String time1 = TuitionTiming.convertTwelveHourToTwentyFourHour(p1.getTuitionTiming().time);
            String time2 = TuitionTiming.convertTwelveHourToTwentyFourHour(p2.getTuitionTiming().time);
            assert time1 != null : "time1 should not be null";
            assert time2 != null : "time2 should not be null";

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
        return students.asUnmodifiableObservableList().size() + " students";
        // TODO: refine later
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TutorHelper // instanceof handles nulls
                && students.equals(((TutorHelper) other).students));
    }

    @Override
    public int hashCode() {
        return students.hashCode();
    }
}
