package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final EventCtrl evntCtrl;
    @FXML
    private ChoiceBox<String> languageBox;
    @FXML
    private TextField createEvent;
    @FXML
    private TextField joinEvent;

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
    }

    public void addEvent() {
        //TODO
    }

}
