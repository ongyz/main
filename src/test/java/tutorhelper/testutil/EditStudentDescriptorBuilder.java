package tutorhelper.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tutorhelper.logic.commands.EditCommand;
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
 * A utility class to help with building EditStudentDescriptor objects.
 */
public class EditStudentDescriptorBuilder {

    private EditCommand.EditStudentDescriptor descriptor;

    public EditStudentDescriptorBuilder() {
        descriptor = new EditCommand.EditStudentDescriptor();
    }

    public EditStudentDescriptorBuilder(EditCommand.EditStudentDescriptor descriptor) {
        this.descriptor = new EditCommand.EditStudentDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditStudentDescriptor} with fields containing {@code student}'s details
     */
    public EditStudentDescriptorBuilder(Student student) {
        descriptor = new EditCommand.EditStudentDescriptor();
        descriptor.setName(student.getName());
        descriptor.setPhone(student.getPhone());
        descriptor.setEmail(student.getEmail());
        descriptor.setAddress(student.getAddress());
        descriptor.setSubjects(student.getSubjects());
        descriptor.setTuitionTiming(student.getTuitionTiming());
        descriptor.setTags(student.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the {@code Subject} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withSubject(Set<Subject> subjects) {
        descriptor.setSubjects((subjects != null) ? new HashSet<>(subjects) : null);
        return this;
    }

    /**
     * Sets the {@code TuitionTiming} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withTuitionTiming(String tuitionTiming) {
        descriptor.setTuitionTiming(new TuitionTiming(tuitionTiming));
        return this;
    }

    /**
     * Sets the {@code psayment} into a {@code List<Payment>} and set it to the {@code EditStudentDescriptor} that we
     * are building.
     */
    public EditStudentDescriptorBuilder withPayments(List<Payment> payments) {
        descriptor.setPayments((payments != null) ? new ArrayList<>(payments) : null);
        return this;
    }

    /**
     * Sets an empty {@code List<Payment>} and set it to the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withEmptyPayments() {
        descriptor.setPayments(null);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditStudentDescriptor}
     * that we are building.
     */
    public EditStudentDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditCommand.EditStudentDescriptor build() {
        return descriptor;
    }
}
