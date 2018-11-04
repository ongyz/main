package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;
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

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label tuitionTimingDayLabel;

    @FXML
    private Label tuitionTimingTimeLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private FlowPane paymentAmount;

    @FXML
    private FlowPane paymentMonth;

    @FXML
    private FlowPane paymentYear;

    @FXML
    private FlowPane subjectList;

    @FXML
    private FlowPane subjectsShort;

    @FXML
    private FlowPane tagsShort;

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

            // Clear previous information
            subjectsShort.getChildren().clear();
            tagsShort.getChildren().clear();
            paymentAmount.getChildren().clear();
            paymentMonth.getChildren().clear();
            paymentYear.getChildren().clear();
            subjectList.getChildren().clear();

            // Fill the labels with info from the person object.
            nameLabel.setText(person.getName().fullName);
            addressLabel.setText(person.getAddress().value);
            emailLabel.setText(person.getEmail().value);
            phoneLabel.setText(person.getPhone().value);

            tuitionTimingDayLabel.setText(person.getTuitionTiming().day.toString().substring(0, 3));
            tuitionTimingTimeLabel.setText(person.getTuitionTiming().time);

            person.getSubjects().forEach(subject -> subjectsShort.getChildren().add(new Label(subject.getSubjectName())));
            person.getTags().forEach(tag -> tagsShort.getChildren().add(new Label(tag.tagName)));

            person.getPayments().forEach(amount -> paymentAmount.getChildren().add(
                    new Label(String.valueOf(amount.getAmount()))));
            person.getPayments().forEach(amount -> paymentMonth.getChildren().add(
                    new Label(String.valueOf(amount.getMonth()))));
            person.getPayments().forEach(amount -> paymentYear.getChildren().add(
                    new Label(String.valueOf(amount.getYear()))));
            person.getSubjects().forEach(subject -> subjectList.getChildren().add(
                    new Label(subject.toString())));

        } else {
            // Person is null, remove all the text.
            nameLabel.setText("");
            addressLabel.setText("");
            emailLabel.setText("");
            phoneLabel.setText("");
            tuitionTimingDayLabel.setText("");
            tuitionTimingTimeLabel.setText("");
            paymentAmount.getChildren().clear();
            paymentMonth.getChildren().clear();
            paymentYear.getChildren().clear();
            tagsShort.getChildren().clear();
            subjectsShort.getChildren().clear();
            subjectList.getChildren().clear();
        }
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection());
    }
}
