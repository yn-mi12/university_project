package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.*;
import java.util.List;


public class StartScreenCtrl implements Initializable {

    private final ServerUtilsEvent server;
    @FXML
    public Button setServers;
    private ObservableList<String> data;
    private final SplittyCtrl controller;
    @FXML
    public ToggleButton highContrastButton;
    @FXML
    public AnchorPane background;
    @FXML
    public Button showAdminButton;
    @FXML
    public Button createButton;
    @FXML
    public Button joinButton;
    @FXML
    public Text eventCreate;
    @FXML
    public Text eventJoin;
    @FXML
    public Text languageChoice;
    @FXML
    public Text recentEvents;
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
        this.controller = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Label> x = setLanguage();
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
                            if(Main.isContrastMode())this.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;" +
                                    "-fx-font-weight: bolder;-fx-border-color: #836FFF");
                            setGraphic(item);
                        }
                    }
                };
            }
        });
        String current = Config.get().getCurrentLocaleName();
        Image icon;
        String iconPath = "client/images/" + Config.get().getCurrentLocale() + ".png";
        icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(iconPath)));
        ImageView iconImageView = new ImageView(icon);
        iconImageView.setFitHeight(25);
        iconImageView.setPreserveRatio(true);
        Label l = new Label(current, iconImageView);
        l.setTextFill(Color.color(0, 0, 0));
        if(Main.isContrastMode())l.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
        languageBox.setValue(l);
        languageBox.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Config.get().setCurrentLocale(newVal.getText());
                Config.get().save();
                Main.reloadUI();
            }
        }));

        eventList.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (eventList.getSelectionModel().getSelectedItem() != null) {
                showButton.setDisable(false);
            }
        });
        Image highContrastIcon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("client/images/highContrast.png")));
        iconImageView = new ImageView(highContrastIcon);
        iconImageView.setFitHeight(25);
        iconImageView.setPreserveRatio(true);
        highContrastButton.setGraphic(new Label("", iconImageView));
        if(Main.isContrastMode())isContrast();
        else {
            highContrastButton = new ToggleButton();
        }
        server.registerForDeleteUpdates(ev -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (data == null)
                        data = FXCollections.observableArrayList();
                    Config.get().removePastCode(ev.getId());
                    data.remove(ev.getTitle() + " : " + ev.getId());
                    eventList.setItems(data);
                }
            });
        });
        server.registerForEditUpdates(ev -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (data != null) {
                        int index = 0;
                        for (String s : data) {
                            if (s.contains(ev.getId())) {
                                index = data.indexOf(s);
                                break;
                            }
                        }
                        data.set(index, ev.getTitle() + " : " + ev.getId());
                        eventList.setItems(data);
                    }
                }
            });
        });
    }

    private @NotNull ObservableList<Label> setLanguage() {
        ObservableList<Label> x = FXCollections.observableArrayList();
        List<Label> perm = new ArrayList<>();
        boolean ok = false;
        List<Config.SupportedLocale> languages = Config.get().getSupportedLocales().stream().toList();
        for (var item : languages) {
            Image icon;
            String iconPath = "client/images/" + item.getCode() + ".png";
            icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(iconPath)));
            ImageView iconImageView = new ImageView(icon);
            iconImageView.setFitHeight(25);
            iconImageView.setPreserveRatio(true);
            Label l = new Label(item.getName(), iconImageView);
            if(Main.isContrastMode())l.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
            if(ok) x.add(l);
            if(l.getText().equals(Config.get().getCurrentLocaleName()))ok = true;
        }
        ok = false;
        setLanguageHelper(languages, ok, x);
        return x;
    }

    private void setLanguageHelper(List<Config.SupportedLocale> languages, boolean ok, ObservableList<Label> x) {
        for (var item : languages) {
            Image icon;
            String iconPath = "client/images/" + item.getCode() + ".png";
            icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(iconPath)));
            ImageView iconImageView = new ImageView(icon);
            iconImageView.setFitHeight(25);
            iconImageView.setPreserveRatio(true);
            Label l = new Label(item.getName(), iconImageView);
            if(Main.isContrastMode())l.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
            if(ok) break;
            if(l.getText().equals(Config.get().getCurrentLocaleName())) ok = true;
            x.add(l);
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
                "-fx-color-label-visible: #F0F3FF");
        highContrastButton.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
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

            } else {
                highContrastButton.setStyle(Main.changeUI(highContrastButton));
                highContrastButton.setGraphic((new Label("", iconImageView)));
            }
        });
        Main.languageFeedback(languageBox);
        titleField.setStyle(Main.changeUI(titleField));
        codeField.setStyle(Main.changeUI(codeField));
        languageBox.setStyle(Main.changeUI(languageBox));
        eventCreate.setStyle(Main.changeUI(eventCreate));
        eventJoin.setStyle(Main.changeUI(eventJoin));
        recentEvents.setStyle(Main.changeUI(recentEvents));
        invalidCode.setStyle(Main.changeUI(invalidCode));
        emptyCode.setStyle(Main.changeUI(emptyCode));
        emptyTitle.setStyle(Main.changeUI(emptyTitle));
        languageChoice.setStyle(Main.changeUI(languageChoice));
        isAdmin.setStyle(Main.changeUI(isAdmin));
        setServers.setStyle(Main.changeUI(setServers));
        Main.buttonFeedback(setServers);
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
                Event e = server.getByID(code);
                if (e != null && !events.contains(e)) {
                    events.add(e);
                    titles.add(e.getTitle() + " : " + e.getId());
                } else {
                    removedCodes.add(code);
                }
            }

            for (String removed : removedCodes) {
                Config.get().removePastCode(removed);
            }

            Config.get().save();
            data = FXCollections.observableList(titles);
            eventList.setItems(data);
        }
    }

    private void clearFields() {
        titleField.clear();
        codeField.clear();
    }

    public void setServer() {
        controller.showSetServer();
    }

    public void createEvent() {
        var title = this.titleField.getText();
        if (title.isEmpty()) {
            emptyTitle.setVisible(true);
            return;
        }

        clearFields();
        Event event;
        try {
            event = new Event(title);
            server.send("/app/events", event);
            Config.get().addPastCode(String.valueOf(event.getId()));
            Config.get().save();
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        controller.showEventOverview(event);
    }

    public void stop() {
        server.stop();
    }


    public void viewEvent() {
        var inviteCode = this.codeField.getText();
        if (inviteCode.isEmpty()) {
            invalidCode.setVisible(false);
            emptyCode.setVisible(true);
            return;
        }

        Event event = server.getByID(inviteCode);

        if (event != null) {
            clearFields();
            Set<String> codes = Config.get().getPastCodes();
            if (!codes.contains(event.getId())) {
                Config.get().addPastCode(String.valueOf(event.getId()));
                Config.get().save();
            }
            controller.showEventOverview(event);
        } else {
            emptyCode.setVisible(false);
            invalidCode.setVisible(true);
        }

    }

    public void showEvent() {
        String eventTitleAndCode = eventList.getSelectionModel().getSelectedItem();
        String inviteCode = eventTitleAndCode.split(": ")[1];
        Event event = server.getByID(inviteCode);
        controller.showEventOverview(event);
    }

    public Event getEvent() {
        String eventTitleAndCode = eventList.getSelectionModel().getSelectedItem();
        String inviteCode = eventTitleAndCode.split(": ")[1];
        return server.getByID(inviteCode);
    }

    public void showAdminLogin() {
        controller.showAdminLogin();
    }

    public void changeContrast() {
        Main.changeContrast();
    }
}
