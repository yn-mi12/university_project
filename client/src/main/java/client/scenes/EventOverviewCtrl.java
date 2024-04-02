package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.ExpenseParticipant;
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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

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
    public boolean isAdmin = false;

    @FXML
    private ListView<String> allExpenses;
    @FXML
    private ListView<String> fromExpenses;
    @FXML
    private ListView<String> includingExpenses;


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

    public void sendInvites() {
        controller.showInvitePage(event);
    }

    public void editTitle(){
        controller.showEditTitle(event);
    }
    public void updateParticipant(){
        controller.initEditParticipantOverview(event);
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
        goBack();
    }

    public void copyCode() {
        ClipboardContent content = new ClipboardContent();
        content.putString(inviteCode.getText());
        Clipboard.getSystemClipboard().setContent(content);
    }

    public void goBack() {
        if(controller.getAdmin()) controller.showAdminOverview();
        else controller.showOverview();
    }

    public void expensesNotSelectedPart(){
        List<Expense> expenses = server.getExpensesByEventId(event);
        List<String> titles = new ArrayList<>();
        for (Expense expense : expenses){
            Participant owner = new Participant();
            Set<ExpenseParticipant> expenseParticipants = expense.getDebtors();
            for (ExpenseParticipant expenseParticipant : expenseParticipants){
                if (expenseParticipant.isOwner()){
                    owner = expenseParticipant.getParticipant();
                }
            }
            String expenseString = owner.getFirstName() + " paid " + expense.getAmount() + " for " + expense.getDescription();
            titles.add(expenseString);
        }
        allExpenses.setItems(FXCollections.observableList(titles));
    }

    public void expensesFromParticipant(){
        String participantsName = part.getText();
        Participant participant = event.getParticipantByName(participantsName);

        List<Expense> expenses = server.getExpensesByEventId(event);
        List<Expense> expensesFromParticipant = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for(Expense expense : expenses){
            List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
            for(int i = 0; i < debtors.size(); i++){
                if (debtors.get(i).isOwner() && debtors.get(i).getParticipant().equals(participant)){
                    expensesFromParticipant.add(expense);
                }
            }
        }
        for (Expense expense: expensesFromParticipant){
            String expenseString = participant.getFirstName() + " paid " + expense.getAmount() + " for " + expense.getDescription();
            titles.add(expenseString);
        }
        fromExpenses.setItems(FXCollections.observableList(titles));
    }

    public void expensesIncludingParticipant(){
        String participantsName = part.getText();
        Participant participant = event.getParticipantByName(participantsName);

        List<Expense> expenses = server.getExpensesByEventId(event);
        List<Expense> expensesIncludingParticipant = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for(Expense expense : expenses){
            List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
            for(int i = 0; i < debtors.size(); i++){
                if (debtors.get(i).getParticipant().equals(participant)){
                    expensesIncludingParticipant.add(expense);
                }
            }
        }

        Participant owner = new Participant();
        for (Expense expense: expensesIncludingParticipant){
            List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
            for(ExpenseParticipant expenseParticipant : debtors){
                if (expenseParticipant.isOwner()){
                    owner = expenseParticipant.getParticipant();
                }
            }
            String expenseString = owner.getFirstName() + " paid " + expense.getAmount() + " for " + expense.getDescription();
            titles.add(expenseString);
        }
        includingExpenses.setItems(FXCollections.observableList(titles));
    }


}
