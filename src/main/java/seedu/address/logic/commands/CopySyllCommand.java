package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYLLABUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
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
import seedu.address.model.subject.Syllabus;

/**
 * Finds all persons whose name matches the keyword and add the to do element to the data.
 * Find is case-insensitive.
 */
public class CopySyllCommand extends Command {

    public static final String COMMAND_WORD = "copysyll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Copy the syllabus of the person identified "
            + "by the first name given into the person identified by the second "
            + "name given."
            + "New values will be appended into the old syllabus.\n"
            + "Parameters: SOURCE_STUDENT_NAME TARGET_STUDENT_NAME SUBJECT_INDEX"
            + "Example: " + COMMAND_WORD + PREFIX_NAME + "John Doe " + PREFIX_NAME + "Betsy Doe " + "1";
    // TODO: 12/10/2018 Add requirement: Both must have the subject to be transferred, and search result must be unique

    private static final String MESSAGE_COPYSYLL_SUCCESS = "Copied syllabus to Person: %1$s";

    private final String personIndex;
    private final Index subjectIndex;
    private final Syllabus syllabus;

    public CopySyllCommand(Index personIndex, Index subjectIndex, Syllabus syllabus) {
        this.personIndex = personIndex;
        this.subjectIndex = subjectIndex;
        this.syllabus = syllabus;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personTarget = lastShownList.get(personIndex.getZeroBased());
        Set<Subject> addedSubjectContent = addSubjectContentTo(personTarget, subjectIndex, syllabus);
        Person personSubjUpdated = createUpdatedPersonForTodo(personTarget, addedSubjectContent);

        model.updatePerson(personTarget, personSubjUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_APPENDSYLL_SUCCESS, personSubjUpdated));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personTarget}
     * with the updated {@code Set<Subject> newSubject}.
     * @param personTarget the person to be updated
     * @param newSubject the updated subjects
     * @return a new {@code Person} with updated subjects
     */
    private Person createUpdatedPersonForTodo(Person personTarget, Set<Subject> newSubject) {
        return new Person(personTarget.getName(), personTarget.getPhone(),
                personTarget.getEmail(), personTarget.getAddress(), newSubject,
                personTarget.getTuitionTiming(), personTarget.getTags());
    }

    /**
     * Add syllabus to the person specified
     * @param personTarget the person to add to
     * @param subjectIndex the index of subject to add to
     * @param syllabus the index of syllabus to add
     * @return a new {@code Set<Subject>} with the specified syllabus added
     */
    private Set<Subject> addSubjectContentTo(Person personTarget, Index subjectIndex, Syllabus syllabus) {
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());
        Subject updatedSubject = subjects.get(subjectIndex.getZeroBased()).addToSubjectContent(syllabus);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);
        return new HashSet<>(subjects);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CopySyllCommand // instanceof handles nulls
                && personIndex.equals(((CopySyllCommand) other).personIndex))
                && syllabus.equals(((CopySyllCommand) other).syllabus); // state check
    }

    /**
     * Stores the details of todo command format.
     */
    public static class AppendSyllFormatChecker {
        public static final int PERSON_INDEX_LOCATION = 0;
        public static final int SUBJECT_INDEX_LOCATION = 1;
        public static final int TODO_NUMBER_OF_ARGS = 2;
    }
}
