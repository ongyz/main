package seedu.address.model.subject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.testutil.Assert;

class SubjectTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Subject.makeSubject(null));
    }

    @Test
    public void constructor_invalidSubjectName_throwsIllegalArgumentException() {
        String invalidSubjectName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> Subject.makeSubject(invalidSubjectName));
    }

    @Test
    public void removeMethod_outOfBounds_throwsCommandException() {
        Subject subject = Subject.makeSubject("Economics");
        Assert.assertThrows(CommandException.class, () -> subject.remove(Index.fromOneBased(2)));
    }

    @Test
    public void toggleStateMethod_outOfBounds_throwsCommandException() {
        Subject subject = Subject.makeSubject("Economics");
        Assert.assertThrows(CommandException.class, () -> subject.toggleState(Index.fromOneBased(2)));
    }

    @Test
    public void test_containSyllabus_returnsTrue() {
        Subject subject = Subject.makeSubject("Physics");

        Syllabus syllabusOne = Syllabus.makeSyllabus("Quantum Physics");
        Syllabus syllabusTwo = Syllabus.makeSyllabus("Kinetics");
        Syllabus syllabusThree = Syllabus.makeSyllabus("Nuclear Physics");

        List<Syllabus> syllabuses = new ArrayList<>();
        syllabuses.add(syllabusTwo);
        syllabuses.add(syllabusThree);

        subject.add(syllabusOne);

        assertTrue(subject.contains(syllabusOne));

        subject.append(syllabuses);

        assertTrue(subject.contains(syllabusTwo));
        assertTrue(subject.contains(syllabusThree));
    }

    @Test
    public void test_containSyllabus_returnsFalse() {
        Subject subject = Subject.makeSubject("Biology");

        Syllabus syllabusOne = Syllabus.makeSyllabus("Molecular Biology");
        Syllabus syllabusTwo = Syllabus.makeSyllabus("Evolution");
        Syllabus syllabusThree = Syllabus.makeSyllabus("Plant Transportation");

        List<Syllabus> syllabuses = new ArrayList<>();
        syllabuses.add(syllabusThree);

        assertFalse(subject.contains(syllabusOne));

        subject.append(syllabuses);
        assertFalse(subject.contains(syllabusTwo));
    }

    @Test
    public void isValidSubjectName() {
        // null subject name
        Assert.assertThrows(NullPointerException.class, () -> SubjectType.isValidSubjectName(null));

        // invalid subject
        assertFalse(SubjectType.isValidSubjectName("")); // empty string
        assertFalse(SubjectType.isValidSubjectName(" ")); // spaces only
        assertFalse(SubjectType.isValidSubjectName("-")); // one character
        assertFalse(SubjectType.isValidSubjectName("Book")); // non valid Subject name

        // valid subject
        assertTrue(SubjectType.isValidSubjectName("Mathematics")); // full name
        assertTrue(SubjectType.isValidSubjectName("Math")); // partial name
        assertTrue(SubjectType.isValidSubjectName("Mathe")); // partial name
        assertTrue(SubjectType.isValidSubjectName("mATh")); // partial name
        assertTrue(SubjectType.isValidSubjectName("MATH")); // partial name in uppercase
        assertTrue(SubjectType.isValidSubjectName("math")); // partial name in lowercase
    }

    @Test
    public void isEqualSubject() {
        Subject biology = Subject.makeSubject("Biology");
        Subject biologyPartial = Subject.makeSubject("bio");

        Subject mathematics = Subject.makeSubject("Mathematics");
        Subject mathematicsUppercase = Subject.makeSubject("MATHEMATICS");

        Subject physics = new Subject(SubjectType.Physics, new ArrayList<>(), 0);
        Subject physicsFilled = new Subject(SubjectType.Physics, new ArrayList<>(), 0);

        physicsFilled.add(Syllabus.makeSyllabus("Quantum Physics"));

        // Identity check
        assertTrue(biology.equals(biology));
        assertTrue(biology.equals(biologyPartial));
        assertTrue(mathematics.equals(mathematicsUppercase));

        // True by name
        assertTrue(physics.getSubjectName().equals(physicsFilled));

        // False due to different content
        assertFalse(physics.equals(physicsFilled));

        // False due to different subjectType
        assertFalse(mathematics.equals(biology));
        assertFalse(mathematicsUppercase.equals(biologyPartial));
    }

    @Test
    public void isCompletionRateUpdating() throws CommandException {
        Subject subject = Subject.makeSubject("Chemistry");
        subject.add(Syllabus.makeSyllabus("Kinetics"));
        subject.add(Syllabus.makeSyllabus("Organic Chemistry"));

        subject.toggleState(Index.fromOneBased(1));
        assertEquals(0.5f, subject.getCompletionRate(), 0.001);

        subject.toggleState(Index.fromOneBased(2));
        assertEquals(1.0f, subject.getCompletionRate(), 0.001);

        subject.toggleState(Index.fromOneBased(1));
        assertEquals(0.5f, subject.getCompletionRate(), 0.001);
    }
}