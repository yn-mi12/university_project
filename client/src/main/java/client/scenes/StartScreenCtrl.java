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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class StartScreenCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl eventCtrl;
    @FXML
    private ListView<String> eventList;
    @FXML
    private ComboBox<Label> languageBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextField codeField;
    @FXML
    private Button showButton;
    @FXML
    private Label invalidCode;
    @FXML
    private Label emptyCode;
    @FXML
    private Label emptyTitle;

    @Inject
    public StartScreenCtrl(ServerUtilsEvent server, SplittyCtrl mainCtrl) {
        this.server = server;
        this.eventCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                Main.reloadUI();
            }
        }));

        eventList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (eventList.getSelectionModel().getSelectedItem() != null) {
                    showButton.setDisable(false);
                }
            }
        });
    }

    public void createEvent() {
        var title = this.titleField.getText();
        if(title.isEmpty()) {
            emptyTitle.setVisible(true);
            return;
        }

        clearFields();
        Event event;
        try {
            System.out.println("Add event");
            event = server.addEvent(new Event(title));
            Config.get().addPastID(String.valueOf(event.getId()));
            Config.get().save();
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        eventCtrl.showEventOverview(event);
    }

    public void viewEvent() {
        var inviteCode = this.codeField.getText();
        if(inviteCode.isEmpty()) {
            invalidCode.setVisible(false);
            emptyCode.setVisible(true);
            return;
        }

        Event event = server.getByInviteCode(inviteCode);

        if(event != null) {
            clearFields();
            Set<String> ids = Config.get().getPastIDs();
            if(!ids.contains(event.getId())) {
                Config.get().addPastID(String.valueOf(event.getId()));
                Config.get().save();
            }
            eventCtrl.showEventOverview(event);
        } else {
            emptyCode.setVisible(false);
            invalidCode.setVisible(true);
        }

    }

    public void refresh() {
        emptyTitle.setVisible(false);
        emptyCode.setVisible(false);
        invalidCode.setVisible(false);
        showButton.setDisable(true);
        Set<String> ids = Config.get().getPastIDs();

        if (!ids.isEmpty()) {

            List<Event> events = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            List<String> removedIDs = new ArrayList<>();

            for (String id : ids) {
                Event e = server.getByID(Long.valueOf(id));
                if (e != null) {
                    events.add(e);
                    titles.add(e.getId() + ": " + e.getTitle());
                } else {
                    removedIDs.add(id);
                }
            }

            // Removes the ids that do not correspond to an event in the database
            for (String removed : removedIDs) {
                Config.get().removePastID(removed);
            }

            Config.get().save();
            eventList.setItems(FXCollections.observableList(titles));
        }
    }


    private void clearFields() {
        titleField.clear();
        codeField.clear();
    }

    public void showEvent() throws IOException {
        String eventIdTitle = eventList.getSelectionModel().getSelectedItem();
        String eventId = eventIdTitle.split(":")[0];
        Event event = server.getByID(Long.parseLong(eventId));
        eventCtrl.showEventOverview(event);
    }

    public Event getEvent() {
        String eventIdTitle = eventList.getSelectionModel().getSelectedItem();
        String eventId = eventIdTitle.split(":")[0];
        return server.getByID(Long.parseLong(eventId));
    }

    public void showAdminLogin() {
        eventCtrl.showAdminLogin();
    }
}
