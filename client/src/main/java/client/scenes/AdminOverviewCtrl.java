package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class AdminOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    @FXML
    public TableView<Event> eventList;
    @FXML
    public AnchorPane background;
    @FXML
    public Button backButton;
    @FXML
    public Button importButton;
    @FXML
    public Label adminOverviewLabel;
    @FXML
    public Label eventsLabel;
    @FXML
    TableColumn<Event, String> eventIDColumn;
    @FXML
    TableColumn<Event, String> eventTitleColumn;
    @FXML
    TableColumn<Event, LocalDateTime> eventCreationDateColumn;
    @FXML
    TableColumn<Event, LocalDateTime> eventLastUpdateDateColumn;
    @FXML
    public Button showButton;
    @FXML
    public ComboBox<Label> languageBox;
    @FXML
    public Button showButtonD;
    @FXML
    public Button showButtonE;
    private Stage primaryStage;
    private ObservableList<Event> data;

    @Inject
    public AdminOverviewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formatTable();
        refresh();

        eventList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observableValue, Event event, Event t1) {
                if (eventList.getSelectionModel().getSelectedItem() != null) {
                    showButton.setDisable(false);
                    showButtonD.setDisable(false);
                    showButtonE.setDisable(false);
                }
            }
        });
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
        l.setStyle("-fx-text-fill: black");
        if(Main.isContrastMode())l.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
        languageBox.setValue(l);
        languageBox.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Config.get().setCurrentLocale(newVal.getText());
                Config.get().save();
                Main.refreshAdminOverview();
            }
        }));

        if(Main.isContrastMode()){
            Main.languageFeedback(languageBox);
            languageBox.setStyle(Main.changeUI(languageBox));
            adminOverviewLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            eventsLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            background.setStyle("-fx-background-color: #69e0ab;");
            backButton.setStyle(Main.changeUI(backButton));
            Main.buttonFeedback(backButton);
            showButton.setStyle(Main.changeUI(showButton));
            Main.buttonFeedback(showButton);
            showButtonE.setStyle(Main.changeUI(showButtonE));
            Main.buttonFeedback(showButtonE);
            importButton.setStyle(Main.changeUI(importButton));
            Main.buttonFeedback(importButton);
            eventList.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                    "-fx-border-width: 2.5; -fx-border-insets: 2;-fx-control-inner-background:#836FFF");
            showButtonD.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;-fx-font-weight: bolder;"+
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                    "-fx-border-width: 1.5; -fx-border-insets: -1");
            showButtonD.setOnMouseEntered(e -> showButtonD.setStyle("-fx-background-color: #c70000; " +
                    "-fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1;"));
            showButtonD.setOnMouseExited(e ->         showButtonD.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;" +
                    "-fx-font-weight: bolder;"+
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                    "-fx-border-width: 1.5; -fx-border-insets: -1"));
            showButtonD.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    showButtonD.setStyle("-fx-background-color: #c70000; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                            "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                            "-fx-border-width: 1.5; -fx-border-insets: -1;");
                }
                else         showButtonD.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;-fx-font-weight: bolder;"+
                        "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                        "-fx-border-width: 1.5; -fx-border-insets: -1");
            });
        }
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

    public void launch(){
        server.registerForMessages("/topic/titles", Event.class , q -> {
            for(var x: data)
            {
                if(Objects.equals(x.getId(), q.getId())){
                    data.remove(x);
                    break;
                }
            }
            data.add(q);
            eventList.refresh();
        });
        server.registerForMessages("/topic/events", Event.class , q -> {
            data.add(q);
            eventList.refresh();
        });
        server.registerForMessages("/topic/deleted", Event.class , q -> {
            for(var x: data)
            {
                if(Objects.equals(x.getId(), q.getId())){
                    data.remove(x);
                    break;
                }
            }
            eventList.refresh();
        });
        server.registerForMessages("/topic/updated", Event.class , q -> {
            for(var x: data)
            {
                if(Objects.equals(x.getId(), q.getId())){
                    data.remove(x);
                    break;
                }
            }
            data.add(q);
            eventList.refresh();
        });
    }

    public void refresh() {
        List<Event> events = server.getAllEvents();
        if(events.isEmpty()) {
            showButton.setDisable(true);
            showButtonD.setDisable(true);
            showButtonE.setDisable(true);
        }
        data = FXCollections.observableList(new ArrayList<>(events));
        eventList.setItems(data);
    }

    public void goBack() {
        controller.setAdmin(false);
        controller.showOverview();
    }

    public Event getEvent() {
        return server.getByID(eventList.getSelectionModel().getSelectedItem().getId());
    }

    public void showEvent() {
        Event event = server.getByID(eventList.getSelectionModel().getSelectedItem().getId());
        controller.showEventOverview(event);
    }
    public void deleteEvent() {
        try {
            Event event = getEvent();
            event.setId(server.getByID(event.getId()).getId());
            server.send("/app/deleted", event);
            refresh();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void exportEvent() {
        var om = new ObjectMapper();

        Event event = server.getByID(eventList.getSelectionModel().getSelectedItem().getId());

        try {
            var jsonEvent = om.writeValueAsString(event);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Event as JSON");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
            File file = fileChooser.showSaveDialog(primaryStage);

            if(file != null) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(jsonEvent);
                fileWriter.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importEvent() {
        var om = new ObjectMapper();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JSON File to Import");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        File file = fileChooser.showOpenDialog(primaryStage);

        if(file != null) {
            try {
                Scanner jsonScanner = new Scanner(file);
                jsonScanner.useDelimiter("\r?\n");
                String json = jsonScanner.next();
                var event = om.readValue(json, Event.class);

                List<Participant> participants = event.getParticipants();;
                List<Expense> expenses = event.getExpenses();
                List<Debt> debts = event.getDebts();
                event.setParticipants(null);
                event.setExpenses(null);
                event.setDebts(null);
                Event find = server.getByID(event.getId());

                if(find == null) {
                    addJsonToServer(event, participants, expenses, debts);
                } else {
                    server.deleteEvent(find);
                    addJsonToServer(event, participants, expenses, debts);
                }
                // TODO tags when we have a proper system for those
                refresh();
            } catch (FileNotFoundException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addJsonToServer(Event event, List<Participant> participants, List<Expense> expenses, List<Debt> debts) {
        Event saved = server.addJsonEvent(event);
        for(Participant p : participants)
            server.addParticipant(p, saved);

        saved = server.getByID(saved.getId());
        List<Participant> newParts = saved.getParticipants();
        Map<Long, Participant> idToNewPart = new HashMap<>();
        for(int i = 0; i < participants.size(); i++)
            idToNewPart.put(participants.get(i).getId(), newParts.get(i));

        Set<ExpenseParticipant> debtors = new HashSet<>();
        int count = 0;
        for(Expense e : expenses) {
            Expense newExpense = new Expense(e.getDescription(), e.getCurrency(), e.getAmount(), e.getDate());
            for(ExpenseParticipant ep : e.getDebtors()) {
                ExpenseParticipant newExpensePart = new
                        ExpenseParticipant(newExpense, newParts.get(count), ep.getShare(), ep.isOwner());
                debtors.add(newExpensePart);
                count++;
            }
            e.setDebtors(debtors);
            e.setEvent(saved);
            server.addExpense(e, saved);

        }

        saved = server.getByID(saved.getId());
        List<Debt> newDebts = new ArrayList<>();
        for(Debt d : debts) {
            Debt newDebt = new Debt(idToNewPart.get(d.getDebtor().getId()),
                    idToNewPart.get(d.getCreditor().getId()), d.getAmount());
            newDebts.add(newDebt);
        }
        server.addAllDebts(newDebts, saved);
    }

    private void formatTable() {
        // Sets all columns not reorder-able
        eventList.getColumns().forEach(e -> e.setReorderable(false));
        // sets title column to fill remaining space
        double w = eventIDColumn.widthProperty().get() +
                eventCreationDateColumn.widthProperty().get() +
                eventLastUpdateDateColumn.widthProperty().get();
        eventTitleColumn.prefWidthProperty().bind(eventList.widthProperty().subtract(w));
    }
}
