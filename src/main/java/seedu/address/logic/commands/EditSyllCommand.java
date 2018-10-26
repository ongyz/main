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
 * Finds syllabus in subjectIndex of person with personIndex and edits the syllabus.
 */
public class EditSyllCommand extends Command {
    public static final String COMMAND_WORD = "editsyll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the syllabus of the person identified "
            + "by the student index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: STUDENT_INDEX SUBJECT_INDEX SYLLABUS_INDEX "
            + "" + PREFIX_SYLLABUS + "SYLLABUS\n"
            + "Example: " + COMMAND_WORD + " 1 1 1 " + PREFIX_SYLLABUS + "Integration";

    public static final String MESSAGE_EDITSYLL_SUCCESS = "Edited syllabus to Person: %1$s";
    public static final String MESSAGE_SUBJECT_NOT_FOUND = "Subject index not found in Person";
    public static final String MESSAGE_DUPLICATE_SYLLABUS = "This syllabus already exists in the address book.";

    private final Index personIndex;
    private final Index subjectIndex;
    private final Index syllabusIndex;
    private final Syllabus syllabusEdit;

    public EditSyllCommand(Index personIndex, Index subjectIndex, Index syllabusIndex, Syllabus syllabusEdit) {
        this.personIndex = personIndex;
        this.subjectIndex = subjectIndex;
        this.syllabusIndex = syllabusIndex;
        this.syllabusEdit = syllabusEdit;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personTarget = lastShownList.get(personIndex.getZeroBased());
        List<Subject> subjects = new ArrayList<>(personTarget.getSubjects());

        if (subjects.size() < subjectIndex.getOneBased()) {
            throw new CommandException(String.format(MESSAGE_SUBJECT_NOT_FOUND, personTarget));
        }

        Subject selectedSubject = subjects.get(subjectIndex.getZeroBased());

        if (selectedSubject.contains(syllabusEdit)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_SYLLABUS, personTarget));
        }

        subjects.set(subjectIndex.getZeroBased(), selectedSubject.edit(syllabusEdit, syllabusIndex));
        Set<Subject> editedSubjectContent = new HashSet<>(subjects);

        Person personSubjUpdated = SubjectsUtil.createPersonWithNewSubjects(personTarget, editedSubjectContent);

        model.updatePerson(personTarget, personSubjUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_EDITSYLL_SUCCESS, personSubjUpdated));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EditSyllCommand // instanceof handles nulls
                && personIndex.equals(((EditSyllCommand) other).personIndex)
                && subjectIndex.equals(((EditSyllCommand) other).subjectIndex)
                && syllabusIndex.equals(((EditSyllCommand) other).syllabusIndex)
                && syllabusEdit.equals(((EditSyllCommand) other).syllabusEdit)); // state check
    }

    /**
     * Stores the details of todo command format.
     */
    public static class EditSyllFormatChecker {
        public static final int PERSON_INDEX = 0;
        public static final int SUBJECT_INDEX = 1;
        public static final int SYLLABUS_INDEX = 2;
        public static final int NUMBER_OF_ARGS = 3;
    }
}
