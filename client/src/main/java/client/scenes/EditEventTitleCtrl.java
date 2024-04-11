package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;


public class EditEventTitleCtrl implements Initializable {
    private final ServerUtilsEvent server;
    private final SplittyCtrl mainCtrl;
    @FXML
    public Label oldTitleLabel;
    @FXML
    public Button okButton;
    @FXML
    public Label newTitleText;
    @FXML
    public Button cancelButton;
    @FXML
    public AnchorPane background;
    private Event event;

    @FXML
    private TextField newTitle;

    @FXML
    public Label oldTitle;

    @Inject
    public EditEventTitleCtrl(ServerUtilsEvent server, SplittyCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    private void clearFields() {
        newTitle.clear();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    public void setEvent(Event event){
        this.event = event;
    }

    public Event getEvent(){
        return event;
    }

    public void ok() {
        try {
            String editedTitle = newTitle.getText();
            event.setTitle(editedTitle);
            event.setId(server.getByID(event.getId()).getId());
            event.setTitle(newTitle.getText());
            server.send("/app/titles", event);
            server.send("/app/updated", event);
            //server.editEventTitle(editedTitle, event);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showEventOverview(event);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.isContrastMode())
        {
            background.setStyle("-fx-background-color: #69e0ab;");
            oldTitle.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            okButton.setStyle(Main.changeUI(okButton));
            Main.buttonFeedback(okButton);
            cancelButton.setStyle(Main.changeUI(cancelButton));
            Main.buttonFeedback(cancelButton);
            newTitleText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            oldTitleLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            newTitle.setStyle(Main.changeUI(newTitle));
        }
    }
}
