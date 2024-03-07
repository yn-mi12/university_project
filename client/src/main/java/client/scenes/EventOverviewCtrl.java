package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;

public class EventOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final EventCtrl evntCtrl;
    private ObservableList<Event> data;

    @FXML
    private TableView<Event> table;
    @FXML
    private TableColumn<Event, String> colTitle;
    @FXML
    private TableColumn<Event, String> colInviteCode;
    @FXML
    private ChoiceBox<String> language;

    @Inject
    public EventOverviewCtrl(ServerUtilsEvent server, EventCtrl mainCtrl) {
        this.server = server;
        this.evntCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colTitle.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getTitle()));
        colInviteCode.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getInviteCode()));
        language.getItems().addAll(Config.get().getSupportedLocales().stream().map(Config.SupportedLocale::getName)
                .toList());
        language.setValue(Config.get().getCurrentLocaleName());
        language.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Config.get().setCurrentLocale(newVal);
                Config.get().save();
                Main.reloadUI();
            }
        }));
    }

//    public void addEvent() {
//        evntCtrl.showAdd();
//    }

    public void modify(){
        Event event = table.getSelectionModel().getSelectedItem();
        evntCtrl.showModify(event);
    }

    public void refresh() {
        var events = server.getEvents();
        data = FXCollections.observableList(events);
        table.setItems(data);
    }
}
