package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyTutorHelper;
import seedu.address.model.TutorHelper;
import seedu.address.model.person.Person;

/**
 * An Immutable TutorHelper that is serializable to XML format
 */
@XmlRootElement(name = "TutorHelper")
public class XmlSerializableTutorHelper {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    @XmlElement
    private List<XmlAdaptedPerson> persons;

    /**
     * Creates an empty XmlSerializableTutorHelper.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableTutorHelper() {
        persons = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableTutorHelper(ReadOnlyTutorHelper src) {
        this();
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this TutorHelper into the model's {@code TutorHelper} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedPerson}.
     */
    public TutorHelper toModelType() throws IllegalValueException {
        TutorHelper tutorHelper = new TutorHelper();
        for (XmlAdaptedPerson p : persons) {
            Person person = p.toModelType();
            if (tutorHelper.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            tutorHelper.addPerson(person);
        }
        return tutorHelper;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableTutorHelper)) {
            return false;
        }
        return persons.equals(((XmlSerializableTutorHelper) other).persons);
    }
}
