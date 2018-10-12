package seedu.address.storage;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.subject.Syllabus;

/**
 * JAXB-friendly adapted version of the Syllabus.
 */
public class XmlAdaptedSyllabus {

    @XmlValue
    private String syllabus;

    @XmlAttribute
    private boolean state;

    /**
     * Constructs an XmlAdaptedSyllabus.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedSyllabus() {}

    /**
     * Converts a given Syllabus into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedSyllabus(Syllabus source) {
        this.syllabus = source.syllabus;
        this.state = source.state;
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Tag object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Syllabus toModelType() throws IllegalValueException {
        if (!Syllabus.isValidSyllabus(syllabus)) {
            throw new IllegalValueException(Syllabus.MESSAGE_SYLLABUS_CONSTRAINTS);
        }
        return new Syllabus(syllabus, state);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof XmlAdaptedSyllabus // instanceof handles nulls
                && syllabus.equals(((XmlAdaptedSyllabus) other).syllabus) // content check
                && state == ((XmlAdaptedSyllabus) other).state); // state check
    }

    @Override
    public int hashCode() {
        return syllabus.hashCode();
    }

}
