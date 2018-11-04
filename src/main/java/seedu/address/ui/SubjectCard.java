package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.subject.Subject;

/**
 * An UI component that displays information of a {@code Subject}.
 */
public class SubjectCard extends UiPart<Region> {

    private static final String FXML = "SubjectCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/TutorHelper-level4/issues/336">The issue on TutorHelper level 4</a>
     */

    public final Subject subject;

    @FXML
    private Label subjectTitle;

    @FXML
    private ProgressBar completionRate;

    @FXML
    private FlowPane subjectContent;

    public SubjectCard(Subject subject) {
        super(FXML);
        this.subject = subject;

        subjectTitle.setText(subject.getSubjectName());
        completionRate.progressProperty().set(subject.getCompletionRate());

        subject.getSubjectContent().forEach(syllabus -> subjectContent.getChildren().add(new Label(syllabus.syllabus)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SubjectCard)) {
            return false;
        }

        // state check
        SubjectCard card = (SubjectCard) other;
        return subjectTitle.getText().equals(card.subjectTitle.getText())
                && completionRate.getProgress() == card.completionRate.getProgress()
                && subjectContent.equals(card.subjectContent);
    }
}
