package tutorhelper.model.subject;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import tutorhelper.testutil.Assert;

public class SyllabusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> Syllabus.makeSyllabus(null));
    }

    @Test
    public void constructor_invalidSyllabus_throwsIllegalArgumentException() {
        String invalidSyllabus = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> Syllabus.makeSyllabus(invalidSyllabus));
    }

    @Test
    public void isValidSyllabus() {
        // null syllabus
        Assert.assertThrows(NullPointerException.class, () -> Syllabus.isValidSyllabus(null));

        // invalid syllabus
        assertFalse(Syllabus.isValidSyllabus("")); // empty string
        assertFalse(Syllabus.isValidSyllabus(" ")); // spaces only

        // valid syllabus
        assertTrue(Syllabus.isValidSyllabus("Kinetics"));
        assertTrue(Syllabus.isValidSyllabus("-")); // one character
        assertTrue(Syllabus.isValidSyllabus("Evolution in Mammals")); // multiple words
    }

    @Test
    public void isEqualSyllabus() {
        Syllabus syllabus = Syllabus.makeSyllabus("Art in Modern Times");
        Syllabus syllabusTwo = Syllabus.makeSyllabus("Art in Modern Times");
        Syllabus syllabusThree = Syllabus.makeSyllabus("Art in the 90s");

        // Identity check
        assertTrue(syllabus.equals(syllabus));

        // True due to same content
        assertTrue(syllabus.equals(syllabusTwo));

        // False due to different content
        assertFalse(syllabus.equals(syllabusThree));
    }
}
