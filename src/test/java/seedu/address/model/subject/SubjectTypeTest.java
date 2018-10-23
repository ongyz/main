package seedu.address.model.subject;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SubjectTypeTest {

    @Test
    public void isSameType() {
        SubjectType subjectTypeOne = SubjectType.convertStringToSubjectName("Math");
        SubjectType subjectTypeTwo = SubjectType.convertStringToSubjectName("Mathematics");
        SubjectType subjectTypeThree = SubjectType.convertStringToSubjectName("maTHemAtics");
        SubjectType subjectTypeFour = SubjectType.convertStringToSubjectName("Math");
        SubjectType subjectTypeFive = SubjectType.convertStringToSubjectName("maTh");

        SubjectType subjectTypeSix = SubjectType.convertStringToSubjectName("Bio");

        // True due to same type
        assertTrue(subjectTypeOne.equals(subjectTypeTwo));
        assertTrue(subjectTypeOne.equals(subjectTypeThree));
        assertTrue(subjectTypeOne.equals(subjectTypeFour));
        assertTrue(subjectTypeOne.equals(subjectTypeFive));

        // True due to different type
        assertFalse(subjectTypeOne.equals(subjectTypeSix));
    }

    @Test
    public void isValidName() {

        // unregistered subject names
        assertFalse(SubjectType.isValidSubjectName("Animal"));
        assertFalse(SubjectType.isValidSubjectName(" "));
        assertFalse(SubjectType.isValidSubjectName("-"));

        assertTrue(SubjectType.isValidSubjectName("Math"));
        assertTrue(SubjectType.isValidSubjectName("Mathematics"));
        assertTrue(SubjectType.isValidSubjectName("maTHematics"));
        assertTrue(SubjectType.isValidSubjectName("math"));
        assertTrue(SubjectType.isValidSubjectName("mATH"));
    }
}