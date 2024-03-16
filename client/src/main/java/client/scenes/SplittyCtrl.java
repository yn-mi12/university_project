package client.scenes;

import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class SplittyCtrl {

    private Stage primaryStage;
    private StartScreenCtrl overviewCtrl;
    private Scene overview;
    private ModifyEventCtrl modifyCtrl;
    private AddEventCtrl addCtrl;
    private Scene modify;
    private Scene add;
    private EventOverviewNewCtrl eventCtrl;
    private Scene event;
    private InvitationCtrl inviteCtrl;
    private Scene invite;

    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initShowOverview(Pair<StartScreenCtrl, Parent> overview) {
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        showOverview();
        primaryStage.show();
    }

    public void initAdd(Pair<AddEventCtrl, Parent> add) {
        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());
    }

    public void initModify(Pair<ModifyEventCtrl, Parent> modify) {
        this.modifyCtrl = modify.getKey();
        this.modify = new Scene(modify.getValue());
    }

    public void initEventOverview(Pair<EventOverviewNewCtrl, Parent> eventOverview) {
        this.eventCtrl = eventOverview.getKey();
        this.event = new Scene(eventOverview.getValue());
    }

    public void initInvitePage(Pair<InvitationCtrl, Parent> invite) {
        this.inviteCtrl = invite.getKey();
        this.invite = new Scene(invite.getValue());
    }

    public void showOverview() {
        primaryStage.setTitle("Splitty");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd(String title) {
        primaryStage.setTitle("Events: Adding Event");
        primaryStage.setScene(add);
        this.addCtrl.setTitleAndCode(title, addCtrl.getEvent().getInviteCode());
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showModify(Event selectedEvent) {
        primaryStage.setTitle("Events: Modify event");
        this.modifyCtrl.oldTitle.setText(selectedEvent.getTitle());
        this.modifyCtrl.setSelectedEvent(selectedEvent);
        primaryStage.setScene(modify);
        modify.setOnKeyPressed(e -> modifyCtrl.keyPressed(e));
    }

    public void showEventOverview(Event selectedEvent){
        primaryStage.setTitle("Event: " + selectedEvent.getTitle());
        this.eventCtrl.setSelectedEvent(selectedEvent);
        this.eventCtrl.eventTitle.setText(selectedEvent.getTitle());
        primaryStage.setScene(event);
        event.setOnKeyPressed(e -> eventCtrl.keyPressed(e));
    }

    public void showInvitePage(Event selectedEvent){
        primaryStage.setTitle("Event: " + selectedEvent.getTitle());
        this.inviteCtrl.setSelectedEvent(selectedEvent);
        this.inviteCtrl.eventInviteTitle.setText(selectedEvent.getTitle());
        primaryStage.setScene(invite);
        invite.setOnKeyPressed(e -> inviteCtrl.keyPressed(e));
    }

}
