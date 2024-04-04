package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
    @FXML
    private Label participantExists;


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
        participantExists.visibleProperty().setValue(false);
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
            participantExists.visibleProperty().setValue(false);
            if (participant != null && participant.getId() != 0 && !participantAlreadyExists()) {
                server.updateParticipant(participant);
            } else if(!participantAlreadyExists()){
                participant.setEvent(event);
                server.addParticipant(participant, event);
                Event updated = server.getByInviteCode(event.getInviteCode());
                clearFields();
                mainCtrl.showEventOverview(updated);
            }else{
                participantExists.visibleProperty().setValue(true);
            }
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private boolean participantAlreadyExists() {
        for (int i = 0; i < event.getParticipants().size(); i++){
            if (firstName.getText().equals(event.getParticipants().get(i).getFirstName())){
                return true;
            }
        }
        return false;
    }

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
    }

}
