package tutorhelper.commons.util;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tutorhelper.model.TutorHelper;
import tutorhelper.model.subject.Subject;
import tutorhelper.storage.XmlAdaptedPay;
import tutorhelper.storage.XmlAdaptedStudent;
import tutorhelper.storage.XmlAdaptedSubject;
import tutorhelper.storage.XmlAdaptedTag;
import tutorhelper.storage.XmlSerializableTutorHelper;
import tutorhelper.testutil.StudentBuilder;
import tutorhelper.testutil.TestUtil;
import tutorhelper.testutil.TutorHelperBuilder;

public class XmlUtilTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlUtilTest");
    private static final Path EMPTY_FILE = TEST_DATA_FOLDER.resolve("empty.xml");
    private static final Path MISSING_FILE = TEST_DATA_FOLDER.resolve("missing.xml");
    private static final Path VALID_FILE = TEST_DATA_FOLDER.resolve("validTutorHelper.xml");
    private static final Path MISSING_STUDENT_FIELD_FILE = TEST_DATA_FOLDER.resolve("missingStudentField.xml");
    private static final Path INVALID_STUDENT_FIELD_FILE = TEST_DATA_FOLDER.resolve("invalidStudentField.xml");
    private static final Path VALID_STUDENT_FILE = TEST_DATA_FOLDER.resolve("validStudent.xml");
    private static final Path TEMP_FILE = TestUtil.getFilePathInSandboxFolder("tempTutorHelper.xml");

    private static final String INVALID_PHONE = "9482asf424";

    private static final String VALID_NAME = "Hans Muster";
    private static final String VALID_PHONE = "94824240";
    private static final String VALID_EMAIL = "hans@example";
    private static final String VALID_ADDRESS = "4th street";
    private static final String VALID_TUITION_TIMING = "Monday 6:00pm";
    private static final List<XmlAdaptedTag> VALID_TAGS = Collections.singletonList(new XmlAdaptedTag("friends"));
    private static final List<XmlAdaptedSubject> VALID_SUBJECTS =
            Collections.singletonList(new XmlAdaptedSubject(Subject.makeSubject("Mathematics")));
    private static final List<XmlAdaptedPay> VALID_PAYMENT = XmlAdaptedPay.setUpTestPaymentValid();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getDataFromFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(null, TutorHelper.class);
    }

    @Test
    public void getDataFromFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.getDataFromFile(VALID_FILE, null);
    }

    @Test
    public void getDataFromFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.getDataFromFile(MISSING_FILE, TutorHelper.class);
    }

    @Test
    public void getDataFromFile_emptyFile_dataFormatMismatchException() throws Exception {
        thrown.expect(JAXBException.class);
        XmlUtil.getDataFromFile(EMPTY_FILE, TutorHelper.class);
    }

    @Test
    public void getDataFromFile_validFile_validResult() throws Exception {
        TutorHelper dataFromFile = XmlUtil.getDataFromFile(VALID_FILE, XmlSerializableTutorHelper.class).toModelType();
        assertEquals(9, dataFromFile.getStudentList().size());
    }

    @Test
    public void xmlAdaptedStudentFromFile_fileWithMissingStudentField_validResult() throws Exception {
        XmlAdaptedStudent actualStudent = XmlUtil.getDataFromFile(
                MISSING_STUDENT_FIELD_FILE, XmlAdaptedStudentWithRootElement.class);
        XmlAdaptedStudent expectedStudent = new XmlAdaptedStudent(null, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    public void xmlAdaptedStudentFromFile_fileWithInvalidStudentField_validResult() throws Exception {
        XmlAdaptedStudent actualStudent = XmlUtil.getDataFromFile(
                INVALID_STUDENT_FIELD_FILE, XmlAdaptedStudentWithRootElement.class);
        XmlAdaptedStudent expectedStudent = new XmlAdaptedStudent(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    public void xmlAdaptedStudentFromFile_fileWithValidStudent_validResult() throws Exception {
        XmlAdaptedStudent actualStudent = XmlUtil.getDataFromFile(
                VALID_STUDENT_FILE, XmlAdaptedStudentWithRootElement.class);
        XmlAdaptedStudent expectedStudent = new XmlAdaptedStudent(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_SUBJECTS, VALID_TUITION_TIMING, VALID_TAGS, VALID_PAYMENT);
        assertEquals(expectedStudent, actualStudent);
    }

    @Test
    public void saveDataToFile_nullFile_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(null, new TutorHelper());
    }

    @Test
    public void saveDataToFile_nullClass_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        XmlUtil.saveDataToFile(VALID_FILE, null);
    }

    @Test
    public void saveDataToFile_missingFile_fileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        XmlUtil.saveDataToFile(MISSING_FILE, new TutorHelper());
    }

    @Test
    public void saveDataToFile_validFile_dataSaved() throws Exception {
        FileUtil.createFile(TEMP_FILE);
        XmlSerializableTutorHelper dataToWrite = new XmlSerializableTutorHelper(new TutorHelper());
        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        XmlSerializableTutorHelper dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableTutorHelper.class);
        assertEquals(dataToWrite, dataFromFile);

        TutorHelperBuilder builder = new TutorHelperBuilder(new TutorHelper());
        dataToWrite = new XmlSerializableTutorHelper(
                builder.withStudent(new StudentBuilder().build()).build());

        XmlUtil.saveDataToFile(TEMP_FILE, dataToWrite);
        dataFromFile = XmlUtil.getDataFromFile(TEMP_FILE, XmlSerializableTutorHelper.class);
        assertEquals(dataToWrite, dataFromFile);
    }

    /**
     * Test class annotated with {@code XmlRootElement} to allow unmarshalling of .xml data to {@code XmlAdaptedStudent}
     * objects.
     */
    @XmlRootElement(name = "student")
    private static class XmlAdaptedStudentWithRootElement extends XmlAdaptedStudent {}
}
