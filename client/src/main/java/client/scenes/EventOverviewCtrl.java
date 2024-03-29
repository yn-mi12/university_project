package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import jakarta.ws.rs.WebApplicationException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static jakarta.ws.rs.core.Response.ok;

public class EventOverviewCtrl implements Initializable {
    private final ServerUtilsEvent server;
    public Label inviteCode;
    private Participant expensePayer;
    private final SplittyCtrl controller;
    private List<Participant> participants;
    @FXML
    private TextArea participantText = new TextArea();
    @FXML
    private SplitMenuButton part;
    @FXML
    public Label eventTitle;
    @FXML
    private ComboBox<Label> languageBox;
    public Event event;


    @Inject
    public EventOverviewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }

    public Event getSelectedEvent() {
        return event;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.event = selectedEvent;
        this.participants = event.getParticipants();
        ObservableList<MenuItem> names = FXCollections.observableArrayList();
        StringBuilder namesString = new StringBuilder();
        HashMap<MenuItem,Participant> map = new HashMap<>();
        int i = 0;
        for (Participant p : participants) {
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
        inviteCode.setText(event.getInviteCode());
    }

    @SuppressWarnings("java.lang.ClassCastException")
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

    public void addParticipant(){
        controller.showAddParticipant(event);
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
        controller.showOverview();
    }

    public void copyCode() {
        ClipboardContent content = new ClipboardContent();
        content.putString(inviteCode.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }
}
