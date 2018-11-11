package systemtests;

import static tutorhelper.ui.testutil.GuiTestAssert.assertListMatching;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import tutorhelper.model.TutorHelper;
import tutorhelper.model.student.Student;
import tutorhelper.model.util.SampleDataUtil;
import tutorhelper.testutil.TestUtil;

public class SampleDataTest extends TutorHelperSystemTest {
    /**
     * Returns null to force test app to load data of the file in {@code getDataFileLocation()}.
     */
    @Override
    protected TutorHelper getInitialData() {
        return null;
    }

    /**
     * Returns a non-existent file location to force test app to load sample data.
     */
    @Override
    protected Path getDataFileLocation() {
        Path filePath = TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
        deleteFileIfExists(filePath);
        return filePath;
    }

    /**
     * Deletes the file at {@code filePath} if it exists.
     */
    private void deleteFileIfExists(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException ioe) {
            throw new AssertionError(ioe);
        }
    }

    @Test
    public void tutorHelper_dataFileDoesNotExist_loadSampleData() {
        Student[] expectedList = SampleDataUtil.getSampleStudents();
        assertListMatching(getStudentListPanel(), expectedList);
    }
}
