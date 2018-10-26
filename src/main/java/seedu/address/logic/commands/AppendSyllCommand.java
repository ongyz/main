package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SYLLABUS;
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
import seedu.address.model.util.SubjectsUtil;

/**
 * Appends a syllabus topic to a specified subject for a specified student in the TutorHelper.
 */
public class AppendSyllCommand extends Command {

    public static final String COMMAND_WORD = "appendsyll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the syllabus of the person identified "
            + "by the student index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: STUDENT_INDEX SUBJECT_INDEX "
            + "" + PREFIX_SYLLABUS + "SYLLABUS\n"
            + "Example: " + COMMAND_WORD + " 1 1 " + PREFIX_SYLLABUS + "Integration";

    public static final String MESSAGE_APPENDSYLL_SUCCESS = "Added syllabus to Person: %1$s";
    public static final String MESSAGE_DUPLICATE_SYLLABUS = "Syllabus is already in Person: %1$s";

    private final Index personIndex;
    private final Index subjectIndex;
    private final Syllabus syllabus;

    public AppendSyllCommand(Index personIndex, Index subjectIndex, Syllabus syllabus) {
        requireNonNull(personIndex);
        requireNonNull(subjectIndex);
        requireNonNull(syllabus);
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

        if (subjectIndex.getZeroBased() >= personTarget.getSubjects().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SUBJECT_INDEX);
        }

        Set<Subject> addedSubjectContent = addSubjectContentTo(personTarget, subjectIndex, syllabus);
        Person personSubjUpdated = SubjectsUtil.createPersonWithNewSubjects(personTarget, addedSubjectContent);

        model.updatePerson(personTarget, personSubjUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_APPENDSYLL_SUCCESS, personSubjUpdated));
    }

    /**
     * Add syllabus to the student.
     * @param personTarget The student to add to.
     * @param subjectIndex The index of subject to add to.
     * @param syllabus The syllabus to add.
     * @return a new {@code Set<Subject>} with the specified syllabus added
     */
    private Set<Subject> addSubjectContentTo(Person personTarget, Index subjectIndex, Syllabus syllabus)
        throws CommandException {
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());
        Subject selectedSubject = subjects.get(subjectIndex.getZeroBased());

        if (selectedSubject.contains(syllabus)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_SYLLABUS, personTarget));
        }

        Subject updatedSubject = selectedSubject.add(syllabus);
        subjects.set(subjectIndex.getZeroBased(), updatedSubject);
        return new HashSet<>(subjects);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AppendSyllCommand // instanceof handles nulls
                && personIndex.equals(((AppendSyllCommand) other).personIndex))
                && syllabus.equals(((AppendSyllCommand) other).syllabus); // state check
    }

    /**
     * Stores the details of the AppendSyll command format.
     */
    public static class AppendSyllFormatChecker {
        public static final int PERSON_INDEX = 0;
        public static final int SUBJECT_INDEX = 1;
        public static final int NUMBER_OF_ARGS = 2;
    }
}
