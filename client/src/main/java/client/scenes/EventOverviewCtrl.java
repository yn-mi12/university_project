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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class EventOverviewCtrl implements Initializable {
    private final ServerUtilsEvent server;
    @FXML
    public Label inviteCode;
    @FXML
    public Label paidLabel;
    @FXML
    public Label participantsLabel;
    @FXML
    public Label forLabel;
    @FXML
    public AnchorPane background;
    @FXML
    public Button backButton;
    @FXML
    public Button editTitleButton;
    @FXML
    public Button copyCodeButton;
    @FXML
    public Button editPartButton;
    @FXML
    public Button addPartButton;
    @FXML
    public Button addExpenseButton;
    @FXML
    public Button settleDebtsButton;
    @FXML
    public Button deleteEventButton;
    @FXML
    public Text languageChoice;
    @FXML
    public Label inviteCodeLabel;
    @FXML
    public Label expenseLabel;
    @FXML
    public Label participantsLabel2;

    private Participant expensePayer;

    private final SplittyCtrl controller;
    private final AddExpenseCtrl expenseCtrl;
    private List<Participant> participants;
    @FXML
    private TextArea participantText;
    @FXML
    private ComboBox<Label> part;
    @FXML
    public Label eventTitle;
    @FXML
    private ComboBox<Label> languageBox;
    @FXML
    private Label totalCost;
    @FXML
    private Label editPencil;
    @FXML
    private Label trash;
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
//        this.event = server.getByID(selectedEvent.getId());
        this.participants = event.getParticipants();
        ObservableList<Label> names = FXCollections.observableArrayList();
        StringBuilder namesString = new StringBuilder();
        HashMap<Label, Participant> map = new HashMap<>();
        int i = 0;
        for (Participant p : participants) {
            Label item = new Label(p.getFirstName() + " " + p.getLastName());
            item.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
            names.add(item);
            map.put(item, p);
            namesString.append(p.getFirstName() + " " + p.getLastName());
            if (i < participants.size() - 1)
                namesString.append(", ");
            i++;
        }
        part.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                newVal.setStyle("-fx-text-fill: #000000");
                if(Main.isContrastMode())newVal.setStyle("-fx-text-fill: #F0F3FF");
                part.setValue(newVal);
                expensePayer = map.get(newVal);
                showTabPanes();
                expensesFromParticipant();
                expensesIncludingParticipant();
            }
        }));
        part.getItems().setAll(names);
        participantText.setEditable(false);
        participantText.setText(namesString.toString());
        inviteCode.setText(event.getId());
    }

    @SuppressWarnings("java.lang.ClassCastException")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label t = new Label(participantsLabel.getText());
        t.setStyle("-fx-text-fill: black");
        if(Main.isContrastMode()) t.setStyle("-fx-text-fill:  #F0F3FF");
        part.setValue(t);
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
        l.setStyle("-fx-text-fill: black");
        if(Main.isContrastMode())l.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
        languageBox.setValue(l);
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

        if(Main.isContrastMode())isContrast();
        part.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Label> call(ListView<Label> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Label item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                        } else {
                            setItem(item);
                            if(Main.isContrastMode())this.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;" +
                                    "-fx-font-weight: bolder;-fx-border-color: #836FFF");
                            setText(item.getText());
                        }
                    }
                };
            }
        });
    }

    private void isContrast() {
        background.setStyle("-fx-background-color: #69e0ab;");
        Main.languageFeedback(languageBox);
        languageBox.setStyle(Main.changeUI(languageBox));
        backButton.setStyle(Main.changeUI(backButton));
        Main.buttonFeedback(backButton);
        addExpenseButton.setStyle(Main.changeUI(addExpenseButton));
        Main.buttonFeedback(addExpenseButton);
        deleteEventButton.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;-fx-font-weight: bolder;"+
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                "-fx-border-width: 1.5; -fx-border-insets: -1");
        deleteEventButton.setOnMouseEntered(e -> deleteEventButton.setStyle("-fx-background-color: #c70000; " +
                "-fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1;"));
        deleteEventButton.setOnMouseExited(e ->         deleteEventButton.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;" +
                "-fx-font-weight: bolder;"+
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                "-fx-border-width: 1.5; -fx-border-insets: -1"));
        deleteEventButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deleteEventButton.setStyle("-fx-background-color: #c70000; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                        "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1;");
            }
            else         deleteEventButton.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;-fx-font-weight: bolder;"+
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                    "-fx-border-width: 1.5; -fx-border-insets: -1");
        });
        addExpenseButton.setStyle(Main.changeUI(addExpenseButton));
        Main.buttonFeedback(addExpenseButton);
        viewButton.setStyle(Main.changeUI(viewButton));
        Main.buttonFeedback(viewButton);
        deleteButton.setStyle(Main.changeUI(deleteButton));
        Main.buttonFeedback(deleteButton);
        copyCodeButton.setStyle(Main.changeUI(copyCodeButton));
        Main.buttonFeedback(copyCodeButton);
        editTitleButton.setStyle(Main.changeUI(editTitleButton));
        Main.buttonFeedback(editTitleButton);
        settleDebtsButton.setStyle(Main.changeUI(settleDebtsButton));
        Main.buttonFeedback(settleDebtsButton);
        part.setStyle(Main.changeUI(part));
        Main.languageFeedback(part);
        editPartButton.setStyle(Main.changeUI(editPartButton));
        Main.buttonFeedback(editPartButton);
        addPartButton.setStyle(Main.changeUI(addPartButton));
        Main.buttonFeedback(addPartButton);
        participantText.setStyle(Main.changeUI(participantText));
        trash.setStyle(Main.changeUI(trash));
        editPencil.setStyle("-fx-text-fill: white;-fx-font-weight: bolder;");
        tabPane.setStyle("-fx-control-inner-background:#836FFF;-fx-font-weight: bolder; " +
                "-fx-border-color: black;-fx-border-width: 3;-fx-border-radius: 5;");
        inviteCodeLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
        languageChoice.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
        totalCost.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
        eventTitle.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
        participantsLabel2.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
        expenseLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
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
        expenseCtrl.setOldExpensePayer(owner);
        expenseCtrl.setExpensePayer(owner);
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
        expenseCtrl.setExpensePayer(owner);
        expenseCtrl.setOldExpense(selected);
        expenseCtrl.ok();

        server.deleteExpense(selected);
        event = server.getByID(event.getId());

        expensesNotSelectedPart();
        expensesIncludingParticipant();
        expensesNotSelectedPart();
    }

    public void settleDebts() {
        Map<Participant, Double> partToAmount = mapParticipantToAmount();

        List<Pair<Participant, Double>> more = new ArrayList<>();
        List<Pair<Participant, Double>> less = new ArrayList<>();
        for(Participant p : partToAmount.keySet()) {
            if(partToAmount.get(p) > 0) {
                more.add(new Pair<>(p, partToAmount.get(p)));
            } else if(partToAmount.get(p) < 0) {
                less.add(new Pair<>(p, partToAmount.get(p) * -1));
            }
        }

        more.sort(Comparator.comparing(Pair<Participant, Double>::getValue, Comparator.reverseOrder()));
        less.sort(Comparator.comparing(Pair<Participant, Double>::getValue, Comparator.reverseOrder()));

        List<Debt> minDebts = new ArrayList<>();

        while(!more.isEmpty() && !less.isEmpty()) {
            Pair<Participant, Double> from = less.getFirst();
            Pair<Participant, Double> to = more.getFirst();
            double amount;

            if(Objects.equals(from.getValue(), to.getValue())) {
                amount = from.getValue();
                more.removeFirst();
                less.removeFirst();
            } else if(from.getValue() < to.getValue()) {
                amount = from.getValue();
                more.set(0, new Pair<>(to.getKey(), to.getValue() - from.getValue()));
                less.removeFirst();
            } else {
                amount = to.getValue();
                less.set(0, new Pair<>(from.getKey(), from.getValue() - to.getValue()));
                more.removeFirst();
            }

            Debt newDebt = new Debt(from.getKey(), to.getKey(), amount);
            minDebts.add(newDebt);
        }

        for(Debt d : server.getDebtsByEvent(event)) {
            event.getDebts().remove(d);
            server.deleteDebt(d);
        }

        server.addAllDebts(minDebts, event);
        event = server.getByID(event.getId());

        minDebts = server.getDebtsByEvent(event);
        controller.showSettleDebts(minDebts, event);
    }

    private @NotNull Map<Participant, Double> mapParticipantToAmount() {
        Map<Participant, Double> partToAmount = new HashMap<>();
        for(Participant p : participants) {
            List<Debt> credits = server.getDebtsByCreditor(p);
            double creditAmount = 0;
            for(Debt d : credits) {
                creditAmount += d.getAmount();
            }
            List<Debt> debits = server.getDebtsByDebtor(p);
            double debitAmount = 0;
            for(Debt d : debits) {
                debitAmount += d.getAmount();
            }
            partToAmount.put(p, creditAmount - debitAmount);
        }
        return partToAmount;
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
        part.setValue(new Label("Participants"));
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
        String participantsName = part.getValue().getText();
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
        String participantsName = part.getValue().getText();
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
