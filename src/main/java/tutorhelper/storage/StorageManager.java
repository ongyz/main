package tutorhelper.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import tutorhelper.commons.core.ComponentManager;
import tutorhelper.commons.core.LogsCenter;
import tutorhelper.commons.events.model.TutorHelperChangedEvent;
import tutorhelper.commons.events.storage.DataSavingExceptionEvent;
import tutorhelper.commons.exceptions.DataConversionException;
import tutorhelper.model.ReadOnlyTutorHelper;
import tutorhelper.model.UserPrefs;

/**
 * Manages storage of TutorHelper data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TutorHelperStorage tutorHelperStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TutorHelperStorage tutorHelperStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.tutorHelperStorage = tutorHelperStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TutorHelper methods ==============================

    @Override
    public Path getTutorHelperFilePath() {
        return tutorHelperStorage.getTutorHelperFilePath();
    }

    @Override
    public Optional<ReadOnlyTutorHelper> readTutorHelper() throws DataConversionException, IOException {
        return readTutorHelper(tutorHelperStorage.getTutorHelperFilePath());
    }

    @Override
    public Optional<ReadOnlyTutorHelper> readTutorHelper(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return tutorHelperStorage.readTutorHelper(filePath);
    }

    @Override
    public void saveTutorHelper(ReadOnlyTutorHelper tutorHelper) throws IOException {
        saveTutorHelper(tutorHelper, tutorHelperStorage.getTutorHelperFilePath());
    }

    @Override
    public void saveTutorHelper(ReadOnlyTutorHelper tutorHelper, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        tutorHelperStorage.saveTutorHelper(tutorHelper, filePath);
    }


    @Override
    @Subscribe
    public void handleTutorHelperChangedEvent(TutorHelperChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTutorHelper(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
