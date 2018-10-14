package seedu.address.model.subject;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.commands.EraseSyllCommand.MESSAGE_ERASESYLL_FAILED;
import static seedu.address.model.subject.SubjectType.convertStringToSubjectName;
import static seedu.address.model.subject.SubjectType.isValidSubjectName;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Represents a Subject in the TutorHelper.
 * Guarantees: immutable; name is valid as declared in SubjectType.isValidSubjectName(String)
 */
public class Subject {

    public static final String MESSAGE_SUBJECT_CONSTRAINTS =
            "Subject name should match one of the following: " + SubjectType.listRepresentation();

    private final SubjectType subjectType;
    private final List<Syllabus> subjectContent = new ArrayList<>();
    private final float completionRate;

    /**
     * Constructs a new {@code Subject} from {@code String subjectName}.
     *
     * @param subjectName Subjects that the student is taking.
     */
    public Subject(String subjectName) {
        requireNonNull(subjectName);
        checkArgument(isValidSubjectName(subjectName), MESSAGE_SUBJECT_CONSTRAINTS);
        this.subjectType = convertStringToSubjectName(subjectName);
        completionRate = 0;
    }

    /**
     * Alternative constructor to guarantee immutability.
     */
    public Subject(SubjectType subjectType, List<Syllabus> subjectContent, float completionRate) {
        requireNonNull(subjectType);
        this.subjectType = subjectType;
        this.subjectContent.addAll(subjectContent);
        this.completionRate = completionRate;
    }

    /**
     * Returns subjectType as {@code SubjectType}.
     */
    public SubjectType getSubjectType() {
        return subjectType;
    }

    /**
     * Returns subjectType in string in {@code String}.
     */
    public String getSubjectName() {
        return subjectType.stringRepresentation;
    }


    public List<Syllabus> getSubjectContent() {
        return subjectContent;
    }

    public float getCompletionRate() {
        return completionRate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n[" + getSubjectName() + ": ");
        Index numbering;

        for (int i = 0; i < getSubjectContent().size(); i++) {
            numbering = Index.fromZeroBased(i);
            builder.append("\n" + numbering.getOneBased() + ". ")
                    .append(getSubjectContent().get(i).toString()).append(" ");
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * Returns true if both subjects have the same {@code SubjectType}
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Subject // instanceof handles nulls
                && subjectType.equals(((Subject) other).subjectType)); // state check
    }

    @Override
    public int hashCode() {
        return subjectType.hashCode();
    }

    /**
     * Add a {@code Syllabus} to the current subject and returns
     * a new {@code Subject} containing the newly added syllabus.
     * @param syllabus the {@code Syllabus} to be added
     * @return a new {@code Subject} containing the newly added syllabus
     */
    public Subject addToSubjectContent(Syllabus syllabus) {
        List<Syllabus> newSubjectContent = new ArrayList<>(getSubjectContent());
        newSubjectContent.add(syllabus);
        return new Subject(getSubjectType(), newSubjectContent, getCompletionRate()).updateCompletionRate();
    }

    /**
     * Removes a {@code Syllabus} from the current subject and returns
     * a new {@code Subject} with the syllabus at given index removed.
     * @param index the index of syllabus to be removed
     * @return a new {@code Subject} with the syllabus at given index removed.
     *
     * @throws CommandException if {@code index} is out of bound of the subjectContent.
     */
    public Subject removeFromSubjectContent(Index index) throws CommandException {
        if (index.getOneBased() > getSubjectContent().size()) {
            throw new CommandException(MESSAGE_ERASESYLL_FAILED);
        }

        List<Syllabus> newSubjectContent = new ArrayList<>(getSubjectContent());
        newSubjectContent.remove(index.getZeroBased());
        return new Subject(getSubjectType(), newSubjectContent, getCompletionRate()).updateCompletionRate();
    }

    /**
     * Return a new {@code Subject} with the state of the syllabus
     * identified by the {@code Index index} flipped.
     * @param index the index of syllabus
     * @return new {@code Subject} with the changed syllabus
     * @throws CommandException if index is out of bound of the subjectContent list
     */
    public Subject toggleSubjectContentState(Index index) throws CommandException {
        if (index.getOneBased() > getSubjectContent().size()) {
            throw new CommandException(MESSAGE_ERASESYLL_FAILED);
        }

        List<Syllabus> newSubjectContent = new ArrayList<>(getSubjectContent());
        Syllabus oldSyllabus = newSubjectContent.get(index.getZeroBased());
        Syllabus newSyllabus = new Syllabus(oldSyllabus.syllabus, !oldSyllabus.state);
        newSubjectContent.set(index.getZeroBased(), newSyllabus);

        return new Subject(getSubjectType(), newSubjectContent, getCompletionRate()).updateCompletionRate();
    }

    /**
     * Recalculate the completion rate of the subject.
     * @return a new {@code Subject} with updated completion rate.
     */
    public Subject updateCompletionRate() {
        int subjectContentLength = getSubjectContent().size();
        int numOfSyllabusCompleted = 0;

        for (Syllabus syllabus: getSubjectContent()) {
            if (syllabus.state) {
                numOfSyllabusCompleted++;
            }
        }
        float completionRate = (float) numOfSyllabusCompleted / subjectContentLength;
        return new Subject(getSubjectType(), getSubjectContent(), completionRate);
    }

    /**
     * Removes all the {@code Syllabus} from the syllabus book
     * @return an empty {@code SyllabusBook}
     */
    public Subject clearSubjectContent() {
        return new Subject(getSubjectName());
    }
}
