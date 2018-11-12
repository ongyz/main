package tutorhelper.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tutorhelper.commons.exceptions.IllegalValueException;
import tutorhelper.model.ReadOnlyTutorHelper;
import tutorhelper.model.TutorHelper;
import tutorhelper.model.student.Payment;
import tutorhelper.model.student.Student;

/**
 * An Immutable TutorHelper that is serializable to XML format
 */
@XmlRootElement(name = "TutorHelper")
public class XmlSerializableTutorHelper {

    public static final String MESSAGE_DUPLICATE_STUDENT = "Students list contains duplicate student(s).";
    private static final int MAX_PAYMENTS_DISPLAYED = 5;

    @XmlElement
    private List<XmlAdaptedStudent> students;

    /**
     * Creates an empty XmlSerializableTutorHelper.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableTutorHelper() {
        students = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableTutorHelper(ReadOnlyTutorHelper src) {
        this();
        students.addAll(src.getStudentList().stream().map(XmlAdaptedStudent::new).collect(Collectors.toList()));
    }

    /**
     * Converts this TutorHelper into the model's {@code TutorHelper} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedStudent}.
     */
    public TutorHelper toModelType() throws IllegalValueException {
        TutorHelper tutorHelper = new TutorHelper();
        for (XmlAdaptedStudent p : students) {
            Student student = p.toModelType();
            if (tutorHelper.hasStudent(student)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_STUDENT);
            }
            List<Payment> pay = student.getPayments();
            while (pay.size() > MAX_PAYMENTS_DISPLAYED) {
                pay.remove(0);
            }
            tutorHelper.addStudent(student);
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
        return students.equals(((XmlSerializableTutorHelper) other).students);
    }
}
