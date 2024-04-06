package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.*;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.util.*;

public class AddExpenseCtrl {
    private EventOverviewCtrl ctrl;
    private Event event;
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private List<Participant> participants;
    private Participant expensePayer;
    private Expense expense;
    @FXML
    private SplitMenuButton whoPaid;
    @FXML
    private TextField whatFor = new TextField();
    @FXML
    private TextField howMuch = new TextField();
    @FXML
    private ChoiceBox<String> currency = new ChoiceBox<>();
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
        ObservableList<MenuItem> names = FXCollections.observableArrayList();
        HashMap<MenuItem,Participant> map = new HashMap<>();

        for (Participant p : participants) {
            MenuItem item = new MenuItem(p.getFirstName());
            names.add(item);
            map.put(item,p);
        }
        whoPaid.getItems().setAll(names);
        whoPaid.setText(paid.getFirstName());
        for (MenuItem mi : whoPaid.getItems()) {
            mi.setOnAction(e -> {
                whoPaid.setText(mi.getText());
                expensePayer = map.get(mi);
            });
        }
        List<String> listOfParticipants = new ArrayList<>();
        for(Participant participant : event.getParticipants()){
            listOfParticipants.add(participant.getFirstName());
        }
        whoPays.setItems(FXCollections.observableList(listOfParticipants));
        whoPays.refresh();
        whoPays.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    public void cancel() {
        clearFields();
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
            var currency = this.currency.getValue();
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
        List<Debt> debts = server.getDebtsByCreditor(expensePayer);
        Map<Participant, Debt> debtorToDebt = new HashMap<>();

        if(debts.isEmpty()) {
            for(Participant p : participants) {
                Debt d = new Debt(p, expensePayer, 0);
                debtorToDebt.put(p, d);
            }
            debtorToDebt.remove(expensePayer);
        } else {
            List<Participant> debtParticipants = new ArrayList<>();
            for(Debt d : debts) {
                debtParticipants.add(d.getDebtor());
                debtorToDebt.put(d.getDebtor(), d);
            }

            for(Participant p : participants) {
                if(!debtParticipants.contains(p) && !p.equals(expensePayer)) {
                    Debt created = new Debt(p, expensePayer, 0);
                    debts.add(created);
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
        for (Debt debt : debtorToDebt.values()) {
            if (debt.getAmount() != 0)
                server.addDebt(debt, event);
        }
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
                boolean isOwner = this.event.getParticipants().get(i).getFirstName().equals(whoPaid.getText());
                ExpenseParticipant expenseParticipant =
                        new ExpenseParticipant(expense, event.getParticipants().get(i),share, isOwner);
                debtors.add(expenseParticipant);
            }
            return debtors;
        }
        ObservableList<String> selectedParticipants = whoPays.getSelectionModel().getSelectedItems();
        for (int i = 0; i < selectedParticipants.size(); i++){
            double share = 100.0/selectedParticipants.size();
            boolean isOwner = selectedParticipants.get(i).equals(whoPaid.getText());
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
        currency.setValue(null);
        allHaveToPay.setSelected(false);
        someHaveToPay.setSelected(false);
        whoPays.getSelectionModel().clearSelection();
    }

}
