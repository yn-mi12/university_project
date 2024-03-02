package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class ModifyEventCtrl {
    private final ServerUtilsEvent server;
    private final EventCtrl mainCtrl;

    @FXML
    public TextField oldTitle;
    private Event selectedEvent;

    @FXML
    private TextField newTitle;
    @Inject
    public ModifyEventCtrl(ServerUtilsEvent server, EventCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public void ok() {
        try {
            Event modifiedEvent = getSelectedEvent();
            modifiedEvent.setTitle(newTitle.getText());
            server.addEvent(modifiedEvent);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showOverview();
    }
//    private Event getOldTitle() {
//        //System.out.println("Get event");
//        var inviteCode = this.inviteCode.getText();
//        var title = this.title.getText();
//        return new Event(title,inviteCode);
//    }
    private void clearFields() {
        oldTitle.clear();
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
}
