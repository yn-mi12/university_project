package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static jakarta.ws.rs.core.Response.ok;

public class EventOverviewNewCtrl implements Initializable {
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;

    @FXML
    public Label eventTitle;
    @FXML
    private ChoiceBox<String> languageBox;
    public Event event;



    @Inject
    public EventOverviewNewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }

    public Event getSelectedEvent(){
        return event;
    }
    public void setSelectedEvent(Event selectedEvent){
        this.event = selectedEvent;
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
                //when the language is changed, it goes to the primary scene,
                // but I don't know how to fix it, so I'm leaving it like this for now
            }
        }));

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
}
