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
import java.net.URL;
import java.util.*;


public class StartScreenCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private ObservableList<String> data;
    private final SplittyCtrl eventCtrl;
    public ToggleButton highContrastButton;
    public AnchorPane background;
    public Button showAdminButton;
    public Button createButton;
    public Button joinButton;
    public Text eventCreate;
    public Text eventJoin;
    public Text languageChoice;
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
        this.eventCtrl = mainCtrl;
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
        if (data == null)
            data = FXCollections.observableArrayList();

        server.registerForAddUpdates(ev -> {

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    //refresh method loops in PastCodes
                    Config.get().addPastCode(ev.getInviteCode());
                    //System.out.println("adding event " + ev.getTitle() + " : " + ev.getInviteCode());
                    data.add(ev.getTitle() + " : " + ev.getInviteCode());
                    eventList.setItems(data);
                }
            });
        });
        server.registerForDeleteUpdates(ev -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Config.get().removePastCode(ev.getInviteCode());
                    //System.out.println("deleting event " + ev.getTitle() + " : " + ev.getInviteCode());
                    data.remove(ev.getTitle() + " : " + ev.getInviteCode());
                    eventList.setItems(data);
                }
            });
        });
        server.registerForEditUpdates(ev -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    int index=0;
                    for(String s : data){
                        if(s.contains(ev.getInviteCode())){
                            index = data.indexOf(s);
                            break;
                        }
                    }
                    data.set(index, ev.getTitle() + " : " + ev.getInviteCode());
                    eventList.setItems(data);
                }
            });
        });
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
                System.out.println("removed code " + removed);
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

    public void createEvent() {
        var title = this.titleField.getText();
        if (title.isEmpty()) {
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

        Event event = server.getByInviteCode(inviteCode);

        if (event != null) {
            clearFields();
            Set<String> codes = Config.get().getPastCodes();
            if (!codes.contains(event.getInviteCode())) {
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
