package client.scenes;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.time.LocalDate;
import java.net.URL;
import java.util.*;

public class AddExpenseCtrl implements Initializable {
    @FXML
    public Label howToSplitLabel;
    @FXML
    public Label whenLabel;
    @FXML
    public Label howMuchLabel;
    @FXML
    public Label whatForLabel;
    @FXML
    public Label whoPaidLabel;
    @FXML
    public Label addExpenseLabel;
    @FXML
    public Button addButton;
    @FXML
    public Button cancelButton;
    @FXML
    public AnchorPane background;
    private EventOverviewCtrl ctrl;
    private Event event;
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private List<Participant> participants;
    private Participant expensePayer;
    private Expense expense;
    private Expense oldExpense;
    private Participant oldExpensePayer;
    private boolean delete;
    @FXML
    private ComboBox<Label> whoPaid;
    @FXML
    private TextField whatFor = new TextField();
    @FXML
    private TextField howMuch = new TextField();
    @FXML
    private ComboBox<Label> currency;
    @FXML
    private DatePicker date = new DatePicker();
    @FXML
    private CheckBox allHaveToPay = new CheckBox();
    @FXML
    private CheckBox someHaveToPay = new CheckBox();
    @FXML
    private ListView<String> whoPays;

    @Inject
    public AddExpenseCtrl(ServerUtilsEvent server, SplittyCtrl ctrl) {
        this.controller = ctrl;
        this.server = server;
    }

    public void setEvent(Participant paid, EventOverviewCtrl ctrl) {
        expensePayer = paid;
        this.ctrl = ctrl;
        this.event = ctrl.getSelectedEvent();
        this.participants = event.getParticipants();
        if(!delete) {
            ObservableList<Label> names = FXCollections.observableArrayList();
            HashMap<Label, Participant> map = new HashMap<>();

            for (Participant p : participants) {
                Label item = new Label(p.getFirstName() + " " + p.getLastName());
                names.add(item);
                map.put(item, p);
            }
            configureComboBox(paid, names, map);
            List<String> listOfParticipants = new ArrayList<>();
            for (Participant participant : event.getParticipants()) {
                listOfParticipants.add(participant.getFirstName() + " " + participant.getLastName());
            }
            whoPays.setItems(FXCollections.observableList(listOfParticipants));
            whoPays.refresh();
            whoPays.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
            currency.setValue(null);
        }
    }

