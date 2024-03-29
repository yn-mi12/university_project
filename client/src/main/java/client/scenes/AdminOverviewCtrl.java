package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    public ListView eventList;
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
            titles.add(x.getTitle());
        }
        eventList.setItems(FXCollections.observableList(titles));
    }
}
