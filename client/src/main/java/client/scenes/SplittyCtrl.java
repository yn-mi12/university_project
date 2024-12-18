package client.scenes;

import client.Main;
import commons.Debt;
import commons.Event;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;

public class SplittyCtrl {

    private Stage primaryStage;
    private StartScreenCtrl overviewCtrl;
    private Scene overview;
    private Scene editParticipant;
    private EventOverviewCtrl eventCtrl;
    private Scene event;
    private Scene expense;
    private AddExpenseCtrl addExpenseCtrl;
    private EditParticipantOverviewCtrl editParticipantOverviewCtrl;
    private EditEventTitleCtrl editTitleCtrl;
    private Scene editTitle;
    private AddParticipantCtrl addParticipantCtrl;
    private Scene addParticipant;
    private AdminPopupCtrl adminPopupCtrl;
    private Scene adminPopup;
    private AdminOverviewCtrl adminOverviewCtrl;
    private Scene adminOverview;
    private boolean isAdmin;
    private SettleDebtsCtrl settleDebtsCtrl;
    private Scene settleDebts;
    private SetServerCtrl setServerCtrl;
    private Scene setServer;

    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initShowOverview(Pair<StartScreenCtrl, Parent> overview) {
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
    }

    public void display() {
        showOverview();
        primaryStage.show();
    }

    public void initExp(Pair<AddExpenseCtrl, Parent> addExp) {
        this.addExpenseCtrl = addExp.getKey();
        this.expense = new Scene(addExp.getValue());
    }

    public void initPartUpdate(Pair<EditParticipantOverviewCtrl, Parent> updatePart) {
        this.editParticipantOverviewCtrl = updatePart.getKey();
        this.editParticipant = new Scene(updatePart.getValue());
    }

    public void initEventOverview(Pair<EventOverviewCtrl, Parent> eventOverview) {
        this.eventCtrl = eventOverview.getKey();
        this.event = new Scene(eventOverview.getValue());
    }

    public void initEditTitle(Pair<EditEventTitleCtrl, Parent> editEventTitle) {
        this.editTitleCtrl = editEventTitle.getKey();
        this.editTitle = new Scene(editEventTitle.getValue());
    }

    public void initAddParticipant(Pair<AddParticipantCtrl, Parent> addParticipant) {
        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());
    }

    public void initSettleDebts(Pair<SettleDebtsCtrl, Parent> settleDebts) {
        this.settleDebtsCtrl = settleDebts.getKey();
        this.settleDebts = new Scene(settleDebts.getValue());
    }

    public void showOverview() {
        overviewCtrl.refresh();
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(overview);
        overview.setOnKeyPressed(e -> {
            overviewCtrl.keyPressed(e);
        });
        overview.getWindow().centerOnScreen();
        Main.setPosition("startScreen");
    }

    public void showEventOverview(Event selectedEvent) {
        primaryStage.setTitle(selectedEvent.getTitle());
        this.eventCtrl.setSelectedEvent(selectedEvent);
        this.eventCtrl.eventTitle.setText(selectedEvent.getTitle());
        primaryStage.setScene(event);
        event.setOnKeyPressed(e -> eventCtrl.keyPressed(e));
        event.getWindow().centerOnScreen();
        eventCtrl.expensesNotSelectedPart();
        eventCtrl.expensesIncludingParticipant();
        eventCtrl.expensesNotSelectedPart();
        Main.setPosition("eventScreen");
    }

    public void initExpShowOverview(Event event,
                                    Participant paid) {
        addExpenseCtrl.setEvent(paid, eventCtrl);
        showExpOverview();
        primaryStage.show();
    }

    public void showExpOverview() {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(expense);
        expense.getWindow().centerOnScreen();
        expense.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
        Main.setPosition("expenseOverview");
    }

    public void initEditParticipantOverview(Event event) {
        editParticipantOverviewCtrl.setEvent(event);
        showEditParticipantOverview();
        primaryStage.show();
    }

    public void showEditParticipantOverview() {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(editParticipant);
        editParticipant.getWindow().centerOnScreen();
        editParticipant.setOnKeyPressed(e -> editParticipantOverviewCtrl.keyPressed(e));
        Main.setPosition("editParticipantScreen");
    }

    public void showEditTitle(Event event) {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(editTitle);
        editTitle.getWindow().centerOnScreen();
        this.editTitleCtrl.setEvent(event);
        this.editTitleCtrl.oldTitle.setText(event.getTitle());
        editTitle.setOnKeyPressed(e -> editTitleCtrl.keyPressed(e));
        Main.setPosition("editTitleScreen");
    }

    public void showAddParticipant(Event event) {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(addParticipant);
        addParticipant.getWindow().centerOnScreen();
        this.addParticipantCtrl.setEvent(event);
        addParticipant.setOnKeyPressed(e -> addParticipantCtrl.keyPressed(e));
        Main.setPosition("addParticipantScreen");
    }

    public void showSettleDebts(List<Debt> debts, Event selectedEvent) {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(settleDebts);
        settleDebts.setOnKeyPressed(e -> settleDebtsCtrl.keyPressed(e));
        settleDebts.getWindow().centerOnScreen();
        settleDebtsCtrl.setDebts(debts);
        settleDebtsCtrl.setEvent(selectedEvent);
        settleDebtsCtrl.refresh();
        primaryStage.show();
        Main.setPosition("settleDebtsScreen");
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void initAdminPopup(Pair<AdminPopupCtrl, Parent> admin) {
        this.adminPopupCtrl = admin.getKey();
        this.adminPopup = new Scene(admin.getValue());
    }

    public void showAdminLogin() {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(adminPopup);
        adminPopup.getWindow().centerOnScreen();
        adminPopup.setOnKeyPressed(e -> adminPopupCtrl.keyPressed(e));
        Main.setPosition("adminLoginScreen");
    }

    public void initAdminOverview(Pair<AdminOverviewCtrl, Parent> adminOverview) {
        this.adminOverviewCtrl = adminOverview.getKey();
        this.adminOverview = new Scene(adminOverview.getValue());
    }

    public void showAdminOverview() {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(adminOverview);
        adminOverviewCtrl.refresh();
        adminOverview.getWindow().centerOnScreen();
        adminOverview.setOnKeyPressed(e -> adminOverviewCtrl.keyPressed(e));
        Main.setPosition("adminOverviewScreen");
    }

    public void initSetServer(Pair<SetServerCtrl, Parent> setServer) {
        this.setServerCtrl = setServer.getKey();
        this.setServer = new Scene(setServer.getValue());
    }

    public void showSetServer() {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(setServer);
        setServer.setOnKeyPressed(e -> setServerCtrl.keyPressed(e));
        setServer.getWindow().centerOnScreen();
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public AddParticipantCtrl getAddParticipantCtrl() {
        return addParticipantCtrl;
    }
}
