package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class SettleDebtsCtrl {

    private ServerUtilsEvent server;
    private final SplittyCtrl eventCtrl;
    private List<Debt> debts;
    private Event event;
    @FXML
    private VBox debtBox;
    @FXML
    private Label settledLabel;

    @Inject
    public SettleDebtsCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.eventCtrl = eventCtrl;
    }

    public void refresh() {
        settledLabel.setVisible(false);
        if(debts.isEmpty()) {
            debtBox.getChildren().setAll(new Label(""));
            settledLabel.setVisible(true);
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for(Debt d : debts) {
            String dString = d.getDebtor().getFirstName() + " owes " + d.getCreditor().getFirstName() + " "
                    + df.format(d.getAmount());
            HBox row = new HBox();
            row.setSpacing(dString.length() + 100);
            Button receivedButton = new Button("Mark Received");
            receivedButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    server.deleteDebt(d);
                    debts.remove(d);
                    debtBox.getChildren().clear();
                    refresh();
                }
            });
            row.getChildren().addAll(new Label(dString), receivedButton);
            debtBox.getChildren().add(row);
        }
    }

    public void goBack() {
        debtBox.getChildren().clear();
        eventCtrl.showEventOverview(event);
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
