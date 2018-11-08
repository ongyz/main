package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalStudents.getTypicalTutorHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.TutorHelperChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.ReadOnlyTutorHelper;
import seedu.address.model.TutorHelper;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlTutorHelperStorage tutorHelperStorage = new XmlTutorHelperStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(tutorHelperStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void tutorHelperReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlTutorHelperStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlTutorHelperStorageTest} class.
         */
        TutorHelper original = getTypicalTutorHelper();
        storageManager.saveTutorHelper(original);
        ReadOnlyTutorHelper retrieved = storageManager.readTutorHelper().get();
        assertEquals(original, new TutorHelper(retrieved));
    }

    @Test
    public void getTutorHelperFilePath() {
        assertNotNull(storageManager.getTutorHelperFilePath());
    }

    @Test
    public void handleTutorHelperChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTutorHelperStorageExceptionThrowingStub(Paths.get("dummy")),
                                             new JsonUserPrefsStorage(Paths.get("dummy")));
        storage.handleTutorHelperChangedEvent(new TutorHelperChangedEvent(new TutorHelper()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTutorHelperStorageExceptionThrowingStub extends XmlTutorHelperStorage {

        public XmlTutorHelperStorageExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveTutorHelper(ReadOnlyTutorHelper tutorHelper, Path filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
