package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;

/**
 * Finds all persons whose name matches the keyword and add the to do element to the data.
 * Find is case-insensitive.
 */
public class CopySubCommand extends Command {

    public static final String COMMAND_WORD = "copysyll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Copy the syllabus of the person identified "
            + "by the first name given into the person identified by the second "
            + "name given."
            + "New values will be appended into the old syllabus.\n"
            + "Parameters: SOURCE_STUDENT_NAME TARGET_STUDENT_NAME SUBJECT_INDEX"
            + "Example: " + COMMAND_WORD + PREFIX_NAME + "John Doe " + PREFIX_NAME + "Betsy Doe " + "1";

    public static final String MESSAGE_COPYSUB_SUCCESS = "Copied syllabus to Person: %1$s";
    public static final String MESSAGE_ZERO_PERSON = "No person found under the search name.";
    public static final String MESSAGE_MULTIPLE_PERSON = "There are multiple persons under the same search name.";

    private final NameContainsKeywordsPredicate predicateSource;
    private final NameContainsKeywordsPredicate predicateTarget;
    private final Index subjectIndex;

    public CopySubCommand(NameContainsKeywordsPredicate predicateSource,
                          NameContainsKeywordsPredicate predicateTarget,
                          Index subjectIndex) {
        this.predicateSource = predicateSource;
        this.predicateTarget = predicateTarget;
        this.subjectIndex = subjectIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList;

        model.updateFilteredPersonList(predicateSource);
        lastShownList = model.getFilteredPersonList();
        checkForAmountOfPerson(lastShownList);

        Person personSource = getPersonFromList(lastShownList);

        model.updateFilteredPersonList(predicateTarget);
        lastShownList = model.getFilteredPersonList();
        checkForAmountOfPerson(lastShownList);

        Person personTarget = getPersonFromList(lastShownList);

        Subject selectedSubject = getSubjectFrom(personSource, subjectIndex);
        Person personUpdated = createUpdatedPersonForCopySyll(personTarget, selectedSubject);

        model.updatePerson(personTarget, personUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_COPYSUB_SUCCESS, personUpdated));
    }

    private Person getPersonFromList(List<Person> personList) {
        return personList.get(0);
    }

    private void checkForAmountOfPerson(List<Person> personList) throws CommandException {
        if (personList.size() < 1) {
            throw new CommandException(MESSAGE_ZERO_PERSON);
        }

        if (personList.size() > 1) {
            throw new CommandException(MESSAGE_MULTIPLE_PERSON);
        }
    }

    private Person createUpdatedPersonForCopySyll(Person personTarget, Subject newSubject) {
        List<Subject> targetSubjects = new ArrayList<>(personTarget.getSubjects());
        targetSubjects.add(newSubject);
        return new Person(personTarget.getName(), personTarget.getPhone(),
                personTarget.getEmail(), personTarget.getAddress(), new HashSet<>(targetSubjects),
                personTarget.getTuitionTiming(), personTarget.getTags());
    }

    private Subject getSubjectFrom(Person personSource, Index subjectIndex) {
        List<Subject> sourceSubjects = new ArrayList<>(personSource.getSubjects());
        Subject selectedSubject = sourceSubjects.get(subjectIndex.getZeroBased());
        return new Subject(selectedSubject.getSubjectType(),
                selectedSubject.getSubjectContent(),
                selectedSubject.getCompletionRate());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CopySubCommand // instanceof handles nulls
                && predicateSource.equals(((CopySubCommand) other).predicateSource)
                && predicateTarget.equals(((CopySubCommand) other).predicateTarget)
                && subjectIndex.equals(((CopySubCommand) other).subjectIndex));
    }

    /**
     * Stores the details of copysyll command format.
     */
    public static class CopySubFormatChecker {
        public static final int SOURCE_PERSON_INDEX = 0;
        public static final int TARGET_PERSON_INDEX = 1;
        public static final int SUBJECT_INDEX = 2;
        public static final int NUMBER_OF_ARGS = 3;
    }
}
