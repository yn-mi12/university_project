package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.EventTemp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class EventOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final EventCtrl evntCtrl;

    private ObservableList<EventTemp> data;

    @FXML
    private TableView<EventTemp> table;
    @FXML
    private TableColumn<EventTemp, String> colTitle;
    @FXML
    private TableColumn<EventTemp, String> colInviteCode;

    @Inject
    public EventOverviewCtrl(ServerUtilsEvent server, EventCtrl mainCtrl) {
        this.server = server;
        this.evntCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colTitle.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getTitle()));
        colInviteCode.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getInviteCode()));
    }

    public void addEvent() {
        evntCtrl.showAdd();
    }
    public void modify(){
        EventTemp event = table.getSelectionModel().getSelectedItem();
        evntCtrl.showModify(event);
    }

    public void refresh() {
        var events = server.getEvents();
        data = FXCollections.observableList(events);
        table.setItems(data);
    }
}
