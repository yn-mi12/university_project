package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
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

public class AddParticipantCtrl implements Initializable {
    private final ServerUtilsEvent server;
    private final SplittyCtrl mainCtrl;
    @FXML
    public AnchorPane background;
    @FXML
    public Button okButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label firstNameText;
    @FXML
    public Label lastNameText;
    @FXML
    public Label emailText;
    @FXML
    public Label accountNameText;
    @FXML
    public Label bankDetailsText;
    @FXML
    public Label ibanText;
    @FXML
    public Label bicText;
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
        Main.reloadUIEvent(event);
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
            if(!editPart){
            participant = getParticipant();
            participantExists.visibleProperty().setValue(false);
            if (participant != null && !participantAlreadyExists()) {
                server.addParticipant(participant, event);
                Event updated = server.getByID(event.getId());
                clearFields();
                mainCtrl.showEventOverview(updated);
                server.send("/app/updated",updated);
            } else {
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
                    event = server.getByID(event.getId());
                    editPart = false;
                    mainCtrl.initEditParticipantOverview(event);
                    mainCtrl.showEditParticipantOverview();
                    server.send("/app/updated",event);
                }
                participantExists.visibleProperty().setValue(true);
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
            if (firstName.getText().equals(event.getParticipants().get(i).getFirstName()) &&
                lastName.getText().equals(event.getParticipants().get(i).getLastName())){
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

    public void setAccountName(String accountName) {
        this.accountName.setText(accountName);
    }

    public void setIban(String iban) {
        this.iban.setText(iban);
    }

    public void setBic(String bic) { this.bic.setText(bic); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.isContrastMode()){
            background.setStyle("-fx-background-color: #69e0ab;");
            okButton.setStyle(Main.changeUI(okButton));
            Main.buttonFeedback(okButton);
            cancelButton.setStyle(Main.changeUI(cancelButton));
            Main.buttonFeedback(cancelButton);
            firstNameText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            lastNameText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            emailText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            ibanText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            bicText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            accountNameText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            firstName.setStyle(Main.changeUI(firstName));
            lastName.setStyle(Main.changeUI(lastName));
            email.setStyle(Main.changeUI(email));
            bic.setStyle(Main.changeUI(bic));
            iban.setStyle(Main.changeUI(iban));
            accountName.setStyle(Main.changeUI(accountName));
            participantExists.setStyle(Main.changeUI(participantExists));
            bankDetailsText.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
        }
    }
}
