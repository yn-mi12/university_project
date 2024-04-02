package client.scenes;

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
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.net.URL;
import java.util.*;

public class AddExpenseCtrl implements Initializable {
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
    private TextArea whoPays;


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
    }

    public void cancel() {
        clearFields();
        controller.showEventOverview(ctrl.getSelectedEvent());
    }

    public void ok() {
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
            server.addExpense(expense, event);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        Event updated = server.getByInviteCode(ctrl.getSelectedEvent().getInviteCode());
        controller.showEventOverview(updated);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Removed this because we don't need to have language switching in the Expense overview
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
        String delimiterPattern = "[\\s,]+";
        String[] givenParticipants = whoPays.getText().split(delimiterPattern);
        for (int i = 0; i < givenParticipants.length; i++){
            double share = 100.0/givenParticipants.length;
            boolean isOwner = this.event.getParticipants().get(i).getFirstName().equals(whoPaid.getText());
            ExpenseParticipant expenseParticipant = new ExpenseParticipant(expense, event.getParticipantByName(givenParticipants[i]), share, isOwner);
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
        whoPays.clear();
    }

}
