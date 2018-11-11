package tutorhelper.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertTrue;
import static tutorhelper.testutil.EventsUtil.postNow;
import static tutorhelper.testutil.TypicalStudents.ALICE;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import tutorhelper.commons.events.ui.StudentPanelSelectionChangedEvent;
import tutorhelper.model.student.Student;
import tutorhelper.testutil.StudentBuilder;

public class BrowserPanelTest extends GuiUnitTest {

    private StudentPanelSelectionChangedEvent selectionChangedEventStub;
    private BrowserPanel browserPanel;
    private BrowserPanelHandle browserPanelHandle;

    @Before
    public void setUp() {
        selectionChangedEventStub = new StudentPanelSelectionChangedEvent(ALICE);

        guiRobot.interact(() -> browserPanel = new BrowserPanel());
        uiPartRule.setUiPart(browserPanel);

        browserPanelHandle = new BrowserPanelHandle(browserPanel.getRoot());
    }

    @Test
    public void display() {

        // default view
        Student expectedDefaultStudent = new StudentBuilder().build();
        Assert.assertEquals(expectedDefaultStudent, browserPanelHandle.getLoadedStudent());

        // associated page of a student
        postNow(selectionChangedEventStub);

        waitUntilBrowserLoaded(browserPanelHandle);
        assertTrue(ALICE.isSameStudent(browserPanelHandle.getLoadedStudent()));
    }
}
