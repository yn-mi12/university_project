package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;

import commons.dto.EventDTO;
import commons.dto.ParticipantDTO;
import jakarta.ws.rs.WebApplicationException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static jakarta.ws.rs.core.Response.ok;

public class EventOverviewNewCtrl implements Initializable {
    private final ServerUtilsEvent server;
    private ParticipantDTO expensePayer;
    private final SplittyCtrl controller;
    private List<ParticipantDTO> participants;
    @FXML
    private TextArea participantText = new TextArea();
    @FXML
    private SplitMenuButton part;
    @FXML
    public Label eventTitle;
    @FXML
    private ChoiceBox<String> languageBox;
    public EventDTO event;


    @Inject
    public EventOverviewNewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }

    public EventDTO getSelectedEvent() {
        return event;
    }

    public void setSelectedEvent(EventDTO selectedEvent) {
        this.event = selectedEvent;
        this.participants = event.getParticipants();
        ObservableList<MenuItem> names = FXCollections.observableArrayList();
        StringBuilder namesString = new StringBuilder();
        HashMap<MenuItem,ParticipantDTO> map = new HashMap<>();
        int i = 0;
        for (ParticipantDTO p : participants) {
            MenuItem item = new MenuItem(p.getFirstName());
            names.add(item);
            map.put(item,p);
            namesString.append(p.getFirstName());
            if (i < participants.size() - 1)
                namesString.append(", ");
            i++;
        }
        part.getItems().setAll(names);
        participantText.setEditable(false);
        participantText.setText(namesString.toString());
        for (MenuItem mi : part.getItems()) {
            mi.setOnAction(e -> {
                part.setText(mi.getText());
                expensePayer = map.get(mi);
            });
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        languageBox.getItems().addAll(Config.get().getSupportedLocales().stream().map(Config.SupportedLocale::getName)
                .toList());
        languageBox.setValue(Config.get().getCurrentLocaleName());
        languageBox.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Config.get().setCurrentLocale(newVal);
                Config.get().save();
                Main.reloadUIEvent(event);
                controller.showEventOverview(event);
            }
        }));

    }
    public void addExpense() {
        controller.initExpShowOverview(event,expensePayer);
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
        controller.showOverview();
    }

    public void sendInvites() {
        controller.showInvitePage(event);
    }

    public void editTitle(){
        controller.showEditTitle(event);
    }

    public void goBack() {
        controller.showOverview();
    }

    public void deleteEvent() {
        try {
            System.out.println("Delete Event");
            server.deleteEvent(event);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        //clearFields();
        controller.showOverview();
    }
}
