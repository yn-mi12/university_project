package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import commons.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.WebApplicationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AdminOverviewCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    @FXML
    public TableView<Event> eventList;
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

    public void launch(){
        server.registerForMessages("/topic/events", Event.class , q -> {
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
        eventList.setItems(FXCollections.observableList(events));
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
            System.out.println("Delete Event");
            server.deleteEvent(getEvent());
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
            System.out.println(jsonEvent);
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
                System.out.println("Imported event: " + event);

                List<Participant> participants = event.getParticipants();;
                List<Expense> expenses = event.getExpenses();
                List<Tag> tags = event.getTags();
                List<Debt> debts = event.getDebts();
                event.setParticipants(null);
                event.setExpenses(null);
                event.setTags(null);
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
            }
            e.setDebtors(debtors);
            e.setEvent(saved);
            server.addExpense(e, saved);
            count++;
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
        //Custom error message
        eventList.setPlaceholder(new Label("No events currently in the server"));
        // Sets all columns not reorder-able
        eventList.getColumns().forEach(e -> e.setReorderable(false));
        // sets title column to fill remaining space
        double w = eventIDColumn.widthProperty().get() +
                eventCreationDateColumn.widthProperty().get() +
                eventLastUpdateDateColumn.widthProperty().get();
        eventTitleColumn.prefWidthProperty().bind(eventList.widthProperty().subtract(w));
    }
}
