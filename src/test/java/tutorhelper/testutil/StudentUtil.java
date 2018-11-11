package tutorhelper.testutil;

import java.util.Set;

import tutorhelper.logic.commands.AddCommand;
import tutorhelper.model.student.Student;
import tutorhelper.model.subject.Subject;
import tutorhelper.model.tag.Tag;
import tutorhelper.logic.commands.EditCommand;
import tutorhelper.logic.parser.CliSyntax;

/**
 * A utility class for Student.
 */
public class StudentUtil {

    /**
     * Returns an add command string for adding the {@code student}.
     */
    public static String getAddCommand(Student student) {
        return AddCommand.COMMAND_WORD + " " + getStudentDetails(student);
    }

    /**
     * Returns the part of command string for the given {@code student}'s details.
     */
    public static String getStudentDetails(Student student) {
        StringBuilder sb = new StringBuilder();
        sb.append(CliSyntax.PREFIX_NAME + student.getName().fullName + " ");
        sb.append(CliSyntax.PREFIX_PHONE + student.getPhone().value + " ");
        sb.append(CliSyntax.PREFIX_EMAIL + student.getEmail().value + " ");
        sb.append(CliSyntax.PREFIX_ADDRESS + student.getAddress().value + " ");
        student.getSubjects().stream().forEach(
            s -> sb.append(CliSyntax.PREFIX_SUBJECT + s.getSubjectName() + " ")
        );
        sb.append(CliSyntax.PREFIX_DAY_AND_TIME + student.getTuitionTiming().toString() + " ");
        sb.append(CliSyntax.PREFIX_TAG);
        student.getTags().stream().forEach(
            s -> sb.append(s.tagName + ", ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditStudentDescriptor}'s details.
     */
    public static String getEditStudentDescriptorDetails(EditCommand.EditStudentDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(CliSyntax.PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(CliSyntax.PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(CliSyntax.PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(CliSyntax.PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getSubjects().isPresent()) {
            Set<Subject> subjects = descriptor.getSubjects().get();
            if (subjects.isEmpty()) {
                sb.append(CliSyntax.PREFIX_SUBJECT);
            } else {
                subjects.forEach(s -> sb.append(CliSyntax.PREFIX_SUBJECT).append(s.getSubjectName()).append(" "));
            }
        }
        descriptor.getTuitionTiming().ifPresent(tuitionTiming -> sb.append(CliSyntax.PREFIX_DAY_AND_TIME)
                .append(tuitionTiming.toString()).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(CliSyntax.PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(CliSyntax.PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
