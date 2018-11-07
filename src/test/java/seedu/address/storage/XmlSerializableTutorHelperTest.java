package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.TutorHelper;
import seedu.address.testutil.TypicalStudents;

public class XmlSerializableTutorHelperTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlSerializableTutorHelperTest");
    private static final Path TYPICAL_STUDENTS_FILE = TEST_DATA_FOLDER.resolve("typicalStudentsTutorHelper.xml");
    private static final Path INVALID_STUDENT_FILE = TEST_DATA_FOLDER.resolve("invalidStudentTutorHelper.xml");
    private static final Path DUPLICATE_STUDENT_FILE = TEST_DATA_FOLDER.resolve("duplicateStudentTutorHelper.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void toModelType_typicalStudentsFile_success() throws Exception {
        XmlSerializableTutorHelper dataFromFile = XmlUtil.getDataFromFile(TYPICAL_STUDENTS_FILE,
                XmlSerializableTutorHelper.class);
        TutorHelper tutorHelperFromFile = dataFromFile.toModelType();
        TutorHelper typicalStudentsTutorHelper = TypicalStudents.getTypicalTutorHelper();
        assertEquals(tutorHelperFromFile, typicalStudentsTutorHelper);
    }

    @Test
    public void toModelType_invalidStudentFile_throwsIllegalValueException() throws Exception {
        XmlSerializableTutorHelper dataFromFile = XmlUtil.getDataFromFile(INVALID_STUDENT_FILE,
                XmlSerializableTutorHelper.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_duplicateStudents_throwsIllegalValueException() throws Exception {
        XmlSerializableTutorHelper dataFromFile = XmlUtil.getDataFromFile(DUPLICATE_STUDENT_FILE,
                XmlSerializableTutorHelper.class);
        thrown.expect(IllegalValueException.class);
        thrown.expectMessage(XmlSerializableTutorHelper.MESSAGE_DUPLICATE_STUDENT);
        dataFromFile.toModelType();
    }

}
