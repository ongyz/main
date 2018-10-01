package seedu.address.model.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import seedu.address.model.Payment;
import seedu.address.model.syllabusbook.SyllabusBook;

import seedu.address.model.tag.Tag;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final ArrayList<Payment> payments = new ArrayList<>();
    private final Subject subject;
    private final TuitionTiming tuitionTiming;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null. Include paymentList.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, ArrayList<Payment> paymentList) {
        requireAllNonNull(name, phone, email, address, tags, paymentList);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.payments.addAll(paymentList);
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email,
                  Address address, Subject subject, TuitionTiming tuitionTiming, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.subject = subject;
        this.tuitionTiming = tuitionTiming;
        this.tags.addAll(tags);
    }

    /**
     * Every field must be present and not null. A constructor to create a person with payments.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, SyllabusBook syllabusBook,
                  ArrayList<Payment> paymentList) {
        requireAllNonNull(name, phone, email, address, tags, syllabusBook, paymentList);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.syllabusBook.syllabusContent.addAll(syllabusBook.syllabusContent);
        this.payments.addAll(paymentList);
    }

    /**
     * Update payment for this person and returns a new instance of this person.
     * @return the same person but updated with payment.
     */
    public Person updatePayment(Payment newPayment) {
        this.payments.add(newPayment);
        return new Person(this.name, this.phone, this.email, this.address, this.tags, this.syllabusBook, this.payments);
    }

    /*
     * Restore payments for this person.
     */
    public void restorePayments(ArrayList<Payment> original) {
        this.payments.addAll(original);
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
  
    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public SyllabusBook getSyllabusBook () {
        return syllabusBook;

    public Subject getSubject() {
        return subject;
    }

    public TuitionTiming getTuitionTiming() {
        return tuitionTiming;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags () {
        return Collections.unmodifiableSet(tags);
    }


    /**
     * Returns true if both persons of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson (Person otherPerson){
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && (otherPerson.getPhone().equals(getPhone()) || otherPerson.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals (Object other){
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getSubject().equals(getSubject())
                && otherPerson.getTuitionTiming().equals(getTuitionTiming())
                && otherPerson.getPayments().equals(getPayments())
                && otherPerson.getTags().equals(getTags())
                && otherPerson.getSyllabusBook().equals(getSyllabusBook());
        }

        @Override
        public int hashCode () {
            // use this method for custom fields hashing instead of implementing your own
            return Objects.hash(name, phone, email, address, subject, tuitionTiming, payments, tags, syllabusBook);
        }

        @Override
        public String toString () {
            final StringBuilder builder = new StringBuilder();
            builder.append(getName())
                    .append(" Phone: ")
                    .append(getPhone())
                    .append(" Email: ")
                    .append(getEmail())
                    .append(" Address: ")
                    .append(getAddress())
                    .append(" Subject: ")
                    .append(getSubject())
                    .append(" Tuition Timing: ")
                    .append(getTuitionTiming())
                    .append(" Tags: ");
            getTags().forEach(builder::append);

            builder.append(" Payments: ");
            getPayments().forEach(builder::append);

            builder.append(" \nSyllabus: ")
                    .append(getSyllabusBook());

            return builder.toString();
        }
