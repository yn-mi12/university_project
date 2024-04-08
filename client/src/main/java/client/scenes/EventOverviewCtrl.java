package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class EventOverviewCtrl implements Initializable {
    private final ServerUtilsEvent server;
    public Label inviteCode;
    public Label paidLabel;
    public Label participantsLabel;
    public Label forLabel;
    private Participant expensePayer;
    private final SplittyCtrl controller;
    private final AddExpenseCtrl expenseCtrl;
    private List<Participant> participants;
    @FXML
    private TextArea participantText = new TextArea();
    @FXML
    private SplitMenuButton part;
    @FXML
    public Label eventTitle;
    @FXML
    private ComboBox<Label> languageBox;
    @FXML
    private Label totalCost;
    private Event event;
    public boolean isAdmin = false;

    @FXML
    private ListView<String> allExpenses;
    @FXML
    private ListView<String> fromExpenses;
    @FXML
    private ListView<String> includingExpenses;

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab fromTab;
    @FXML
    private Tab includingTab;
    @FXML
    private Button viewButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    private String viewChoice;

    @Inject
    public EventOverviewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl, AddExpenseCtrl expenseCtrl) {
        this.server = server;
        this.controller = eventCtrl;
        this.expenseCtrl = expenseCtrl;
    }

    public Event getSelectedEvent() {
        return event;
    }

    public void setSelectedEvent(Event selectedEvent) {
        hideTabPanes();
        this.event = selectedEvent;
        this.participants = event.getParticipants();
        ObservableList<MenuItem> names = FXCollections.observableArrayList();
        StringBuilder namesString = new StringBuilder();
        HashMap<MenuItem, Participant> map = new HashMap<>();
        int i = 0;
        for (Participant p : participants) {
            MenuItem item = new MenuItem(p.getFirstName() + " " + p.getLastName());
            names.add(item);
            map.put(item, p);
            namesString.append(p.getFirstName() + " " + p.getLastName());
            if (i < participants.size() - 1)
                namesString.append(", ");
            i++;
        }
        part.setText(participantsLabel.getText());
        part.getItems().setAll(names);
        participantText.setEditable(false);
        participantText.setText(namesString.toString());
        for (MenuItem mi : part.getItems()) {
            mi.setOnAction(e -> {
                part.setText(mi.getText());
                expensePayer = map.get(mi);
                showTabPanes();
                expensesFromParticipant();
                expensesIncludingParticipant();
            });
        }
        inviteCode.setText(event.getId());
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

        allExpenses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (allExpenses.getSelectionModel().getSelectedItem() != null) {
                    viewButton.setDisable(false);
                    deleteButton.setDisable(false);
                    viewChoice = "all";
                }
            }
        });

        fromExpenses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (fromExpenses.getSelectionModel().getSelectedItem() != null) {
                    viewButton.setDisable(false);
                    deleteButton.setDisable(false);
                    viewChoice = "from";
                }
            }
        });

        includingExpenses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (includingExpenses.getSelectionModel().getSelectedItem() != null) {
                    viewButton.setDisable(false);
                    deleteButton.setDisable(false);
                    viewChoice = "include";
                }
            }
        });
    }

    public void addExpense() {
        controller.initExpShowOverview(event, expensePayer);
    }

    public Expense getExpense() {
        long id = 0;
        switch(viewChoice) {
            case "all" -> {
                id = Long.valueOf(allExpenses.getSelectionModel().getSelectedItem().split(":")[0]);
                allExpenses.getSelectionModel().clearSelection();
            }
            case "from" -> {
                id = Long.valueOf(fromExpenses.getSelectionModel().getSelectedItem().split(":")[0]);
                fromExpenses.getSelectionModel().clearSelection();
            }
            case "include" -> {
                id = Long.valueOf(includingExpenses.getSelectionModel().getSelectedItem().split(":")[0]);
                includingExpenses.getSelectionModel().clearSelection();
            }
        }
        return server.getExpenseById(id);
    }

    public void viewExpense() {
        viewButton.setDisable(true);
        deleteButton.setDisable(true);
        Expense selected = getExpense();

        Participant owner = null;
        for(ExpenseParticipant ep : selected.getDebtors()) {
            if(ep.isOwner()) {
                owner = ep.getParticipant();
                break;
            }
        }
        controller.initExpShowOverview(event, owner);
        expenseCtrl.setWhatForText(selected.getDescription());
        expenseCtrl.setHowMuchText(String.valueOf(selected.getAmount()));
        expenseCtrl.setCurrencyText(selected.getCurrency());
        expenseCtrl.setDateText(selected.getDate().toLocalDate());
        expenseCtrl.setOldExpense(selected);

        expenseCtrl.getWhoPays().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        int count = 0;
        for(ExpenseParticipant ep : selected.getDebtors()) {
            if(ep.getShare() != 0) {
                Participant p = ep.getParticipant();
                expenseCtrl.getWhoPays().getSelectionModel().select(p.getFirstName() + " " + p.getLastName());
                count++;
            }
        }
        if(count == participants.size()) {
            expenseCtrl.getWhoPays().getSelectionModel().clearSelection();
            expenseCtrl.getAllHaveToPay().setSelected(true);
        } else {
            expenseCtrl.getSomeHaveToPay().setSelected(true);
        }
    }

    public void deleteExpense() {
        deleteButton.setDisable(true);
        viewButton.setDisable(true);
        expenseCtrl.setDelete(true);
        Expense selected = getExpense();

        Participant owner = null;
        for(ExpenseParticipant ep : selected.getDebtors()) {
            if(ep.isOwner())
                owner = ep.getParticipant();
        }
        expenseCtrl.setEvent(owner, this);
        expenseCtrl.setOldExpense(selected);
        expenseCtrl.ok();

        server.deleteExpense(selected);
        event = server.getByID(event.getId());

        expensesNotSelectedPart();
        expensesIncludingParticipant();
        expensesNotSelectedPart();
    }

    public void settleDebts() {
        List<Debt> allDebts = server.getDebtsByEvent(event);
        controller.showSettleDebts(allDebts, event);
    }

    public void sendInvites() {
        controller.showInvitePage(event);
    }

    public void editTitle() {
        controller.showEditTitle(event);
    }

    public void updateParticipant() {
        controller.initEditParticipantOverview(event);
    }

    public void addParticipant() {
        controller.showAddParticipant(event);
    }

    public void deleteEvent() {
        try {
            System.out.println("Delete Event");
            event.setId(server.getByID(event.getId()).getId());
            server.send("/app/deleted", event);
            if(!controller.getAdmin()) {
                Config.get().removePastCode(event.getId());
                Config.get().save();
            }
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
        viewButton.setDisable(true);
        deleteButton.setDisable(true);
        part.setText("Participants");
        if (controller.getAdmin()) controller.showAdminOverview();
        else controller.showOverview();
    }

    public void expensesNotSelectedPart() {
        List<Expense> expenses = server.getExpensesByEventId(event);
        List<String> titles = new ArrayList<>();
        double totalAmount = 0;

        if (expenses != null) {
            for (Expense expense : expenses) {
                Participant owner = new Participant();
                Set<ExpenseParticipant> expenseParticipants = expense.getDebtors();
                for (ExpenseParticipant expenseParticipant : expenseParticipants) {
                    if (expenseParticipant.isOwner()) {
                        owner = expenseParticipant.getParticipant();
                    }
                }
                String expenseString = expense.getId() + ": " + owner.getFirstName() + " " + owner.getLastName() + " "
                        + paidLabel.getText() + " " + expense.getAmount() + " " + forLabel.getText() + " " + expense.getDescription();
                titles.add(expenseString);
                totalAmount += expense.getAmount();
            }
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String text = totalCost.getText().replaceAll("[0-9]", "").replace(".", "");
        if (text.charAt(text.length() - 1) == ' ')
            totalCost.setText(text + df.format(totalAmount));
        else totalCost.setText(text + " " + df.format(totalAmount));

        allExpenses.setItems(FXCollections.observableList(titles));
    }

    public void expensesFromParticipant() {
        String participantsName = part.getText();
        Participant participant = event.getParticipantByName(participantsName);
        List<Expense> expenses = server.getExpensesByEventId(event);
        List<Expense> expensesFromParticipant = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        if (expenses != null) {
            for (Expense expense : expenses) {
                List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
                for (int i = 0; i < debtors.size(); i++) {
                    if (debtors.get(i).isOwner() && debtors.get(i).getParticipant().equals(participant)) {
                        expensesFromParticipant.add(expense);
                    }
                }
            }
            for (Expense expense : expensesFromParticipant) {
                String expenseString = expense.getId() + ": " +participant.getFirstName() + " " + participant.getLastName() +
                        " " + paidLabel.getText() + " " + expense.getAmount() + " " + forLabel.getText() + " " + expense.getDescription();
                titles.add(expenseString);
            }
        }
        fromExpenses.setItems(FXCollections.observableList(titles));
    }

    public void expensesIncludingParticipant() {
        String participantsName = part.getText();
        Participant participant = event.getParticipantByName(participantsName);
        List<Expense> expenses = server.getExpensesByEventId(event);
        List<Expense> expensesIncludingParticipant = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        if (expenses != null) {
            for (Expense expense : expenses) {
                List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
                for (int i = 0; i < debtors.size(); i++) {
                    if (debtors.get(i).getParticipant().equals(participant)) {
                        expensesIncludingParticipant.add(expense);
                    }
                }
            }

            Participant owner = new Participant();
            for (Expense expense : expensesIncludingParticipant) {
                List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
                for (ExpenseParticipant expenseParticipant : debtors) {
                    if (expenseParticipant.isOwner()) {
                        owner = expenseParticipant.getParticipant();
                    }
                }
                String expenseString = expense.getId() + ": " + owner.getFirstName() +  " " + owner.getLastName() +  " "
                        + paidLabel.getText() + " " + expense.getAmount() + " " + forLabel.getText() + " " + expense.getDescription();
                titles.add(expenseString);
            }
        }
        includingExpenses.setItems(FXCollections.observableList(titles));
    }

    public void hideTabPanes() {
        tabPane.getTabs().remove(fromTab);
        tabPane.getTabs().remove(includingTab);
    }

    public void showTabPanes() {
        tabPane.getTabs().add(fromTab);
        tabPane.getTabs().add(includingTab);
    }
}
