package seedu.address.ui;

import static guitests.guihandles.WebViewUtil.waitUntilBrowserLoaded;
import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;
import static seedu.address.testutil.TypicalStudents.ALICE;
import static seedu.address.ui.BrowserPanel.DEFAULT_PAGE;
import static seedu.address.ui.UiPart.FXML_FILE_FOLDER;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BrowserPanelHandle;
import seedu.address.MainApp;
import seedu.address.commons.events.ui.StudentPanelSelectionChangedEvent;
import seedu.address.model.student.Payment;
import seedu.address.model.subject.Subject;
import seedu.address.model.tag.Tag;

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
    public void display() throws Exception {
        // default web page
        URL expectedDefaultPageUrl = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        assertEquals(expectedDefaultPageUrl, browserPanelHandle.getLoadedUrl());

        // associated web page of a student
        postNow(selectionChangedEventStub);

        final StringBuilder subjectsBuilder = new StringBuilder();
        ALICE.getSubjects().forEach(subjectsBuilder::append);

        final StringBuilder subjectNamesBuilder = new StringBuilder();
        List<Subject> subjectNames = new ArrayList<>(ALICE.getSubjects());
        for (int i = 0; i < subjectNames.size(); i++) {
            subjectNamesBuilder.append(subjectNames.get(i).getSubjectName());
            if (i != subjectNames.size() - 1) {
                subjectNamesBuilder.append(" | ");
            }
        }

        final StringBuilder paymentsBuilder = new StringBuilder();
        List<Payment> payments = new ArrayList<>(ALICE.getPayments());
        for (int i = 0; i < payments.size(); i++) {
            Payment selected = payments.get(i);
            paymentsBuilder.append(String.format("Month: %5d     Year: %10d     Amount: %10d         ",
                    selected.getMonth(), selected.getYear(), selected.getAmount()));
        }

        final StringBuilder tagsBuilder = new StringBuilder();
        List<Tag> tags = new ArrayList<>(ALICE.getTags());
        for (int i = 0; i < tags.size(); i++) {
            tagsBuilder.append(tags.get(i).toString());
            if (i != tags.size() - 1) {
                tagsBuilder.append(" | ");
            }
        }

        String expectedStudentUrl = ("name=" + ALICE.getName().fullName
                + "&phone=" + ALICE.getPhone().value
                + "&email=" + ALICE.getEmail().value
                + "&address=" + ALICE.getAddress().value.replace("#", "%23")
                + "&tuitionTimingDay=" + ALICE.getTuitionTiming().day.toString().substring(0, 3)
                + "&tuitionTimingTime=" + ALICE.getTuitionTiming().time
                + "&subjectNames=" + subjectNamesBuilder.toString()
                + "&subjects=" + subjectsBuilder.toString()
                + "&payments=" + paymentsBuilder.toString()
                + "&tags=" + tagsBuilder.toString())
                .replaceAll(" ", "%20").replaceAll("\n", "");

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedStudentUrl, browserPanelHandle.getLoadedUrl().getQuery());
    }
}
