package client.scenes;

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
    private InvitationCtrl inviteCtrl;
    private Scene invite;
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

    public void initInvitePage(Pair<InvitationCtrl, Parent> invite) {
        this.inviteCtrl = invite.getKey();
        this.invite = new Scene(invite.getValue());
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
        overview.getWindow().centerOnScreen();
    }

    public void showEventOverview(Event selectedEvent){
        primaryStage.setTitle("Event: " + selectedEvent.getTitle());
        this.eventCtrl.setSelectedEvent(selectedEvent);
        this.eventCtrl.eventTitle.setText(selectedEvent.getTitle());
        primaryStage.setScene(event);
        event.getWindow().centerOnScreen();
        eventCtrl.expensesNotSelectedPart();
        eventCtrl.expensesIncludingParticipant();
        eventCtrl.expensesNotSelectedPart();
    }

    public void showInvitePage(Event selectedEvent) {
        primaryStage.setTitle("Event: " + selectedEvent.getTitle());
        this.inviteCtrl.eventInviteTitle.setText(selectedEvent.getTitle());
        this.inviteCtrl.inviteCode.setText(selectedEvent.getId());
        this.inviteCtrl.event = selectedEvent;
        primaryStage.setScene(invite);
        invite.getWindow().centerOnScreen();
        invite.setOnKeyPressed(e -> inviteCtrl.keyPressed(e));
    }

    public void initExpShowOverview(Event event,
                                    Participant paid) {
        addExpenseCtrl.setEvent(paid,eventCtrl);

        showExpOverview();
        primaryStage.show();
    }

    public void showExpOverview() {
        primaryStage.setTitle("Add/Edit Expense");
        primaryStage.setScene(expense);
        expense.getWindow().centerOnScreen();
        expense.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
    }

    public void initEditParticipantOverview(Event event) {
        editParticipantOverviewCtrl.setEvent(event);
        showEditParticipantOverview();
        primaryStage.show();
    }

    public void showEditParticipantOverview() {
        primaryStage.setTitle("Edit Participant");
        primaryStage.setScene(editParticipant);
        editParticipant.getWindow().centerOnScreen();
        //editParticipant.setOnKeyPressed(e -> editParticipantOverviewCtrl.keyPressed(e));
    }

    public void showEditTitle(Event event) {
        primaryStage.setTitle("Edit Title");
        primaryStage.setScene(editTitle);
        editTitle.getWindow().centerOnScreen();
        this.editTitleCtrl.setEvent(event);
        this.editTitleCtrl.oldTitle.setText(event.getTitle());
        editTitle.setOnKeyPressed(e -> editTitleCtrl.keyPressed(e));
    }

    public void showAddParticipant(Event event) {
        primaryStage.setTitle("Add Participant");
        primaryStage.setScene(addParticipant);
        addParticipant.getWindow().centerOnScreen();
        this.addParticipantCtrl.setEvent(event);
        addParticipant.setOnKeyPressed(e -> addParticipantCtrl.keyPressed(e));
    }

    public void showSettleDebts(List<Debt> debts, Event selectedEvent) {
        primaryStage.setTitle("Settle Debts");
        primaryStage.setScene(settleDebts);
        settleDebts.getWindow().centerOnScreen();
        settleDebtsCtrl.setDebts(debts);
        settleDebtsCtrl.setEvent(selectedEvent);
        settleDebtsCtrl.refresh();
        primaryStage.show();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void initAdminPopup(Pair<AdminPopupCtrl, Parent> admin) {
        this.adminPopupCtrl = admin.getKey();
        this.adminPopup = new Scene(admin.getValue());
    }

    public void showAdminLogin() {
        primaryStage.setTitle("Admin Login");
        primaryStage.setScene(adminPopup);
        adminPopup.getWindow().centerOnScreen();
        //adminPopup.setOnKeyPressed(e -> adminPopupCtrl.keyPressed(e));
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
    }

    public void initSetServer(Pair<SetServerCtrl, Parent> setServer) {
        this.setServerCtrl = setServer.getKey();
        this.setServer = new Scene(setServer.getValue());
    }

    public void showSetServer() {
        primaryStage.setTitle("Set Server");
        primaryStage.setScene(setServer);
        setServer.getWindow().centerOnScreen();
    }

    public boolean getAdmin()
    {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }

    public AddParticipantCtrl getAddParticipantCtrl() {
        return addParticipantCtrl;
    }
}
