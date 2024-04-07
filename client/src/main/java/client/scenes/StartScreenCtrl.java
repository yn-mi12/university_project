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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Callback;
import java.net.URL;
import java.util.*;
import java.util.List;


public class StartScreenCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl eventCtrl;
    public ToggleButton highContrastButton;
    public AnchorPane background;
    public Button showAdminButton;
    public Button createButton;
    public Button joinButton;
    public Text event_create;
    public Text event_join;
    public Text language_choice;
    public Text recent_events;
    public Text isAdmin;
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
            Label l = new Label(item.getName(), iconImageView);
            if(Main.isContrastMode())l.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
            x.add(l);
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
                            if(Main.isContrastMode())this.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;" +
                                    "-fx-font-weight: bolder;-fx-border-color: #836FFF");
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
        Image highContrastIcon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("client/images/highContrast.png")));
        ImageView iconImageView = new ImageView(highContrastIcon);
        iconImageView.setFitHeight(25);
        iconImageView.setPreserveRatio(true);
        highContrastButton.setGraphic(new Label("", iconImageView));
        if(Main.isContrastMode())isContrast();
        else {
            highContrastButton = new ToggleButton();
        }
    }

    private void isContrast() {
        ImageView iconImageView;
        Image highContrastIcon;
        background.setStyle("-fx-background-color: #69e0ab;");
        joinButton.setStyle(Main.changeUI(joinButton));
        Main.buttonFeedback(joinButton);
        createButton.setStyle(Main.changeUI(createButton));
        Main.buttonFeedback(createButton);
        showAdminButton.setStyle(Main.changeUI(showAdminButton));
        Main.buttonFeedback(showAdminButton);
        showButton.setStyle(Main.changeUI(showButton));
        Main.buttonFeedback(showButton);
        eventList.setStyle("-fx-background-color: #836FFF;-fx-font-weight: bolder; " +
                "-fx-border-color: #211951; -fx-control-inner-background: #836FFF; " +
                "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 15%);" +
                "-fx-text-fill: #F0F3FF; -fx-color-label-visible: #F0F3FF");
        highContrastButton.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 2.5; -fx-border-insets: -2");
        highContrastIcon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("client/images/highContrastInvert.png")));
        iconImageView = new ImageView(highContrastIcon);
        iconImageView.setFitHeight(25);
        iconImageView.setPreserveRatio(true);
        highContrastButton.setGraphic(new Label("", iconImageView));
        highContrastButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                highContrastButton.setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                        "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 2.5; -fx-border-insets: -2");
                highContrastButton.getGraphic().setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                        "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 2.5; -fx-border-insets: -2");

            }
            else {
                highContrastButton.setStyle(Main.changeUI(highContrastButton));
                highContrastButton.setGraphic((new Label("", iconImageView)));
            }
        });
        Main.languageFeedback(languageBox);
        titleField.setStyle(Main.changeUI(titleField));
        codeField.setStyle(Main.changeUI(codeField));
        languageBox.setStyle(Main.changeUI(languageBox));
        event_create.setStyle(Main.changeUI(event_create));
        event_join.setStyle(Main.changeUI(event_join));
        recent_events.setStyle(Main.changeUI(recent_events));
        invalidCode.setStyle(Main.changeUI(invalidCode));
        emptyCode.setStyle(Main.changeUI(emptyCode));
        emptyTitle.setStyle(Main.changeUI(emptyTitle));
        language_choice.setStyle(Main.changeUI(language_choice));
        isAdmin.setStyle(Main.changeUI(isAdmin));
    }

    public void refresh() {
        emptyTitle.setVisible(false);
        emptyCode.setVisible(false);
        invalidCode.setVisible(false);
        showButton.setDisable(true);
        Set<String> codes = Config.get().getPastCodes();

        if (!codes.isEmpty()) {

            List<Event> events = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            List<String> removedCodes = new ArrayList<>();

            for (String code : codes) {
                Event e = server.getByInviteCode(code);
                if (e != null) {
                    events.add(e);
                    titles.add(e.getTitle() + " : " + e.getInviteCode());
                } else {
                    removedCodes.add(code);
                }
            }

            for (String removed : removedCodes) {
                Config.get().removePastCode(removed);
            }

            Config.get().save();
            eventList.setItems(FXCollections.observableList(titles));
        }
    }

    private void clearFields() {
        titleField.clear();
        codeField.clear();
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
            event = new Event(title);
            server.send("/app/events", event);
            Config.get().addPastCode(String.valueOf(event.getInviteCode()));
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
            Set<String> codes = Config.get().getPastCodes();
            if(!codes.contains(event.getInviteCode())) {
                Config.get().addPastCode(String.valueOf(event.getInviteCode()));
                Config.get().save();
            }
            eventCtrl.showEventOverview(event);
        } else {
            emptyCode.setVisible(false);
            invalidCode.setVisible(true);
        }

    }

    public void showEvent() {
        String eventTitleAndCode = eventList.getSelectionModel().getSelectedItem();
        String inviteCode = eventTitleAndCode.split(": ")[1];
        Event event = server.getByInviteCode(inviteCode);
        eventCtrl.showEventOverview(event);
    }

    public Event getEvent() {
        String eventTitleAndCode = eventList.getSelectionModel().getSelectedItem();
        String inviteCode = eventTitleAndCode.split(": ")[1];
        return server.getByInviteCode(inviteCode);
    }

    public void showAdminLogin() {
        eventCtrl.showAdminLogin();
    }

    public void changeContrast() {
        Main.changeContrast();
    }
}
