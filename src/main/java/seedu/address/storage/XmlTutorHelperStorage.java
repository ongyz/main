package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyTutorHelper;

/**
 * A class to access TutorHelper data stored as an xml file on the hard disk.
 */
public class XmlTutorHelperStorage implements TutorHelperStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTutorHelperStorage.class);

    private Path filePath;

    public XmlTutorHelperStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getTutorHelperFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyTutorHelper> readTutorHelper() throws DataConversionException, IOException {
        return readTutorHelper(filePath);
    }

    /**
     * Similar to {@link #readTutorHelper()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTutorHelper> readTutorHelper(Path filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
                logger.info("TutorHelper file " + filePath + " not found");
                return Optional.empty();
        }

        XmlSerializableTutorHelper xmlTutorHelper = XmlFileStorage.loadDataFromSaveFile(filePath);
        try {
            return Optional.of(xmlTutorHelper.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveTutorHelper(ReadOnlyTutorHelper tutorHelper) throws IOException {
        saveTutorHelper(tutorHelper, filePath);
    }

    /**
     * Similar to {@link #saveTutorHelper(ReadOnlyTutorHelper)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveTutorHelper(ReadOnlyTutorHelper TutorHelper, Path filePath) throws IOException {
        requireNonNull(TutorHelper);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        XmlFileStorage.saveDataToFile(filePath, new XmlSerializableTutorHelper(TutorHelper));
    }

}
