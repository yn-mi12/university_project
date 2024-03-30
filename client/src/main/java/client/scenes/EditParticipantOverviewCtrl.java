package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditParticipantOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private Event event;
    public ListView<String> participantList;
    public Button showButton;
    public ComboBox<Label> languageBox;
    public Button showButtonD;
    public Button showButtonE;
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
        participantList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (participantList.getSelectionModel().getSelectedItem() != null) {
                    showButton.setDisable(false);
                    showButtonD.setDisable(false);
                    showButtonE.setDisable(false);
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Label> x = FXCollections.observableArrayList();
        List<Config.SupportedLocale> languages = Config.get().getSupportedLocales().stream().toList();
        for (var item : languages) {
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
                        } else {
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
        //clearFields();
        controller.showOverview();
    }

    public Participant getParticipant() {
        String partIdName = participantList.getSelectionModel().getSelectedItem();
        String partId = partIdName.split(":")[0];
        return server.getParticipantByID(Long.parseLong(partId));
    }

//    public void showEvent() throws IOException {
//        String eventIdTitle = eventList.getSelectionModel().getSelectedItem();
//        String eventId = eventIdTitle.split(":")[0];
//        Event event = server.getByID(Long.parseLong(eventId));
//        controller.showEventOverview(event);
//    }

    //    public void deleteParticipant() {
//        try {
//            System.out.println("Delete Event");
//            server.deleteParticipant(getParticipant());
//            Main.reloadUIEvent();
//        } catch (WebApplicationException e) {
//
//            var alert = new Alert(Alert.AlertType.ERROR);
//            alert.initModality(Modality.APPLICATION_MODAL);
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
//        }
//    }
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                //update();
                break;
            case ESCAPE:
                //cancel();
                break;
            default:
                break;
        }
    }
}

