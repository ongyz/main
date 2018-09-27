package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;

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

    private SyllabusBook(List<? extends Syllabus> syllabusBook) {
        requireNonNull(syllabusBook);
        this.syllabusContent.addAll(syllabusBook);
    }

    /**
     * Add a {@code Syllabus} to the current syllabus book and returns
     * a new {@code SyllabusBook} containing the newly added syllabus.
     * @param syllabus the {@code Syllabus} to be added
     * @return a new {@code SyllabusBook} containing the newly added syllabus
     */
    public SyllabusBook addToSyllabusBook(Syllabus syllabus) {
        List<Syllabus> newSyllabusBook = new ArrayList<Syllabus>(syllabusContent);
        newSyllabusBook.add(syllabus);
        return new SyllabusBook(newSyllabusBook);
    }

    /**
     * Removes a {@code Syllabus} from the current syllabus book and returns
     * a new {@code SyllabusBook} with the syllabus at given index removed.
     * @param index the index of syllabus to be removed
     * @return a new {@code SyllabusBook} with the syllabus at given index removed.
     */
    public SyllabusBook removeFromSyllabusBook(Index index) {
        List<Syllabus> newSyllabusBook = new ArrayList<Syllabus>(syllabusContent);
        newSyllabusBook.remove(index.getZeroBased());
        return new SyllabusBook(newSyllabusBook);
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
        return  other == this // short circuit if same object
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
