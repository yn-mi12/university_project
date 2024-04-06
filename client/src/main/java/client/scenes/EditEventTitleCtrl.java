package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;


public class EditEventTitleCtrl {
    private final ServerUtilsEvent server;
    private final SplittyCtrl mainCtrl;
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
            System.out.println("Edit Title");
            String editedTitle = newTitle.getText();
            event.setTitle(editedTitle);
            System.out.println("Edit Title");
            System.out.println("Id:" + event.getId());
            event.setId(server.getByInviteCode(event.getId()).getId());
            event.setTitle(newTitle.getText());
            server.send("/app/titles", event);
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




}
