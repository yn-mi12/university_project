package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.*;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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
    public AnchorPane background;
    public Button backButton;
    public Button editTitleButton;
    public Button copyCodeButton;
    public Button editPartButton;
    public Button addPartButton;
    public Button addExpenseButton;
    public Button settleDebtsButton;
    public Button deleteEventButton;
    public Text languageChoice;
    public Label inviteCodeLabel;
    public Label expenseLabel;
    public Label participantsLabel2;
    private Participant expensePayer;
    private final SplittyCtrl controller;
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
    public Event event;
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

    @Inject
    public EventOverviewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }

    public Event getSelectedEvent() {
        return event;
    }

    public void setSelectedEvent(Event selectedEvent) {
        hideTabPanes();
        this.event = selectedEvent;
        this.event = server.getByInviteCode(selectedEvent.getInviteCode());
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
        inviteCode.setText(event.getInviteCode());
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
        expensePayer = null;
    }

    private void isContrast() {
        background.setStyle("-fx-background-color: #69e0ab;");
        Main.languageFeedback(languageBox);
        languageBox.setStyle(Main.changeUI(languageBox));
        backButton.setStyle(Main.changeUI(backButton));
        Main.buttonFeedback(backButton);
        addExpenseButton.setStyle(Main.changeUI(addExpenseButton));
        Main.buttonFeedback(addExpenseButton);
        Main.buttonFeedback(deleteEventButton);
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
        if(expensePayer!=null) controller.initExpShowOverview(event, expensePayer);
    }

    public void settleDebts() {
        List<Debt> allDebts = server.getDebtsByEvent(event);
        controller.showSettleDebts(allDebts, event);
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
            event.setId(server.getByInviteCode(event.getInviteCode()).getId());
            server.send("/app/deleted", event);

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
                String expenseString = owner.getFirstName() + " " + owner.getLastName() + " "
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
                String expenseString = participant.getFirstName() + " " + participant.getLastName() +
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
                String expenseString = owner.getFirstName() +  " " + owner.getLastName() +  " "
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
