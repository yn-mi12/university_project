package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.BadRequestException;
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

import java.net.URL;
import java.util.*;

public class AddExpenseCtrl implements Initializable {
    public ComboBox<Label> tagsComboBox;
    public Label expenseType;
    public Label howToSplitLabel;
    public Label whenLabel;
    public Label howMuchLabel;
    public Label whatForLabel;
    public Label whoPaidLabel;
    public Label addExpenseLabel;
    public Button addButton;
    public Button cancelButton;
    public AnchorPane background;
    private EventOverviewCtrl ctrl;
    private Event event;
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private List<Participant> participants;
    private Participant expensePayer;
    private Expense expense;
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
        ObservableList<Label> names = FXCollections.observableArrayList();
        HashMap<Label,Participant> map = new HashMap<>();

        for (Participant p : participants) {
            Label item = new Label(p.getFirstName() + " " + p.getLastName());
            names.add(item);
            map.put(item,p);
        }
        whoPaid.getItems().setAll(names);
        whoPaid.setValue(new Label(paid.getFirstName() + " " + paid.getLastName()));
        whoPaid.getValue().setStyle("-fx-text-fill: #000000");
        if(Main.isContrastMode())whoPaid.getValue().setStyle("-fx-text-fill: #F0F3FF");
        whoPaid.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                newVal.setStyle("-fx-text-fill: #000000");
                if(Main.isContrastMode())newVal.setStyle("-fx-text-fill: #F0F3FF");
                whoPaid.setValue(newVal);
                expensePayer = map.get(newVal);
            }
        }));
        List<String> listOfParticipants = new ArrayList<>();
        for(Participant participant : event.getParticipants()){
            listOfParticipants.add(participant.getFirstName() + " " + participant.getLastName());
        }
        whoPays.setItems(FXCollections.observableList(listOfParticipants));
        whoPays.refresh();
        whoPays.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    public void cancel() {
        clearFields();
        Main.reloadUIEvent(event);
        controller.showEventOverview(ctrl.getSelectedEvent());
    }

    public void ok() {
        Event updated;
        try {

            System.out.println("Add expense");
            System.out.println("Id:" + event.getId());
            System.out.println("Get expense");
            var description = this.whatFor.getText();
            var amount = howMuch.getText();
            var currency = this.currency.getValue().getText();
            var date = this.date.getValue();
            //var tags = this.tags.getText();
            this.expense = new Expense(description, currency,
                    Double.parseDouble(amount), java.sql.Date.valueOf(date));
            System.out.println(expense);
            expense.setDebtors(getDebtors());
            expense.setEvent(event);
            Expense saved = server.addExpense(expense, event);
            updated = server.getByInviteCode(ctrl.getSelectedEvent().getInviteCode());

            for(ExpenseParticipant ep : saved.getDebtors())
                if(ep.isOwner())
                    expensePayer = ep.getParticipant();
            participants = server.getEventParticipants(updated);
            calculateDebts(saved, updated);
            updated = server.getByInviteCode(ctrl.getSelectedEvent().getInviteCode());

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;

        }
        clearFields();
        controller.showEventOverview(updated);
    }

    public void calculateDebts(Expense saved, Event event) {
        List<Debt> debtsCreditor = server.getDebtsByCreditor(expensePayer);
        Map<Participant, Debt> debtorToDebt = new HashMap<>();

        if(debtsCreditor.isEmpty()) {
            for(Participant p : participants) {
                Debt d = new Debt(p, expensePayer, 0);
                debtorToDebt.put(p, d);
            }
            debtorToDebt.remove(expensePayer);
        } else {
            List<Participant> debtParticipants = new ArrayList<>();
            for(Debt d : debtsCreditor) {
                debtParticipants.add(d.getDebtor());
                debtorToDebt.put(d.getDebtor(), d);
            }

            for(Participant p : participants) {
                if(!debtParticipants.contains(p) && !p.equals(expensePayer)) {
                    Debt created = new Debt(p, expensePayer, 0);
                    debtsCreditor.add(created);
                    debtorToDebt.put(p, created);
                }
            }
        }
        setAndAddDebts(saved, event, debtorToDebt);
    }

    public void setAndAddDebts(Expense saved, Event event, Map<Participant, Debt> debtorToDebt) {
        for (ExpenseParticipant ep : saved.getDebtors()) {
            if (!ep.isOwner()) {
                Debt d = debtorToDebt.get(ep.getParticipant());
                d.setAmount(d.getAmount() + (ep.getShare() / 100 * saved.getAmount()));
                debtorToDebt.replace(ep.getParticipant(), d);
            }
        }
        List<Debt> debtsDebtor = server.getDebtsByDebtor(expensePayer);
        try {
            for (Debt debt : debtsDebtor) {
                Participant p = debt.getCreditor();
                Debt d = debtorToDebt.get(p);
                if (debt.getAmount() > d.getAmount()) {
                    debtorToDebt.remove(p);
                    debt.setAmount(debt.getAmount() - d.getAmount());
                    server.updateDebtAmount(debt.getAmount(), debt);
                } else if (debt.getAmount() < d.getAmount()) {
                    server.deleteDebt(debt);
                    event.getDebts().remove(debt);
                    d.setAmount(d.getAmount() - debt.getAmount());
                    debtorToDebt.replace(p, d);
                } else {
                    server.deleteDebt(debt);
                    debtorToDebt.remove(p);
                }
            }
        } catch(BadRequestException e) {
            System.out.println("Failed to add debts");
            return;
        }

        List<Debt> finalDebts = new ArrayList<>();
        for (Debt debt : debtorToDebt.values()) {
            System.out.println(debt);
            if (debt.getAmount() != 0)
                finalDebts.add(debt);
        }

        server.addAllDebts(finalDebts, event);
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
        ObservableList<String> selectedParticipants = whoPays.getSelectionModel().getSelectedItems();
        for (int i = 0; i < selectedParticipants.size(); i++){
            double share = 100.0/selectedParticipants.size();
            boolean isOwner = selectedParticipants.get(i).equals(whoPaid.getValue().getText());
            ExpenseParticipant expenseParticipant = new
                    ExpenseParticipant(expense, event.getParticipantByName(selectedParticipants.get(i)), share, isOwner);
            debtors.add(expenseParticipant);
        }
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
        whoPays.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Label> currencies = new ArrayList<>();
        currencies.add(new Label("USD"));
        currencies.add(new Label("EUR"));
        currencies.add(new Label("CHF"));
        currency.setItems(FXCollections.observableList(currencies));
        tagsComboBox.setCellFactory(new Callback<>() {
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
            tagsComboBox.setStyle(Main.changeUI(tagsComboBox));
            date.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF;" +
                    "-fx-border-width: 2.5; -fx-border-insets: -2;-fx-control-inner-background:#211951");
            expenseType.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
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
            tagsComboBox.getSelectionModel().selectedItemProperty().addListener(((obs1, oldVal1, newVal1) -> {
                if (newVal1 != null) {
                    if(Main.isContrastMode())newVal1.setStyle(("-fx-text-fill: #F0F3FF"));
                    currency.setValue(newVal1);
                }
        }));
            Main.languageFeedback(tagsComboBox);
            Main.languageFeedback(currency);
            Main.languageFeedback(whoPaid);
        }
    }
}
