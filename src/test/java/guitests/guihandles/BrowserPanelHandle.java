package guitests.guihandles;

import java.net.URL;

import guitests.GuiRobot;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * A handler for the {@code BrowserPanel} of the UI.
 */
public class BrowserPanelHandle extends NodeHandle<Node> {

    public static final String BROWSER_ID = "#browser";

    private boolean isWebViewLoaded = true;

    private URL lastRememberedUrl;

    public BrowserPanelHandle(Node browserPanelNode) {
        super(browserPanelNode);

        WebView webView = getChildNode(BROWSER_ID);
        WebEngine engine = webView.getEngine();
        new GuiRobot().interact(() -> engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.RUNNING) {
                isWebViewLoaded = false;
            } else if (newState == Worker.State.SUCCEEDED) {
                isWebViewLoaded = true;
            }
        }));
    }

    /**
     * Returns the {@code URL} of the currently loaded page.
     */
    public URL getLoadedUrl() {
        return WebViewUtil.getLoadedUrl(getChildNode(BROWSER_ID));
    }

    /**
     * Translates the given URL into the equivalent {@code Person}
     */
    public Person getLoadedPerson() {
        String query = getLoadedUrl().getQuery()
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll("%20", " ");
        String[] personDataDecoded = query.split("&");
        PersonBuilder createdPerson = new PersonBuilder();

        for (String encodedData: personDataDecoded) {
            String[] idAndValue = encodedData.split("=");
            switch(idAndValue[0]) {
            case "name":
                createdPerson.withName(idAndValue[1]);
                break;
            case "phone":
                createdPerson.withPhone(idAndValue[1]);
                break;
            case "email":
                createdPerson.withEmail(idAndValue[1]);
                break;
            case "address":
                createdPerson.withAddress(idAndValue[1]);
                break;
            default:
                break;
            }
        }
        return createdPerson.build();
    }

    /**
     * Remembers the {@code URL} of the currently loaded page.
     */
    public void rememberUrl() {
        lastRememberedUrl = getLoadedUrl();
    }

    /**
     * Returns true if the current {@code URL} is different from the subjectName remembered by the most recent
     * {@code rememberUrl()} call.
     */
    public boolean isUrlChanged() {
        return !lastRememberedUrl.equals(getLoadedUrl());
    }

    /**
     * Returns true if the browser is done loading a page, or if this browser has yet to load any page.
     */
    public boolean isLoaded() {
        return isWebViewLoaded;
    }
}
