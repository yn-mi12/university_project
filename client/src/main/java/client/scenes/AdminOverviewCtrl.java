package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    public ListView<String> eventList;
    public Button showButton;
    public ComboBox<Label> languageBox;
    public Button showButtonD;
    public Button showButtonE;
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
                    showButtonD.setDisable(false);
                    showButtonE.setDisable(false);
                }
            }
        });

        ObservableList<Label> x = FXCollections.observableArrayList();
        List<Config.SupportedLocale> languages = Config.get().getSupportedLocales().stream().toList();
        for(var item : languages)
        {
            Image icon;
            String iconPath = "client/images/" + item.getCode() + ".png";
            icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(iconPath)));
            ImageView iconImageView = new ImageView(icon);
            iconImageView.setFitHeight(25);
            iconImageView.setPreserveRatio(true);
            x.add(new Label(item.getName(), iconImageView));
        }
        languageBox.setItems(x);
        languageBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Label> call(ListView<Label> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Label item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                        }
                        else {
                            item.setTextFill(Color.color(0, 0, 0));
                            setGraphic(item);
                        }
                    }
                };
            }
        });
        String current = String.valueOf(Config.get().getCurrentLocaleName());
        languageBox.setValue(languageBox.getItems().stream()
                .filter(l -> String.valueOf(l.getText()).equals(current)).findFirst().orElse(null));
        languageBox.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Config.get().setCurrentLocale(newVal.getText());
                Config.get().save();
                Main.refreshAdminOverview();
            }
        }));
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
