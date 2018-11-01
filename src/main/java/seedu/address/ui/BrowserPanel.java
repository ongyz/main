package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Payment;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL =
            "PersonPage.html";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private Label nameLabel;

    @FXML
    private Label tuitionTimingDayLabel;

    @FXML
    private Label tuitionTimingTimeLabel;

    @FXML
    private Label subjectsLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadPersonPage(null);
        registerAsAnEventHandler(this);
    }

    /**
     * Loads a person's information into the AnchorPane.
     * @param person The person whose page is to be loaded into the AnchorPane.
     */
    private void loadPersonPage(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            nameLabel.setText(person.getName().fullName);

            tuitionTimingDayLabel.setText(person.getTuitionTiming().day.toString().substring(0, 3));

            tuitionTimingTimeLabel.setText(person.getTuitionTiming().time);

            final StringBuilder subjectNamesBuilder = new StringBuilder();
            List<Subject> subjectNames = new ArrayList<>(person.getSubjects());
            for (int i = 0; i < subjectNames.size(); i++) {
                subjectNamesBuilder.append(subjectNames.get(i).getSubjectName());
                if (i != subjectNames.size() - 1) {
                    subjectNamesBuilder.append(" | ");
                }
            }
            subjectsLabel.setText(subjectNamesBuilder.toString());

            addressLabel.setText(person.getAddress().value);

            emailLabel.setText(person.getEmail().value);

            phoneLabel.setText(person.getPhone().value);
        } else {
            // Person is null, remove all the text.
            nameLabel.setText("");

            tuitionTimingDayLabel.setText("");

            tuitionTimingTimeLabel.setText("");

            subjectsLabel.setText("");

            addressLabel.setText("");

            emailLabel.setText("");

            phoneLabel.setText("");
        }
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection());
    }
}
