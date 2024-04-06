package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.ExpenseParticipant;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EditParticipantOverviewCtrl {
    private Participant selectedParticipant;
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private Event event;
    @FXML
    public ListView<String> participantList;
    private Stage primaryStage;

    @FXML
    private Label noDeleteParticipant;

    @Inject
    public EditParticipantOverviewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }

    public void setEvent(Event event) {
        this.event = event;
        List<Participant> participants = server.getEventParticipants(event);
        List<String> names = new ArrayList<>();
        for (Participant x : participants) {
            names.add(x.getId() + ": " + x.getFirstName());
        }
        participantList.setItems(FXCollections.observableList(names));
    }

    public void clearFields() {
        participantList.getSelectionModel().clearSelection();
    }

    public void setParticipant() {
        String partIdName = participantList.getSelectionModel().getSelectedItem();
        String partId = partIdName.split(":")[0];
        this.selectedParticipant = server.getParticipantByID(Long.parseLong(partId));
    }

    public void editParticipant() {
        setParticipant();
        try {
            event = server.getByInviteCode(event.getInviteCode());
            controller.showAddParticipant(event);
            AddParticipantCtrl addCtrl = controller.getAddParticipantCtrl();
            addCtrl.setParticipant(selectedParticipant);
            addCtrl.setEditPart(true);
            addCtrl.setFirstName(selectedParticipant.getFirstName());
            addCtrl.setLastName(selectedParticipant.getLastName());

            if(selectedParticipant.getEmail() != null)
                addCtrl.setEmail(selectedParticipant.getEmail());

            if(selectedParticipant.getAccountName() != null) {
                addCtrl.setAccountName(selectedParticipant.getAccountName());
                addCtrl.setIban(selectedParticipant.getIban());
                addCtrl.setBic(selectedParticipant.getBic());
            }
            //addCtrl.ok();
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void cancel() {
        controller.getAddParticipantCtrl().setEditPart(false);
        clearFields();
        selectedParticipant = null;
        Main.reloadUIEvent(event);
        controller.showEventOverview(event);
        noDeleteParticipant.visibleProperty().setValue(false);
    }

    public void deleteParticipant() {
        setParticipant();
        try {
            System.out.println("Delete Participant");
            if (!checkParticipantInExpenses()){
                server.deleteParticipant(server.getParticipantByID(selectedParticipant.getId()));
                event.deleteParticipant(selectedParticipant);
                noDeleteParticipant.visibleProperty().setValue(false);
                cancel();
            }else{
                noDeleteParticipant.visibleProperty().setValue(true);
            }

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public boolean checkParticipantInExpenses(){
        List<Participant> participantsInExpenses = new ArrayList<>();
        for (Expense expense : event.getExpenses()){
            Set<ExpenseParticipant> debtors = expense.getDebtors();
            List<ExpenseParticipant> debtorsList = new ArrayList<>(debtors);
            for (ExpenseParticipant expenseParticipant : debtorsList) {
                participantsInExpenses.add(expenseParticipant.getParticipant());
            }
        }
        for (Participant participant: participantsInExpenses){
            if (selectedParticipant.getId()==participant.getId()){
                return true;
            }
        }
        return false;
    }

    public void hideLabel() {
        noDeleteParticipant.visibleProperty().setValue(false);
    }
}