    private void configureComboBox(Participant paid, ObservableList<Label> names, HashMap<Label, Participant> map) {
        whoPaid.getItems().setAll(names);
        whoPaid.setValue(new Label(paid.getFirstName() + " " + paid.getLastName()));
        whoPaid.getValue().setStyle("-fx-text-fill: #000000");
        if (Main.isContrastMode()) whoPaid.getValue().setStyle("-fx-text-fill: #F0F3FF");
        whoPaid.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                newVal.setStyle("-fx-text-fill: #000000");
                if (Main.isContrastMode()) newVal.setStyle("-fx-text-fill: #F0F3FF");
                whoPaid.setValue(newVal);
                expensePayer = map.get(newVal);
                controller.showExpOverview();
            }
        }));
    }

    public void cancel() {
        oldExpense = null;
        oldExpensePayer = null;
        clearFields();
        Main.reloadUIEvent(event);
        controller.showEventOverview(ctrl.getSelectedEvent());
    }

    public void ok() {
        Event updated;
        try {
            if(oldExpense != null) {
                if(delete) {
                    delete = false;
                    server.deleteExpense(oldExpense);
                    return;
                }
                //server.updateExpenseAmount(oldExpense.getAmount() * -1.0, oldExpense);
                server.deleteExpense(oldExpense);
                event = server.getByID(event.getId());
                oldExpense = null;
                oldExpensePayer = null;
            }

            expensePayer = event.getParticipantByName(whoPaid.getValue().getText());
            event = server.getByID(event.getId());
            var description = this.whatFor.getText();
            var amount = howMuch.getText();
            var currency = this.currency.getValue().getText();
            var date = this.date.getValue();
            this.expense = new Expense(description, currency,
                    Double.parseDouble(amount), java.sql.Date.valueOf(date));
            expense.setDebtors(getDebtors());
            expense.setEvent(event);
            Expense saved = server.addExpense(expense, event);

            updated = server.getByID(ctrl.getSelectedEvent().getId());
            participants = server.getEventParticipants(updated);
            for(ExpenseParticipant ep : saved.getDebtors())
                if(ep.isOwner())
                    expensePayer = ep.getParticipant();

            updated = server.getByID(ctrl.getSelectedEvent().getId());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        controller.showEventOverview(updated);
        server.send("/app/updated",updated);
    }

    @FXML
    private void handleCheckBoxAction() {
        if (allHaveToPay.isSelected() && someHaveToPay.isSelected()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Only one check box can be selected!");
            alert.showAndWait();
            someHaveToPay.setSelected(false);
        }
    }

    public HashSet<ExpenseParticipant> getDebtors(){
        HashSet<ExpenseParticipant> debtors = new HashSet<>();
        if (allHaveToPay.isSelected()){
            double share = 100.0/event.getParticipants().size();
            for (int i = 0; i < event.getParticipants().size(); i++){
                String fullName = this.event.getParticipants().get(i).getFirstName() + " " +
                        this.event.getParticipants().get(i).getLastName();
                boolean isOwner = fullName.equals(whoPaid.getValue().getText());
                ExpenseParticipant expenseParticipant =
                        new ExpenseParticipant(expense, event.getParticipants().get(i),share, isOwner);
                debtors.add(expenseParticipant);
            }
            return debtors;
        }
        boolean check = false;
        ObservableList<String> selectedParticipants = whoPays.getSelectionModel().getSelectedItems();
        for (int i = 0; i < selectedParticipants.size(); i++){
            double share = 100.0/selectedParticipants.size();
            boolean isOwner = selectedParticipants.get(i).equals(whoPaid.getValue().getText());
            if(isOwner)
                check = true;
            ExpenseParticipant expenseParticipant = new
                    ExpenseParticipant(expense, event.getParticipantByName(selectedParticipants.get(i)), share, isOwner);
            debtors.add(expenseParticipant);
        }
        if(!check)
            debtors.add(new ExpenseParticipant(expense, event.getParticipantByName(whoPaid.getValue().getText()), 0, true));
        return debtors;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    private void clearFields() {
        howMuch.clear();
        whatFor.clear();
        date.setValue(null);
        currency.setValue(new Label());
        allHaveToPay.setSelected(false);
        someHaveToPay.setSelected(false);
        whoPaid.getSelectionModel().clearSelection();
        whoPays.getSelectionModel().clearSelection();
    }

    public void setWhatForText(String whatFor) {
        this.whatFor.setText(whatFor);
    }

    public void setHowMuchText(String howMuch) {
        this.howMuch.setText(howMuch);
    }

    public void setCurrencyText(String currency) {
        this.currency.getSelectionModel().select(new Label(currency));
    }

    public void setDateText(LocalDate date) {
        this.date.setValue(date);
    }

    public ListView<String> getWhoPays() {
        return whoPays;
    }

    public CheckBox getAllHaveToPay() {
        return allHaveToPay;
    }

    public CheckBox getSomeHaveToPay() {
        return someHaveToPay;
    }

    public void setOldExpense(Expense oldExpense) {
        this.oldExpense = oldExpense;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public void setExpensePayer(Participant expensePayer) {
        this.expensePayer = expensePayer;
    }

    public void setOldExpensePayer(Participant oldExpensePayer) {
        this.oldExpensePayer = oldExpensePayer;
    }

    public void setWhoPaid(String fullName) {
        this.whoPaid.setValue(new Label(fullName));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Label> currencies = new ArrayList<>();
        currencies.add(new Label("USD"));
        currencies.add(new Label("EUR"));
        currencies.add(new Label("CHF"));
        currency.setItems(FXCollections.observableList(currencies));
        whoPaid.setCellFactory(new Callback<>() {
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
        currency.setCellFactory(new Callback<>() {
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
        if(Main.isContrastMode())
        {
            background.setStyle("-fx-background-color: #69e0ab;");
            addButton.setStyle(Main.changeUI(addButton));
            Main.buttonFeedback(addButton);
            cancelButton.setStyle(Main.changeUI(cancelButton));
            Main.buttonFeedback(cancelButton);
            addExpenseLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            howMuchLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            whenLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            howToSplitLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            whoPaidLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            whatForLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            whoPaid.setStyle(Main.changeUI(whoPaid));
            whatFor.setStyle(Main.changeUI(whatFor));
            howMuch.setStyle(Main.changeUI(howMuch));
            currency.setStyle(Main.changeUI(currency));
            date.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF;" +
                    "-fx-border-width: 2.5; -fx-border-insets: -2;-fx-control-inner-background:#211951");
            allHaveToPay.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            someHaveToPay.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            whoPays.setStyle("-fx-background-color: #836FFF;-fx-font-weight: bolder; " +
                    "-fx-border-color: #211951; -fx-control-inner-background: #836FFF; " +
                    "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 15%);" +
                    "-fx-color-label-visible: #F0F3FF");
            currency.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                if(Main.isContrastMode())newVal.setStyle(("-fx-text-fill: #F0F3FF"));
                currency.setValue(newVal);
            }
            }));
            Main.languageFeedback(currency);
            Main.languageFeedback(whoPaid);
        }
    }
}
