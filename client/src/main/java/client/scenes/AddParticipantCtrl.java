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
    @FXML
    private TextField accountName;
    @FXML
    private TextField iban;
    @FXML
    private TextField bic;
    private Participant participant;
    @FXML
    private Label participantExists;
    private boolean editPart = false;


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

    public void setEditPart(boolean editPart) {
        this.editPart = editPart;
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
        participant = null;
        mainCtrl.showEventOverview(event);
    }

    public Participant getParticipant() {
        String partFirstName = firstName.getText();
        String partLastName = lastName.getText();

        String partEmail;
        if(!email.getText().isEmpty())
            partEmail = email.getText();
        else
            partEmail = null;

        String partAccountName;
        String partIban;
        String partBic;
        if(!accountName.getText().isEmpty()) {
            partAccountName = accountName.getText();
            partIban = iban.getText();
            partBic = bic.getText();
        } else {
            partAccountName = null;
            partIban = null;
            partBic = null;
        }

        if (participant != null) {
            participant.setFirstName(partFirstName);
            participant.setLastName(partLastName);
            participant.setEmail(partEmail);
            participant.setAccountName(partAccountName);
            participant.setIban(partIban);
            participant.setBic(partBic);
            return participant;
        } else
            return new Participant(partFirstName, partLastName, partEmail, partAccountName, partIban, partBic);
    }

    public void ok() {
        try {
            if(editPart == false){
            participant = getParticipant();
            participantExists.visibleProperty().setValue(false);
            if (participant != null && !participantAlreadyExists()) {
                server.addParticipant(participant, event);
                Event updated = server.getByInviteCode(event.getInviteCode());
                clearFields();
                mainCtrl.showEventOverview(updated);
            }else{
                participantExists.visibleProperty().setValue(true);
            }
            }
            else {
                participant.setFirstName(getParticipant().getFirstName());
                participant.setLastName(getParticipant().getLastName());
                participant.setEmail(getParticipant().getEmail());
                participantExists.visibleProperty().setValue(false);
                if(participant.getFirstName() != null && participant.getLastName()!=null) {
                    server.updateParticipant(participant);
                    event = server.getByInviteCode(event.getInviteCode());
                    editPart = false;
                    mainCtrl.initEditParticipantOverview(event);
                    mainCtrl.showEditParticipantOverview();
                }
                clearFields();
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
        accountName.clear();
        iban.clear();
        bic.clear();
    }

    public void setFirstName(String firstName) {
        this.firstName.setText(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.setText(lastName);
    }

    public void setEmail(String email) {
        this.email.setText(email);
    }
}
