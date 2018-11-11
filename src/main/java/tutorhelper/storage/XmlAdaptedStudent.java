package tutorhelper.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;

import tutorhelper.commons.exceptions.IllegalValueException;
import tutorhelper.model.student.Address;
import tutorhelper.model.student.Email;
import tutorhelper.model.student.Name;
import tutorhelper.model.student.Payment;
import tutorhelper.model.student.Phone;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;
import tutorhelper.model.tag.Tag;
import tutorhelper.model.tuitiontiming.TuitionTiming;

/**
 * JAXB-friendly version of the Student.
 */
public class XmlAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String tuitionTiming;

    @XmlElement
    private List<XmlAdaptedSubject> subjects = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedPay> payments = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedStudent.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedStudent() {}

    /**
     * Constructs an {@code XmlAdaptedStudent} with the given student details.
     */
    public XmlAdaptedStudent(String name, String phone, String email,
                             String address, List<XmlAdaptedSubject> subjects, String tuitionTiming,
                             List<XmlAdaptedTag> tagged, List<XmlAdaptedPay> payments) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.subjects = subjects;
        this.tuitionTiming = tuitionTiming;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
        if (payments != null) {
            this.payments = new ArrayList<>(payments);
        }
    }

    /**
     * Converts a given Student into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedStudent
     */
    public XmlAdaptedStudent(Student source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        subjects = source.getSubjects().stream()
                .map(XmlAdaptedSubject::new)
                .collect(Collectors.toList());;
        tuitionTiming = source.getTuitionTiming().value;
        tagged = source.getTags().stream()
                .map(XmlAdaptedTag::new)
                .collect(Collectors.toList());
        payments = source.getPayments().stream()
                .map(XmlAdaptedPay::new)
                .collect(Collectors.toList());

    }

    /**
     * Converts this jaxb-friendly adapted student object into the model's Student object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted student
     */
    public Student toModelType() throws IllegalValueException {
        final List<Tag> studentTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            studentTags.add(tag.toModelType());
        }

        if (subjects == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Subject.class.getSimpleName()));
        }
        final List<Subject> studentSubjects = new ArrayList<>();
        for (XmlAdaptedSubject subject : subjects) {
            studentSubjects.add(subject.toModelType());
        }

        final List<Payment> studentPayments = new ArrayList<>();
        for (XmlAdaptedPay payment : payments) {
            studentPayments.add(payment.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (tuitionTiming == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, TuitionTiming.class.getSimpleName()));
        }
        if (!TuitionTiming.isValidTiming(tuitionTiming)) {
            throw new IllegalValueException(TuitionTiming.MESSAGE_TUITION_TIMING_CONSTRAINTS);
        }
        final TuitionTiming modelTuitionTiming = new TuitionTiming(tuitionTiming);

        final Set<Subject> modelSubjects = new HashSet<>(studentSubjects);
        final Set<Tag> modelTags = new HashSet<>(studentTags);
        final List<Payment> modelPayments = new ArrayList<>(studentPayments);

        return new Student(modelName, modelPhone, modelEmail, modelAddress,
                modelSubjects, modelTuitionTiming, modelTags, modelPayments);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedStudent)) {
            return false;
        }

        XmlAdaptedStudent otherStudent = (XmlAdaptedStudent) other;
        return Objects.equals(name, otherStudent.name)
                && Objects.equals(phone, otherStudent.phone)
                && Objects.equals(email, otherStudent.email)
                && Objects.equals(address, otherStudent.address)
                && subjects.equals(otherStudent.subjects)
                && Objects.equals(tuitionTiming, otherStudent.tuitionTiming)
                && tagged.equals(otherStudent.tagged)
                && payments.equals(otherStudent.payments);
    }
}
