package seedu.address.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SYLLABUS_DIFFERENTIATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_EXAM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TUITION_TIMING_AMY;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_SUBJECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.model.student.Student;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.SubjectType;
import seedu.address.testutil.StudentBuilder;

public class SubjectsUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * All the the test under SubjectsUtilTest will be done based on the following student.
     * Student's details found in {@code CommandTestUtil}
     */
    public final Student sourceAmy = new StudentBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                                        .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                                        .withSubjects(VALID_SUBJECT_AMY).withTuitionTiming(VALID_TUITION_TIMING_AMY)
                                        .withSyllabus(INDEX_FIRST_SUBJECT, VALID_SYLLABUS_DIFFERENTIATION)
                                        .withTags(VALID_TAG_EXAM).build();

    @Test
    public void execute_copySubject() {
        Subject copiedSubject = SubjectsUtil.copySubjectFrom(sourceAmy, INDEX_FIRST_SUBJECT);
        Subject trueSubject = new ArrayList<>(sourceAmy.getSubjects()).get(INDEX_FIRST_SUBJECT.getZeroBased());

        // Equivalent under equals() method
        assertTrue(copiedSubject.equals(trueSubject));

        // Not equivalent under identity check
        assertFalse(copiedSubject == trueSubject);
    }

    @Test
    public void execute_hasSubject_returnsBoolean() {
        assertTrue(SubjectsUtil.hasSubject(sourceAmy, SubjectType.Mathematics));
        assertFalse(SubjectsUtil.hasSubject(sourceAmy, SubjectType.Biology));
        assertFalse(SubjectsUtil.hasSubject(sourceAmy, SubjectType.History));
        assertFalse(SubjectsUtil.hasSubject(sourceAmy, SubjectType.Chemistry));
    }

    @Test
    public void execute_findSubjectIndex_returnOptionalWithValuePresent() {
        Optional<Index> index = SubjectsUtil.findSubjectIndex(sourceAmy, SubjectType.Mathematics);
        assertEquals(index.get(), INDEX_FIRST_SUBJECT);
    }

    @Test
    public void execute_findSubjectIndex_returnEmptyOptional() {
        Optional<Index> index = SubjectsUtil.findSubjectIndex(sourceAmy, SubjectType.Chemistry);
        thrown.expect(NoSuchElementException.class);
        index.get();
    }

    @Test
    public void execute_createStudentWithNewSubjects() {
        List<Subject> subjects = new ArrayList<>(sourceAmy.getSubjects());
        subjects.add(Subject.makeSubject("Chemistry"));
        Student newStudent = SubjectsUtil.createStudentWithNewSubjects(sourceAmy, new HashSet<>(subjects));

        // Equivalent under isSameStudent()
        assertTrue(newStudent.isSameStudent(sourceAmy));

        // Non-equivalent under equals
        assertNotEquals(newStudent, sourceAmy);

        // Number of subject should increase
        assertTrue(newStudent.getSubjects().size() > sourceAmy.getSubjects().size());
    }

}
