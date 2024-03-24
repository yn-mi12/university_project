package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Participant;
import commons.dto.EventDTO;
import commons.dto.ParticipantDTO;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class AddParticipantCtrl {
    private final ServerUtilsEvent server;
    private final SplittyCtrl mainCtrl;
    private EventDTO event;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    private Participant participant;

    @Inject
    public AddParticipantCtrl(ServerUtilsEvent server, SplittyCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
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

    public void cancel() {
        clearFields();
        mainCtrl.showEventOverview(event);
    }

    public void ok() {
        try {
            String partFirstName = firstName.getText();
            String partLastName = lastName.getText();
            String partEmail = email.getText();
            participant = new Participant(partFirstName, partLastName, partEmail);
            server.addParticipant(participant, event);
            System.out.println("Add Participant");
            System.out.println("Id:" + event.getId());
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

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
    }

}
