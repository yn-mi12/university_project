package client.scenes;

import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class EventCtrl {

    private Stage primaryStage;

    private EventOverviewCtrl overviewCtrl;
    private Scene overview;
    private ModifyEventCtrl modifyCtrl;
    private AddEventCtrl addCtrl;
    private Scene modify;
    private Scene add;

    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void initShowOverview(Pair<EventOverviewCtrl, Parent> overview){
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        showOverview();
        primaryStage.show();
    }
    public void initAdd(Pair<AddEventCtrl, Parent> add){
        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());
    }
    public void initModify(Pair<ModifyEventCtrl, Parent> modify){
        this.modifyCtrl = modify.getKey();
        this.modify = new Scene(modify.getValue());
    }

    public void showOverview() {
        primaryStage.setTitle("Events: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Events: Adding Event");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }
    public void showModify(Event selectedEvent) {
        primaryStage.setTitle("Events: Modify event");
        this.modifyCtrl.oldTitle.setText(selectedEvent.getTitle());
        this.modifyCtrl.setSelectedEvent(selectedEvent);
        primaryStage.setScene(modify);
        modify.setOnKeyPressed(e -> modifyCtrl.keyPressed(e));
    }
}
