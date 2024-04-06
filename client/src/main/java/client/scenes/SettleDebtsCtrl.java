package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class SettleDebtsCtrl {

    private ServerUtilsEvent server;
    private final SplittyCtrl eventCtrl;
    private List<Debt> creditorDebts;
    private List<Debt> debtorDebts;
    private Event event;
    @FXML
    private ScrollPane creditPane;
    @FXML
    private ScrollPane debtPane;
    @FXML
    private VBox creditorBox;
    @FXML
    private VBox debtorBox;
    @FXML
    private Label settledLabel;
    @FXML
    private Label groupPay;
    @FXML
    private Label partPay;
    @FXML
    private Label bankAvail;
    @FXML
    private Label bankUnavail;
    @FXML
    private Label give;
    @FXML
    private Label received;

    @Inject
    public SettleDebtsCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.eventCtrl = eventCtrl;
    }

    public void refresh() {
        groupPay.setVisible(true);
        partPay.setVisible(true);
        creditPane.setFitToWidth(true);
        debtPane.setFitToWidth(true);
        creditPane.setVisible(true);
        debtPane.setVisible(true);
        settledLabel.setVisible(false);
        if(creditorDebts.isEmpty() && debtorDebts.isEmpty()) {
            creditorBox.getChildren().clear();
            groupPay.setVisible(false);
            partPay.setVisible(false);
            creditPane.setVisible(false);
            debtPane.setVisible(false);
            settledLabel.setVisible(true);
            return;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        setCreditors(df);
        setDebtors(df);
    }

    private void setCreditors(DecimalFormat df) {
        double groupAmount = 0;
        for(Debt d : creditorDebts) {
            String dString = d.getDebtor().getFirstName() + " " + give.getText() + " " + d.getCreditor().getFirstName()
                    + " " + df.format(d.getAmount());
            HBox row = new HBox();
            row.setSpacing(dString.length() + 75);
            Button receivedButton = new Button(received.getText());
            receivedButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    server.deleteDebt(d);
                    creditorDebts.remove(d);
                    creditorBox.getChildren().clear();
                    refresh();
                }
            });

            Participant creditor = d.getCreditor();
            Label bankDetails;
            if(creditor.getAccountName() != null) {
                bankDetails = new Label(bankAvail.getText() + creditor.getAccountName() + "\n" +
                        "IBAN: " + creditor.getIban() + "\n" +
                        "BIC: " +creditor.getBic());
            } else {
                bankDetails = new Label(bankUnavail.getText());
            }

            TitledPane creditorDetails = new TitledPane(dString, bankDetails);
            creditorDetails.setExpanded(false);
            row.getChildren().addAll(creditorDetails, receivedButton);
            creditorBox.getChildren().add(row);

            groupAmount += d.getAmount();
        }
        String text = groupPay.getText().replaceAll("[0-9]","").replace(".", "");
        if(text.charAt(text.length() - 1) == ' ')
            groupPay.setText(text + df.format(groupAmount));
        else groupPay.setText(text + " " + df.format(groupAmount));
    }

    private void setDebtors(DecimalFormat df) {
        double partAmount = 0;
        for(Debt d : debtorDebts) {
            String dString = d.getDebtor().getFirstName() + " " + give.getText() + " " + d.getCreditor().getFirstName()
                    + " " + df.format(d.getAmount());
            HBox row = new HBox();
            row.setSpacing(dString.length() + 75);
            Button receivedButton = new Button(received.getText());
            receivedButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    server.deleteDebt(d);
                    debtorDebts.remove(d);
                    debtorBox.getChildren().clear();
                    refresh();
                }
            });

            Participant creditor = d.getCreditor();
            Label bankDetails;
            if(creditor.getAccountName() != null) {
                bankDetails = new Label(bankAvail.getText() + creditor.getAccountName() + "\n" +
                        "IBAN: " + creditor.getIban() + "\n" +
                        "BIC: " +creditor.getBic());
            } else {
                bankDetails = new Label(bankUnavail.getText());
            }

            TitledPane creditorDetails = new TitledPane(dString, bankDetails);
            creditorDetails.setExpanded(false);
            row.getChildren().addAll(creditorDetails, receivedButton);
            debtorBox.getChildren().add(row);

            partAmount += d.getAmount();
        }

        String text = partPay.getText().replaceAll("[0-9]","").replace(".", "");
        if(text.charAt(text.length() - 1) == ' ')
            partPay.setText(text + df.format(partAmount));
        else partPay.setText(text + " " + df.format(partAmount));
    }

    public void goBack() {
        creditorBox.getChildren().clear();
        debtorBox.getChildren().clear();
        eventCtrl.showEventOverview(event);
    }

    public void setCreditorDebts(List<Debt> creditorDebts) {
        this.creditorDebts = creditorDebts;
    }

    public void setDebtorDebts(List<Debt> debtorDebts) { this.debtorDebts = debtorDebts; }

    public void setEvent(Event event) {
        this.event = event;
    }
}
