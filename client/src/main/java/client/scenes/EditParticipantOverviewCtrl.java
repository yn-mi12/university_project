package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class EditParticipantOverviewCtrl {
    private Participant selectedParticipant;
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private Event event;
    @FXML
    public ListView<String> participantList;
    private Stage primaryStage;

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
            controller.showAddParticipant(event);
            AddParticipantCtrl addCtrl = controller.getAddParticipantCtrl();
            addCtrl.setParticipant(selectedParticipant);
            addCtrl.ok();

        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void cancel() {
        clearFields();
        selectedParticipant = null;
        Main.reloadUIEvent(event);
        controller.showEventOverview(event);
    }

    public void deleteParticipant() {
        setParticipant();
        try {
            System.out.println("Delete Participant");
            server.deleteParticipant(server.getParticipantByID(selectedParticipant.getId()));
            event.deleteParticipant(selectedParticipant);
            cancel();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

