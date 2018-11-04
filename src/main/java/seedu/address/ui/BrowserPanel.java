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
    private Label tuitionTimingLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label paymentsLabel;

    @FXML
    private Label subjectsLabel;

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

            tuitionTimingLabel.setText(person.getTuitionTiming().value);

            addressLabel.setText(person.getAddress().value);

            emailLabel.setText(person.getEmail().value);

            phoneLabel.setText(person.getPhone().value);

            final StringBuilder paymentsBuilder = new StringBuilder();
            List<Payment> payments = new ArrayList<>(person.getPayments());
            for (int i = 0; i < payments.size(); i++) {
                Payment selected = payments.get(i);
                paymentsBuilder.append(String.format("Month: %5d     Year: %10d     Amount: %10d         \n",
                        selected.getMonth(), selected.getYear(), selected.getAmount()));
            }
            paymentsLabel.setText(paymentsBuilder.toString());

            final StringBuilder subjectsBuilder = new StringBuilder();
            List<Subject> subjects = new ArrayList<>(person.getSubjects());
            for (int i = 0; i < subjects.size(); i++) {
                String subject = subjects.get(i).toString();
                subjectsBuilder.append(subject.substring(2, subject.length() - 1) + "\n\n");
            }
            subjectsLabel.setText(subjectsBuilder.toString().trim());
        } else {
            // Person is null, remove all the text.
            nameLabel.setText("");

            tuitionTimingLabel.setText("");

            addressLabel.setText("");

            emailLabel.setText("");

            phoneLabel.setText("");

            paymentsLabel.setText("");

            subjectsLabel.setText("");
        }
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection());
    }
}
