package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Removes a syllabus topic from a specified subject for a specified student.
 */
public class EraseSyllCommand extends Command {

    public static final String COMMAND_WORD = "erasesyll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the selected syllabus of the "
            + "person identified by the syllabus index number in the selected student's subject.\n"
            + "Parameters: STUDENT_INDEX SUBJECT_INDEX SYLLABUS_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 1 2";

    public static final String MESSAGE_ERASESYLL_SUCCESS = "Removed selected syllabus from Person: %1$s";

    private final Index personIndex;
    private final Index subjectIndex;
    private final Index syllabusIndex;

    public EraseSyllCommand(Index personIndex, Index subjectIndex, Index syllabusIndex) {
        this.personIndex = personIndex;
        this.subjectIndex = subjectIndex;
        this.syllabusIndex = syllabusIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personTarget = lastShownList.get(personIndex.getZeroBased());

        Set<Subject> removedSubjectContent = removeSubjectContentFrom(personTarget);
        Person personSubjUpdated = SubjectsUtil.createPersonWithNewSubjects(personTarget, removedSubjectContent);

        model.updatePerson(personTarget, personSubjUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_ERASESYLL_SUCCESS, personSubjUpdated));
    }

    /**
     * Removes syllabus topic from the specified student.
     * @param personTarget The student to remove from.
     * @return a new {@code Set<Subject>} with the specified syllabus removed
     * @throws CommandException if the index to remove from is invalid
     */
    private Set<Subject> removeSubjectContentFrom(Person personTarget) throws CommandException {
        List<Subject> subjects = personTarget.getSubjects().stream().collect(Collectors.toList());

        if (hasExceededNumberOfSubjects(subjects)) {
            throw new CommandException(Messages.MESSAGE_INVALID_SUBJECT_INDEX);
        }

        if (hasExceededNumberOfSyllabus(subjects.get(subjectIndex.getZeroBased()))) {
            throw new CommandException(Messages.MESSAGE_INVALID_SYLLABUS_INDEX);
        }

        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).remove(syllabusIndex);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);
        return new HashSet<>(subjects);
    }

    private boolean hasExceededNumberOfSubjects(List<Subject> subjects) {
        return subjectIndex.getOneBased() > subjects.size();
    }

    private boolean hasExceededNumberOfSyllabus(Subject subject) {
        return syllabusIndex.getOneBased() > subject.getSubjectContent().size();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EraseSyllCommand // instanceof handles nulls
                && personIndex.equals(((EraseSyllCommand) other).personIndex))
                && subjectIndex.equals(((EraseSyllCommand) other).subjectIndex)
                && syllabusIndex.equals(((EraseSyllCommand) other).syllabusIndex); // state check
    }

    /**
     * Stores the details of rmtodo command format.
     */
    public static class EraseSyllFormatChecker {
        public static final int PERSON_INDEX = 0;
        public static final int SUBJECT_INDEX = 1;
        public static final int SYLLABUS_INDEX = 2;
        public static final int NUMBER_OF_ARGS = 3;
    }
}
