package tutorhelper.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import tutorhelper.commons.events.model.TutorHelperChangedEvent;
import tutorhelper.commons.events.storage.DataSavingExceptionEvent;
import tutorhelper.commons.exceptions.DataConversionException;
import tutorhelper.model.ReadOnlyTutorHelper;
import tutorhelper.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends TutorHelperStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getTutorHelperFilePath();

    @Override
    Optional<ReadOnlyTutorHelper> readTutorHelper() throws DataConversionException, IOException;

    @Override
    void saveTutorHelper(ReadOnlyTutorHelper tutorHelper) throws IOException;

    /**
     * Saves the current version of the TutorHelper to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleTutorHelperChangedEvent(TutorHelperChangedEvent abce);
}
