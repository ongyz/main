package seedu.address.ui;

import static java.time.Duration.ofMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.address.testutil.TypicalStudents.getTypicalStudents;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysStudent;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardEquals;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import guitests.guihandles.StudentCardHandle;
import guitests.guihandles.StudentListPanelHandle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.model.student.Student;
import seedu.address.storage.XmlSerializableTutorHelper;

public class StudentListPanelTest extends GuiUnitTest {
    private static final ObservableList<Student> TYPICAL_STUDENTS =
            FXCollections.observableList(getTypicalStudents());

    private static final JumpToListRequestEvent JUMP_TO_SECOND_EVENT = new JumpToListRequestEvent(INDEX_SECOND_STUDENT);

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "sandbox");

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 2500;

    private StudentListPanelHandle studentListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_STUDENTS);

        for (int i = 0; i < TYPICAL_STUDENTS.size(); i++) {
            studentListPanelHandle.navigateToCard(TYPICAL_STUDENTS.get(i));
            Student expectedStudent = TYPICAL_STUDENTS.get(i);
            StudentCardHandle actualCard = studentListPanelHandle.getStudentCardHandle(i);

            assertCardDisplaysStudent(expectedStudent, actualCard);
            assertEquals(Integer.toString(i + 1) + ". ", actualCard.getId());
        }
    }

    @Test
    public void handleJumpToListRequestEvent() {
        initUi(TYPICAL_STUDENTS);
        postNow(JUMP_TO_SECOND_EVENT);
        guiRobot.pauseForHuman();

        StudentCardHandle expectedStudent = studentListPanelHandle
                .getStudentCardHandle(INDEX_SECOND_STUDENT.getZeroBased());
        StudentCardHandle selectedStudent = studentListPanelHandle.getHandleToSelectedCard();
        assertCardEquals(expectedStudent, selectedStudent);
    }

    /**
     * Verifies that creating and deleting large number of students in {@code StudentListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() throws Exception {
        ObservableList<Student> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of student cards exceeded time limit");
    }

    /**
     * Returns a list of students containing {@code studentCount} students that is used to populate the
     * {@code StudentListPanel}.
     */
    private ObservableList<Student> createBackingList(int studentCount) throws Exception {
        Path xmlFile = createXmlFileWithStudents(studentCount);
        XmlSerializableTutorHelper xmlTutorHelper =
                XmlUtil.getDataFromFile(xmlFile, XmlSerializableTutorHelper.class);
        return FXCollections.observableArrayList(xmlTutorHelper.toModelType().getStudentList());
    }

    /**
     * Returns a .xml file containing {@code studentCount} students. This file will be deleted when the JVM terminates.
     */
    private Path createXmlFileWithStudents(int studentCount) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n");
        builder.append("<TutorHelper>\n");
        for (int i = 0; i < studentCount; i++) {
            builder.append("<students>\n");
            builder.append("<name>a</name>\n");
            builder.append("<phone>" + (10000000 + i) + "</phone>\n");
            builder.append("<email>" + i + "@example.com</email>\n");
            builder.append("<address>" + i + "</address>\n");
            builder.append("<tuitionTiming>Monday 6:00pm</tuitionTiming>\n");
            builder.append("<subjects subjectName=\"Biology\" completionRate=\"0.0\"/>\n");
            builder.append("<payments>\n");
            builder.append("<amount>400</amount>\n");
            builder.append("<year>2018</year>\n");
            builder.append("<month>8</month>\n");
            builder.append("</payments>\n");
            builder.append("</students>\n");
        }
        builder.append("</TutorHelper>\n");

        Path manyStudentsFile = Paths.get(TEST_DATA_FOLDER + "manyStudents.xml");
        FileUtil.createFile(manyStudentsFile);
        FileUtil.writeToFile(manyStudentsFile, builder.toString());
        manyStudentsFile.toFile().deleteOnExit();
        return manyStudentsFile;
    }

    /**
     * Initializes {@code studentListPanelHandle} with a {@code StudentListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code StudentListPanel}.
     */
    private void initUi(ObservableList<Student> backingList) {
        StudentListPanel studentListPanel = new StudentListPanel(backingList);
        uiPartRule.setUiPart(studentListPanel);

        studentListPanelHandle = new StudentListPanelHandle(getChildNode(studentListPanel.getRoot(),
                StudentListPanelHandle.STUDENT_LIST_VIEW_ID));
    }
}
