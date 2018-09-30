package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.syllabusbook.SyllabusBook;

/**
 * JAXB-friendly adapted version of the Syllabus Book.
 */
public class XmlAdaptedSyllabusBook {

    @XmlElement
    private List<XmlAdaptedSyllabus> syllabusContent = new ArrayList<>();

    /**
     * Constructs a XmlSyllabusBook}.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedSyllabusBook() {}

    /**
     * Converts a given SyllabusBook into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedSyllabusBook
     */
    public XmlAdaptedSyllabusBook(SyllabusBook source) {
        this.syllabusContent = source.syllabusContent.stream()
                .map(XmlAdaptedSyllabus::new)
                .collect(Collectors.toList());
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public SyllabusBook toModelType() throws IllegalValueException {
        SyllabusBook syllabusBook = new SyllabusBook();
        for (XmlAdaptedSyllabus syllabus : syllabusContent) {
            syllabusBook.addToSyllabusBook(syllabus.toModelType());
        }
        return syllabusBook;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof XmlAdaptedSyllabusBook // instanceof handles nulls
                && contentAreSame((XmlAdaptedSyllabusBook) other)); // state check
    }

    /**
     * Checks whether the content of this syllabus book is the same
     * with the other syllabus book.
     * @param other the one to be compared to
     * @return true if contents are the same
     */
    private boolean contentAreSame(XmlAdaptedSyllabusBook other) {
        if (this.syllabusContent.size() != other.syllabusContent.size()) {
            return false;
        }
        for (int i = 0, j = 0; i < syllabusContent.size(); i++, j++) {
            if (!syllabusContent.get(i).equals(other.syllabusContent.get(j))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return syllabusContent.hashCode();
    }
}
