package seedu.address.model.syllabusbook;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.RmtodoCommand.MESSAGE_RMTODO_FAILED;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Represents a Person's syllabus book in the address book.
 * Guarantees: immutable
 */
public class SyllabusBook {

    public final List<Syllabus> syllabusContent = new ArrayList<Syllabus>();

    /**
     * Constructs a {@code SyllabusBook}.
     *
     */
    public SyllabusBook() {}

    public SyllabusBook(SyllabusBook syllabusBook) {
        requireNonNull(syllabusBook);
        this.syllabusContent.addAll(syllabusBook.syllabusContent);
    }

    /**
     * Add a {@code Syllabus} to the current syllabus book and returns
     * a new {@code SyllabusBook} containing the newly added syllabus.
     * @param syllabus the {@code Syllabus} to be added
     * @return a new {@code SyllabusBook} containing the newly added syllabus
     */
    public SyllabusBook addToSyllabusBook(Syllabus syllabus) {
        SyllabusBook newSyllabusBook = new SyllabusBook(this);
        newSyllabusBook.syllabusContent.add(syllabus);
        return new SyllabusBook(newSyllabusBook);
    }

    /**
     * Removes a {@code Syllabus} from the current syllabus book and returns
     * a new {@code SyllabusBook} with the syllabus at given index removed.
     * @param index the index of syllabus to be removed
     * @return a new {@code SyllabusBook} with the syllabus at given index removed.
     *
     * @throws CommandException if {@code index} is out of bound of the syllabus book.
     */
    public SyllabusBook removeFromSyllabusBook(Index index) throws CommandException {
        if (index.getOneBased() > syllabusContent.size()) {
            throw new CommandException(MESSAGE_RMTODO_FAILED);
        }

        SyllabusBook newSyllabusBook = new SyllabusBook(this);
        newSyllabusBook.syllabusContent.remove(index.getZeroBased());
        return newSyllabusBook;
    }

    /**
     * Removes all the {@code Syllabus} from the syllabus book
     * @return an empty {@code SyllabusBook}
     */
    public SyllabusBook clearSyllabusBook() {
        return new SyllabusBook();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        Index numbering;
        for (int i = 0; i < syllabusContent.size(); i++) {
            numbering = Index.fromZeroBased(i);
            builder.append("\n" + numbering.getOneBased() + ". ")
                    .append(syllabusContent.get(i).toString());
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SyllabusBook // instanceof handles nulls
                && contentAreSame((SyllabusBook) other)); // state check
    }

    /**
     * Checks whether the content of this syllabus book is the same
     * with the other syllabus book.
     * @param other the one to be compared to
     * @return true if contents are the same
     */
    private boolean contentAreSame(SyllabusBook other) {
        if (this.syllabusContent.size() != other.syllabusContent.size()) {
            return false;
        }
        for (int i = 0, j = 0; i < syllabusContent.size(); i++, j++) {
            if (!syllabusContent.get(i).equals(other.syllabusContent.get(j))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return syllabusContent.hashCode();
    }
}
