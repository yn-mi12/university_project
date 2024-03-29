package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    public ListView<String> eventList;
    public Button showButton;
    private Stage primaryStage;

    @Inject
    public AdminOverviewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Event> events = server.getAllEvents();
        List<String> titles = new ArrayList<>();
        for(Event x : events)
        {
            titles.add(x.getId() + ": " +  x.getTitle());
        }
        eventList.setItems(FXCollections.observableList(titles));
        eventList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (eventList.getSelectionModel().getSelectedItem() != null) {
                    showButton.setDisable(false);
                }
            }
        });
    }

    public void goBack() {
        controller.setAdmin(false);
        controller.showOverview();
    }

    public Event getEvent(){
        String eventIdTitle = eventList.getSelectionModel().getSelectedItem();
        String eventId = eventIdTitle.split(":")[0];
        return server.getByID(Long.parseLong(eventId));
    }

    public void showEvent() throws IOException {
        String eventIdTitle = eventList.getSelectionModel().getSelectedItem();
        String eventId = eventIdTitle.split(":")[0];
        Event event = server.getByID(Long.parseLong(eventId));
        controller.showEventOverview(event);
    }

    public void deleteEvent() {
        try {
            System.out.println("Delete Event");
            server.deleteEvent(getEvent());
            Main.refreshAdminOverview();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
