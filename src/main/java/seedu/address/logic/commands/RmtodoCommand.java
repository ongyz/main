package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.syllabusbook.SyllabusBook;

/**
 * Finds all persons whose name matches the keyword and add the to do element to the data.
 * Find is case-insensitive.
 */
public class RmtodoCommand extends Command {

    public static final String COMMAND_WORD = "rmtodo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the selected syllabus of the "
            + "person identified by the index number used in the displayed person list. \n"
            + "Parameters: INDEX SYLLABUS_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 2";

    public static final String MESSAGE_RMTODO_SUCCESS = "Removed selected syllabus from Person: %1$s";
    public static final String MESSAGE_RMTODO_FAILED = "Syllabus does not exist.";

    private final Index index;
    private final Index syllabusIndex;

    public RmtodoCommand(Index index, Index syllabusIndex) {
        this.index = index;
        this.syllabusIndex = syllabusIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personTarget = lastShownList.get(index.getZeroBased());
        SyllabusBook removedSyllBook = personTarget.getSyllabusBook().removeFromSyllabusBook(syllabusIndex);

        Person personSyllRemoved = new Person(personTarget.getName(),
            personTarget.getPhone(), personTarget.getEmail(), personTarget.getAddress(),
            personTarget.getTags(), removedSyllBook);

        model.updatePerson(personTarget, personSyllRemoved);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_RMTODO_SUCCESS, personSyllRemoved));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RmtodoCommand // instanceof handles nulls
                && index.equals(((RmtodoCommand) other).index))
                && syllabusIndex.equals(((RmtodoCommand) other).syllabusIndex); // state check
    }

    /**
     * Stores the details of rmtodo command format.
     */
    public static class RmtodoFormatChecker {
        public static final int PERSON_INDEX_LOCATION = 0;
        public static final int SYLLABUS_INDEX_LOCATION = 1;
        public static final int RMTODO_NUMBER_OF_ARGS = 2;
    }
}
