package seedu.address.testutil;

import seedu.address.model.TutorHelper;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building TutorHelper objects.
 * Example usage: <br>
 *     {@code TutorHelper ab = new TutorHelperBuilder().withPerson("John", "Doe").build();}
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
     * Adds a new {@code Person} to the {@code TutorHelper} that we are building.
     */
    public TutorHelperBuilder withPerson(Person person) {
        tutorHelper.addPerson(person);
        return this;
    }

    public TutorHelper build() {
        return tutorHelper;
    }
}
