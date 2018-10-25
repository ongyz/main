package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;
import seedu.address.model.util.SubjectsUtil;

/**
 * Finds all persons whose name matches the keyword and add the to do element to the data.
 * Find is case-insensitive.
 */
public class CopySubCommand extends Command {

    public static final String COMMAND_WORD = "copysub";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Copy the identified subject of the student identified "
            + "by the source student index given into the person identified by the target student index"
            + "New values will be appended into the old syllabus if the subject already exist.\n"
            + "Parameters: SOURCE_STUDENT_INDEX SUBJECT_INDEX TARGET_STUDENT_INDEX\n"
            + "Example: " + COMMAND_WORD + "1 2 4";

    public static final String MESSAGE_COPYSUB_SUCCESS = "Copied syllabus to Person: %1$s";
    public static final String MESSAGE_COPYSUB_FAILED_SAME_PERSON =
            "Copying subject to the same person is not allowed: %1$s";

    private final Index sourcePersonIndex;
    private final Index subjectIndex;
    private final Index targetPersonIndex;

    public CopySubCommand(Index sourcePersonIndex, Index subjectIndex, Index targetPersonIndex) {
        this.sourcePersonIndex = sourcePersonIndex;
        this.subjectIndex = subjectIndex;
        this.targetPersonIndex = targetPersonIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (sourcePersonIndex.getZeroBased() >= lastShownList.size()
            || targetPersonIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personSource = lastShownList.get(sourcePersonIndex.getZeroBased());
        Person personTarget = lastShownList.get(targetPersonIndex.getZeroBased());

        if (sourcePersonIndex.equals(targetPersonIndex)) {
            throw new CommandException(String.format(MESSAGE_COPYSUB_FAILED_SAME_PERSON, personSource));
        }

        if (subjectIndex.getZeroBased() >= personSource.getSubjects().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SUBJECT_INDEX);
        }

        Subject selectedSubject = SubjectsUtil.copySubjectFrom(personSource, subjectIndex);
        Set<Subject> updatedSubjects = updateSubjectsFor(personTarget, selectedSubject);
        Person personUpdated = SubjectsUtil.createPersonWithNewSubjects(personTarget, updatedSubjects);

        model.updatePerson(personTarget, personUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_COPYSUB_SUCCESS, personUpdated));
    }

    /**
     * Add subject into personTarget. If personTarget already has the same subject, append the content
     * instead.
     * @param personTarget the person to be updated
     * @param newSubject the subject to be added
     * @return a new {@code Set<Subject>} with updated subjects
     */
    private Set<Subject> updateSubjectsFor(Person personTarget, Subject newSubject) {
        List<Subject> targetSubjects = new ArrayList<>(personTarget.getSubjects());

        if (SubjectsUtil.hasSubject(personTarget, newSubject.getSubjectType())) {
            Index index = SubjectsUtil.findSubjectIndex(personTarget, newSubject.getSubjectType()).get();
            Subject updatedSubject = targetSubjects.get(index.getZeroBased())
                                                   .append(newSubject.getSubjectContent());
            targetSubjects.set(index.getZeroBased(), updatedSubject);
        } else {
            targetSubjects.add(newSubject);
        }

        return new HashSet<>(targetSubjects);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CopySubCommand // instanceof handles nulls
                && sourcePersonIndex.equals(((CopySubCommand) other).sourcePersonIndex)
                && targetPersonIndex.equals(((CopySubCommand) other).targetPersonIndex)
                && subjectIndex.equals(((CopySubCommand) other).subjectIndex));
    }

    /**cop
     * Stores the details of copysyll command format.
     */
    public static class CopySubFormatChecker {
        public static final int SOURCE_PERSON_INDEX = 0;
        public static final int SUBJECT_INDEX = 1;
        public static final int TARGET_PERSON_INDEX = 2;
        public static final int NUMBER_OF_ARGS = 3;
    }
}
