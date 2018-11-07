package seedu.address.testutil;

import seedu.address.model.TutorHelper;
import seedu.address.model.student.Student;

/**
 * A utility class to help with building TutorHelper objects.
 * Example usage: <br>
 *     {@code TutorHelper ab = new TutorHelperBuilder().withStudent("John", "Doe").build();}
 */
public class TutorHelperBuilder {

    private TutorHelper tutorHelper;

    public TutorHelperBuilder() {
        tutorHelper = new TutorHelper();
    }

    public TutorHelperBuilder(TutorHelper tutorHelper) {
        this.tutorHelper = tutorHelper;
    }

    /**
     * Adds a new {@code Student} to the {@code TutorHelper} that we are building.
     */
    public TutorHelperBuilder withStudent(Student student) {
        tutorHelper.addStudent(student);
        return this;
    }

    public TutorHelper build() {
        return tutorHelper;
    }
}
