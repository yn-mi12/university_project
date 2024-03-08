package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class StartScreenCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final EventCtrl evntCtrl;
    @FXML
    private ListView<String> eventList;
    @FXML
    private ChoiceBox<String> languageBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextField codeField;

    @Inject
    public StartScreenCtrl(ServerUtilsEvent server, EventCtrl mainCtrl) {
        this.server = server;
        this.evntCtrl = mainCtrl;
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
                Main.reloadUI();
            }
        }));

        eventList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String id = eventList.getSelectionModel().getSelectedItem().split(": ")[0];
                viewPastEvent(Long.valueOf(id));
            }
        });
    }

    public void createEvent() {
        var title = this.titleField.getText();
        clearFields();
        evntCtrl.showAdd(title);
    }

    public void viewEvent() {
        var inviteCode = this.codeField.getText();
        clearFields();
        //Will be used to view an event joined using the invite code field
    }

    public void viewPastEvent(Long id) {
        //Will be used to view a selected event in the recent events section
    }

    public void refresh() {
        List<Long> ids = new ArrayList<>();
        File eventIDs = new File("client/src/main/java/client/utils/events.txt");
        if(eventIDs.exists()) {
            try {
                Scanner idScanner = new Scanner(eventIDs);
                idScanner.useDelimiter("\r?\n");
                while(idScanner.hasNext()) {
                    ids.add(idScanner.nextLong());
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        List<Event> events = new ArrayList<>();
        for(Long id : ids) {
            events.add(server.getByID(id));
        }
        List<String> titles = new ArrayList<>();
        for(Event e : events) {
            titles.add(e.getId() + ": " + e.getTitle());
        }

        eventList.setItems(FXCollections.observableList(titles));
    }

    private void clearFields() {
        titleField.clear();
        codeField.clear();
    }
}
