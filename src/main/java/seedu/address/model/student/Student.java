package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.subject.Subject;
import seedu.address.model.tag.Tag;
import seedu.address.model.tuitiontiming.TuitionTiming;

/**
 * Represents a Student in the TutorHelper.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Subject> subjects = new HashSet<>();
    private final TuitionTiming tuitionTiming;
    private final Set<Tag> tags = new HashSet<>();
    private final List<Payment> payments = new ArrayList<>();

    /**
     * Alternate constructor for student with payment being optional.
     */
    public Student(Name name, Phone phone, Email email, Address address,
                   Set<Subject> subjects, TuitionTiming tuitionTiming, Set<Tag> tags) {
        this(name, phone, email, address, subjects, tuitionTiming, tags, new ArrayList<>());
    }

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Phone phone, Email email, Address address,
                   Set<Subject> subjects, TuitionTiming tuitionTiming, Set<Tag> tags, List<Payment> paymentList) {
        requireAllNonNull(name, phone, email, address, tags, paymentList);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.subjects.addAll(subjects);
        this.tuitionTiming = tuitionTiming;
        this.tags.addAll(tags);
        this.payments.addAll(paymentList);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public TuitionTiming getTuitionTiming() {
        return tuitionTiming;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(subjects);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both students of the same name have similar identity field that is the same.
     * Two student are the same
     * if they have the same name field, phone number, email as well as address field.
     * This defines a weaker notion of equality between two students.
     */
    public boolean isSameStudent(Student otherStudent) {

        if (otherStudent == this) {
            return true;
        }

        if (otherStudent == null) {
            return false;
        }

        boolean nameSame = otherStudent.getName().equals(getName());
        boolean phoneSame = otherStudent.getPhone().equals(getPhone());
        boolean emailSame = otherStudent.getEmail().equals(getEmail());
        boolean addressSame = otherStudent.getAddress().equals(getAddress());
        boolean checkSameFields = nameSame && (phoneSame ||  emailSame || addressSame);
        return checkSameFields;
    }

    /**
     * This function returns true if there is one true boolean
     * Otherwise, returns false.
     */
    public boolean checkDifferOneFunc(boolean... vars) {
        int count = 0;
        for (boolean var: vars) {
            count += ((!var) ? 1 : 0);
        }
        if (count == 1) {
            return true;
        }
        return false;
    }
    /**
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;

        return otherStudent.getName().equals(getName())
                && otherStudent.getPhone().equals(getPhone())
                && otherStudent.getEmail().equals(getEmail())
                && otherStudent.getAddress().equals(getAddress())
                && otherStudent.getSubjects().equals(getSubjects())
                && otherStudent.getTuitionTiming().equals(getTuitionTiming())
                && otherStudent.getTags().equals(getTags())
                && otherStudent.getPayments().equals(getPayments());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, subjects, tuitionTiming, tags, payments);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress());
        return builder.toString();
    }
}
