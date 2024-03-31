package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class AddParticipantCtrl {
    private final ServerUtilsEvent server;
    private final SplittyCtrl mainCtrl;
    private Event event;
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

    public void setParticipant(Participant participant) {
        this.participant = participant;
        this.participant.setId(participant.getId());
    }


    public void setEvent(Event event) {
        this.event = event;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    public void cancel() {
        clearFields();
        participant = null;
        mainCtrl.showEventOverview(event);
    }

    public Participant getParticipant() {
        String partFirstName = firstName.getText();
        String partLastName = lastName.getText();
        String partEmail = email.getText();

        if (participant != null) {
            participant.setFirstName(partFirstName);
            participant.setLastName(partLastName);
            participant.setEmail(partEmail);
            return participant;
        } else
            return new Participant(partFirstName, partLastName, partEmail);
    }

    public void ok() {
        try {
//            if (participant != null && participant.getId() != 0) {
//                setFirstName(participant.getFirstName());
//                setLastName(participant.getLastName());
//                setEmail(participant.getEmail());
//            }
            //TODO looks better if the fields show the old data

            participant = getParticipant();

            if (participant != null && participant.getId() != 0) {
                server.updateParticipant(participant);
                event.updateParticipant(participant);
            } else {
                server.addParticipant(participant, event);
                participant.setEvent(event);
                event.addParticipant(participant);
            }
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
    }

}
