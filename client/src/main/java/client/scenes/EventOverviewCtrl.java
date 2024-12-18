package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
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
    @FXML
    public Label shareLabel;
    @FXML
    public Label allLabel;
    @FXML
    public Tab allTab;
    @FXML
    public Label confirmLabelEvent1;
    @FXML
    public Label confirmLabelEvent2;
    @FXML
    public Label confirmLabelExpense1;
    @FXML
    public Label confirmLabelExpense2;
    @FXML
    public Label confirmButton;
    @FXML
    public Label confirmCancelButton;

    private Participant expensePayer;

    private final SplittyCtrl controller;
    private final AddExpenseCtrl expenseCtrl;
    private List<Participant> participants;
    @FXML
    private ListView<String> participantList;
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
    private double totalAmount = 0;

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
        participantList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        participantList.addEventFilter(MouseEvent.MOUSE_PRESSED, javafx.event.Event::consume);
        hideTabPanes();
        this.event = selectedEvent;
        this.participants = event.getParticipants();
        ObservableList<Label> names = FXCollections.observableArrayList();
        HashMap<Label, Participant> map = new HashMap<>();
        List<String> participantsArrayList = new ArrayList<>();
        expensesNotSelectedPart();
        allExpenses.refresh();
        fromExpenses.refresh();
        includingExpenses.refresh();
        for (Participant p : participants) {
            Label item = new Label(p.getFirstName() + " " + p.getLastName());
            item.setStyle("-fx-background-color: transparent; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;");
            names.add(item);
            map.put(item, p);
            participantsArrayList.add(p.getFirstName() + " " + p.getLastName() + ", " + shareLabel.getText()
                    + " " + getShare(p));
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
        participantList.setItems(FXCollections.observableList(participantsArrayList));
        inviteCode.setText(event.getId());
    }

    private String getShare(Participant p) {
        double share = 0.0;
        List<Expense> expenses = server.getExpensesByEventId(event);
        for(Expense e : expenses) {
            for(ExpenseParticipant ep : e.getDebtors()) {
                if(ep.isOwner() && ep.getParticipant().equals(p)) {
                    share += e.getAmount();
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String returned = "";
        if(this.totalAmount != 0) {
            returned = df.format(100.0 * share/this.totalAmount) + "%";
        } else {
            returned = "0.00%";
        }
        return returned;
    }

    @SuppressWarnings("java.lang.ClassCastException")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label t = new Label(participantsLabel.getText());
        t.setStyle("-fx-text-fill: black");
        if(Main.isContrastMode()) t.setStyle("-fx-text-fill:  #F0F3FF");
        part.setValue(t);
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
                Main.reloadUIEvent(event);
                controller.showEventOverview(event);
            }
        }));

        allExpenses.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (allExpenses.getSelectionModel().getSelectedItem() != null) {
                viewButton.setDisable(false);
                deleteButton.setDisable(false);
                viewChoice = "all";
            }
        });

        fromExpenses.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (fromExpenses.getSelectionModel().getSelectedItem() != null) {
                viewButton.setDisable(false);
                deleteButton.setDisable(false);
                viewChoice = "from";
            }
        });

        includingExpenses.getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> {
            if (includingExpenses.getSelectionModel().getSelectedItem() != null) {
                viewButton.setDisable(false);
                deleteButton.setDisable(false);
                viewChoice = "include";
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
        deleteEventButton.setOnMouseEntered(e -> {deleteEventButton.setStyle("-fx-background-color: #c70000; " +
                "-fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1;");
            trash.setStyle("-fx-text-fill: #F0F3FF");
        });
        deleteEventButton.setOnMouseExited(e ->         {deleteEventButton.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;" +
                "-fx-font-weight: bolder;"+
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                "-fx-border-width: 1.5; -fx-border-insets: -1");
            trash.setStyle("-fx-text-fill: #ff3d3d");
        });
        deleteEventButton.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deleteEventButton.setStyle("-fx-background-color: #c70000; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                        "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1;");
                trash.setStyle("-fx-text-fill: #F0F3FF");
            }
            else {
                deleteEventButton.setStyle("-fx-background-color: #211951; -fx-text-fill: #ff3d3d;-fx-font-weight: bolder;"+
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; " +
                    "-fx-border-width: 1.5; -fx-border-insets: -1");
                trash.setStyle("-fx-text-fill: #ff3d3d");
            }
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
        participantList.setStyle("-fx-background-color: #836FFF;-fx-font-weight: bolder; " +
                "-fx-border-color: #211951; -fx-control-inner-background: #836FFF; " +
                "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 15%);" +
                "-fx-color-label-visible: #F0F3FF");
        inviteCode.setStyle("-fx-text-fill: black;-fx-font-weight: bolder");
    }

    public void addExpense() {
        controller.initExpShowOverview(event, expensePayer);
    }

    public Expense getExpense() {
        long id = 0;
        switch(viewChoice) {
            case "all" -> {
                String selected = allExpenses.getSelectionModel().getSelectedItem().split("\\[")[1];
                id = Long.valueOf(selected.substring(0, selected.length() - 1));
                allExpenses.getSelectionModel().clearSelection();
            }
            case "from" -> {
                String selected = allExpenses.getSelectionModel().getSelectedItem().split("\\[")[1];
                id = Long.valueOf(selected.substring(0, selected.length() - 1));
                fromExpenses.getSelectionModel().clearSelection();
            }
            case "include" -> {
                String selected = allExpenses.getSelectionModel().getSelectedItem().split("\\[")[1];
                id = Long.valueOf(selected.substring(0, selected.length() - 1));
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
        expenseCtrl.setOldExpense(selected);
        expenseCtrl.setOldExpensePayer(owner);
        expenseCtrl.setWhatForText(selected.getDescription());
        expenseCtrl.setHowMuchText(String.valueOf(selected.getAmount()));
        expenseCtrl.setCurrencyText(selected.getCurrency());
        expenseCtrl.setDateText(selected.getDate().toLocalDate());

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
        //HERE
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Confirmation");
        confirmationDialog.setHeaderText(confirmLabelExpense1.getText());
        confirmationDialog.setContentText(confirmLabelExpense2.getText());

        ButtonType okButton = new ButtonType(confirmButton.getText(), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType(confirmCancelButton.getText(), ButtonBar.ButtonData.CANCEL_CLOSE);

        confirmationDialog.getButtonTypes().setAll(okButton, cancelButton);

        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == okButton){
                System.out.println("Deleting expense: " + selected.getId());
                Participant owner = null;
                for(ExpenseParticipant ep : selected.getDebtors()) {
                    if(ep.isOwner())
                        owner = ep.getParticipant();
                }
                expenseCtrl.setEvent(owner, this);
                expenseCtrl.setOldExpense(selected);
                expenseCtrl.setOldExpensePayer(owner);
                expenseCtrl.ok();

                event = server.getByID(event.getId());
                server.send("/app/updated",event);
                expensesNotSelectedPart();
                expensesIncludingParticipant();
                expensesNotSelectedPart();
                expenseCtrl.setOldExpense(null);
                expenseCtrl.setOldExpensePayer(null);
            }else{
                controller.showEventOverview(event);
            }
        });

        event = server.getByID(event.getId());
        server.send("/app/updated",event);
        expensesNotSelectedPart();
        expensesIncludingParticipant();
        expensesFromParticipant();
        expenseCtrl.setOldExpense(null);
        expenseCtrl.setOldExpensePayer(null);
    }

    public void settleDebts() {
        Map<Participant, Double> amountForEachPart = mapParticipantToAmount();

        List<Pair<Participant, Double>> morePaid = new ArrayList<>();
        List<Pair<Participant, Double>> lessPaid = new ArrayList<>();
        for(Participant p : amountForEachPart.keySet()) {
            if(amountForEachPart.get(p) > 0.000001) {
                morePaid.add(new Pair<>(p, amountForEachPart.get(p)));
            } else if(amountForEachPart.get(p) < -0.000001) {
                lessPaid.add(new Pair<>(p, amountForEachPart.get(p) * -1.0));
            }
        }

        morePaid.sort(Comparator.comparing(k -> k.getKey().getId(), Comparator.reverseOrder()));
        lessPaid.sort(Comparator.comparing(k -> k.getKey().getId(), Comparator.reverseOrder()));

        calculateAndShowMinDebts(morePaid, lessPaid);
    }

    private @NotNull Map<Participant, Double> mapParticipantToAmount() {
        Map<Participant, Double> partToAmount = new HashMap<>();
        List<Expense> expenses = server.getExpensesByEventId(event);

        for(Participant p : participants) {
            partToAmount.put(p, 0.0);
        }

        for(Expense e : expenses) {
            for(ExpenseParticipant ep : e.getDebtors()) {
                double initial;
                if(!ep.isOwner()) {
                    initial = partToAmount.get(ep.getParticipant());
                    partToAmount.put(ep.getParticipant(), initial - (ep.getShare()/ 100.0 * e.getAmount()));
                } else {
                    initial = partToAmount.get(ep.getParticipant());
                    partToAmount.put(ep.getParticipant(), initial + ((1.0 - (ep.getShare()) / 100.0) * e.getAmount()));
                }
            }
        }
        return partToAmount;
    }

    private void calculateAndShowMinDebts(List<Pair<Participant, Double>> morePaid, List<Pair<Participant, Double>> lessPaid) {
        List<Debt> minDebts = new ArrayList<>();

        while(!morePaid.isEmpty() && !lessPaid.isEmpty()) {
            Pair<Participant, Double> from = lessPaid.getFirst();
            Pair<Participant, Double> to = morePaid.getFirst();
            double amount;

            if(Objects.equals(from.getValue(), to.getValue())) {
                amount = from.getValue();
                morePaid.removeFirst();
                lessPaid.removeFirst();
            } else if(from.getValue() < to.getValue()) {
                amount = from.getValue();
                morePaid.set(0, new Pair<>(to.getKey(), to.getValue() - from.getValue()));
                lessPaid.removeFirst();
            } else {
                amount = to.getValue();
                lessPaid.set(0, new Pair<>(from.getKey(), from.getValue() - to.getValue()));
                morePaid.removeFirst();
            }

            Debt newDebt = new Debt(from.getKey(), to.getKey(), amount);
            minDebts.add(newDebt);
        }
        List<Debt> finalDebts = new ArrayList<>();
        for(Debt debt : minDebts) {
            if(!(Math.abs(debt.getAmount()) < 0.000001))
                finalDebts.add(debt);
        }
        controller.showSettleDebts(finalDebts, event);
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
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation");
            confirmationDialog.setHeaderText(confirmLabelEvent1.getText());
            confirmationDialog.setContentText(confirmLabelEvent2.getText());

            ButtonType okButton = new ButtonType(confirmButton.getText(), ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType(confirmCancelButton.getText(), ButtonBar.ButtonData.CANCEL_CLOSE);

            confirmationDialog.getButtonTypes().setAll(okButton, cancelButton);

            confirmationDialog.showAndWait().ifPresent(response -> {
                if (response == okButton){
                    System.out.println("Deleting event: " + event.getId());
                    event.setId(server.getByID(event.getId()).getId());
                    server.send("/app/deleted", event);
                    if(!controller.getAdmin()) {
                        Config.get().removePastCode(event.getId());
                        Config.get().save();
                    }
                    goBack();
                }else{
                    controller.showEventOverview(event);
                }
            });
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        List<Expense> expenses = server.getExpensesByEventId(event);
        List<String> titles = new ArrayList<>();
        double totalAmount = 0;

        if (expenses != null) {
            for (Expense expense : expenses) {
                if(expense.getAmount() > 0) {
                    Participant owner = new Participant();
                    Set<ExpenseParticipant> expenseParticipants = expense.getDebtors();
                    for (ExpenseParticipant expenseParticipant : expenseParticipants) {
                        if (expenseParticipant.isOwner()) {
                            owner = expenseParticipant.getParticipant();
                        }
                    }
                    String expenseString = expense.getDate() + ": " + owner.getFirstName() + " " + owner.getLastName() + " "
                            + paidLabel.getText() + " " + df.format(expense.getAmount()) + " " + forLabel.getText() + " " + expense.getDescription()
                                + " (" + getParts(expense) + ") [" + expense.getId() + "]";
                    titles.add(expenseString);
                    totalAmount += expense.getAmount();
                }
            }
        }
        this.totalAmount = totalAmount;
        String text = totalCost.getText().replaceAll("[0-9]", "").replace(".", "");
        if (text.charAt(text.length() - 1) == ' ')
            totalCost.setText(text + df.format(totalAmount));
        else totalCost.setText(text + " " + df.format(totalAmount));

        allExpenses.setItems(FXCollections.observableList(titles));
    }

    public void expensesFromParticipant() {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String participantsName = part.getValue().getText();
        Participant participant = event.getParticipantByName(participantsName);
        List<Expense> expenses = server.getExpensesByEventId(event);
        List<Expense> expensesFromParticipant = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        if (expenses != null) {
            for (Expense expense : expenses) {
                if(expense.getAmount() > 0) {
                    List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
                    for (int i = 0; i < debtors.size(); i++) {
                        if (debtors.get(i).isOwner() && debtors.get(i).getParticipant().equals(participant)) {
                            expensesFromParticipant.add(expense);
                        }
                    }
                }
            }
            for (Expense expense : expensesFromParticipant) {
                String expenseString = expense.getDate() + ": " +participant.getFirstName() + " " + participant.getLastName() +
                        " " + paidLabel.getText() + " " + df.format(expense.getAmount()) + " " + forLabel.getText() + " " + expense.getDescription()
                            + " (" + getParts(expense) + ") [" + expense.getId() + "]";
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
            setIncludingExpenses(expenses, participant, expensesIncludingParticipant, titles);
        }
        includingExpenses.setItems(FXCollections.observableList(titles));
    }

    private void setIncludingExpenses(List<Expense> expenses, Participant participant,
                                      List<Expense> expensesIncludingParticipant, List<String> titles) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        for (Expense expense : expenses) {
            if(expense.getAmount() > 0) {
                List<ExpenseParticipant> debtors = new ArrayList<>(expense.getDebtors());
                for (int i = 0; i < debtors.size(); i++) {
                    if (debtors.get(i).getParticipant().equals(participant)) {
                        expensesIncludingParticipant.add(expense);
                    }
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
            String expenseString = expense.getDate() + ": " + owner.getFirstName() +  " " + owner.getLastName() +  " "
                    + paidLabel.getText() + " " + df.format(expense.getAmount()) + " " + forLabel.getText() + " " + expense.getDescription()
                        + " (" + getParts(expense) + ") [" + expense.getId() + "]";
            titles.add(expenseString);
        }
    }

    private String getParts(Expense e) {
        String result = "";
        int count = 0;
        for(ExpenseParticipant ep : e.getDebtors()) {
            if(ep.getShare() != 0) {
                result += ep.getParticipant().getFirstName() + " " + ep.getParticipant().getLastName() + ", ";
                count++;
            }
        }
        if(count == server.getByID(event.getId()).getParticipants().size())
            return allLabel.getText();
        return result.substring(0, result.length() - 2);
    }

    public void hideTabPanes() {
        tabPane.getTabs().remove(fromTab);
        tabPane.getTabs().remove(includingTab);
    }

    public void showTabPanes() {
        tabPane.getTabs().add(fromTab);
        tabPane.getTabs().add(includingTab);
    }

    public void launch() {
        server.registerForMessages("/topic/updated", Event.class , q -> {
            if(q!=null && q.getId().equals(event.getId())) {
                event = q;
                setSelectedEvent(event);
                Platform.runLater(() -> {
                    switch (Main.getPosition()){
                        case "eventScreen":
                            Main.reloadUIEvent(event);
                            break;
                        case "editParticipantScreen":
                            controller.initEditParticipantOverview(event);
                            break;
                        case "editTitleScreen":
                            controller.showEditTitle(event);
                            break;
                        default:
                            break;
                    }
                });
            }
        });
        server.registerForMessages("/topic/deleted", Event.class , q -> {
            if(q!=null && q.getId().equals(event.getId())) {
                Platform.runLater(() -> {
                    switch (Main.getPosition()){
                        case "eventScreen":
                        case "editParticipantScreen":
                        case "editTitleScreen":
                        case "settleDebtsScreen":
                        case "addParticipantScreen":
                        case "expenseOverview":
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Event missing");
                            alert.setHeaderText(null);
                            alert.setContentText("The event you are viewing has been deleted!");
                            alert.showAndWait();
                            Main.reloadUI();
                            goBack();
                            break;
                    }
                });
            }
        });
    }
    public void keyPressed(KeyEvent e) {
        //Point p = MouseInfo.getPointerInfo().getLocation();
        switch (e.getCode()) {
            case ESCAPE:
                goBack();
                break;
        }
    }
}
