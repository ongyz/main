package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalTutorHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.TutorHelper;
import seedu.address.model.ReadOnlyTutorHelper;

public class XmlTutorHelperStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlAddressBookStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readTutorHelper_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readTutorHelper(null);
    }

    private java.util.Optional<ReadOnlyTutorHelper> readTutorHelper(String filePath) throws Exception {
        return new XmlTutorHelperStorage(Paths.get(filePath)).readTutorHelper(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readTutorHelper("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readTutorHelper("NotXmlFormatTutorHelper.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readTutorHelper_invalidPersonTutorHelper_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readTutorHelper("invalidPersonTutorHelper.xml");
    }

    @Test
    public void readTutorHelper_invalidAndValidPersonTutorHelper_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readTutorHelper("invalidAndValidPersonTutorHelper.xml");
    }

    @Test
    public void readAndSaveTutorHelper_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempTutorHelper.xml");
        TutorHelper original = getTypicalTutorHelper();
        XmlTutorHelperStorage xmlTutorHelperStorage = new XmlTutorHelperStorage(filePath);

        //Save in new file and read back
        xmlTutorHelperStorage.saveTutorHelper(original, filePath);
        ReadOnlyTutorHelper readBack = xmlTutorHelperStorage.readTutorHelper(filePath).get();
        assertEquals(original, new TutorHelper(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        xmlTutorHelperStorage.saveTutorHelper(original, filePath);
        readBack = xmlTutorHelperStorage.readTutorHelper(filePath).get();
        assertEquals(original, new TutorHelper(readBack));

        //Save and read without specifying file path
        original.addPerson(IDA);
        xmlTutorHelperStorage.saveTutorHelper(original); //file path not specified
        readBack = xmlTutorHelperStorage.readTutorHelper().get(); //file path not specified
        assertEquals(original, new TutorHelper(readBack));

    }

    @Test
    public void saveTutorHelper_nullTutorHelper_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveTutorHelper(null, "SomeFile.xml");
    }

    /**
     * Saves {@code TutorHelper} at the specified {@code filePath}.
     */
    private void saveTutorHelper(ReadOnlyTutorHelper tutorHelper, String filePath) {
        try {
            new XmlTutorHelperStorage(Paths.get(filePath))
                    .saveTutorHelper(tutorHelper, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveTutorHelper_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveTutorHelper(new TutorHelper(), null);
    }


}
