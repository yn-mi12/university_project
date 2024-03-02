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
public class AddEventCtrl {
    private final ServerUtilsEvent server;
    private final EventCtrl mainCtrl;

    @FXML
    private TextField title;

    @FXML
    private TextField inviteCode;

    @Inject
    public AddEventCtrl(ServerUtilsEvent server, EventCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }
    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void ok() {
        try {
            System.out.println("Add event");
            server.addEvent(getEvent());
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

    public TextField getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode.setText(inviteCode);
    }

    public Event getEvent() {
        System.out.println("Get event");
        var title = this.title.getText();
        return new Event(title);
        //I deleted the inviteCode from the parameters of Event because
        // it is not given as a parameter to the constructor.
        //It is created inside the constructor
    }
    private void clearFields() {
        title.clear();
        inviteCode.clear();
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
