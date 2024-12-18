package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//import java.math.RoundingMode;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class SettleDebtsCtrl implements Initializable {
    @FXML
    public AnchorPane background;
    @FXML
    public Label openDebtsLabel;
    @FXML
    public Button backButton;
    @FXML
    public AnchorPane innerPane1;
    @FXML
    public AnchorPane innerPane2;
    private ServerUtilsEvent server;
    private final SplittyCtrl eventCtrl;
    private List<Debt> debts;
    private List<Debt> removed = new ArrayList<>();
    private Event event;
    @FXML
    private ScrollPane openDebtPane;
    @FXML
    private ScrollPane settledPane;
    @FXML
    private VBox openDebtBox;
    @FXML
    private VBox settledBox;
    @FXML
    private Label settledLabel;
    @FXML
    private Label bankAvail;
    @FXML
    private Label bankUnavail;
    @FXML
    private Label give;
    @FXML
    private Label received;
    @FXML
    private Label undo;
    @FXML
    private Label settledDebtsLabel;
    @FXML
    private Label settledDesc;

    @Inject
    public SettleDebtsCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.eventCtrl = eventCtrl;
    }

    public void refresh() {
        openDebtPane.setFitToWidth(true);
        settledPane.setFitToWidth(true);
        openDebtPane.setVisible(true);
        settledPane.setVisible(true);
        settledDebtsLabel.setVisible(true);
        settledLabel.setVisible(false);
        if (debts.isEmpty() && removed.isEmpty()) {
            openDebtBox.getChildren().clear();
            openDebtPane.setVisible(false);
            settledPane.setVisible(false);
            settledDebtsLabel.setVisible(false);
            settledLabel.setVisible(true);
            return;
        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        setOpenDebts(df);
        setRemovedDebts(df);
    }

    private void setOpenDebts(DecimalFormat df) {
        for(Debt d : debts) {
            String dString = d.getDebtor().getFirstName() + " " + d.getDebtor().getLastName() + " " + give.getText()
                    + " " + d.getCreditor().getFirstName() + " " + d.getCreditor().getLastName()
                    + " " + df.format(d.getAmount());
            HBox row = new HBox();
            row.setSpacing(40);
            Button receivedButton = new Button(received.getText());
            receivedButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    removed.add(d);
                    debts.remove(d);
                    settledBox.getChildren().clear();
                    openDebtBox.getChildren().clear();
                    refresh();
                }
            });

            Participant creditor = d.getCreditor();
            Label bankDetails;
            if (creditor.getAccountName() != null) {
                bankDetails = new Label(bankAvail.getText() + creditor.getAccountName() + "\n" +
                        "IBAN: " + creditor.getIban() + "\n" +
                        "BIC: " + creditor.getBic());
            } else {
                bankDetails = new Label(bankUnavail.getText());
            }
            bankDetails.setMinHeight(100);
            bankDetails.setMinWidth(200);
            TitledPane creditorDetails = new TitledPane(dString, bankDetails);
            creditorDetails.setMinWidth(300);
            creditorDetails.setExpanded(false);
            row.getChildren().addAll(creditorDetails, receivedButton);
            openDebtBox.getChildren().add(row);
        }
    }

    private void setRemovedDebts(DecimalFormat df) {
        for (Debt d : removed) {
            String dString = d.getDebtor().getFirstName() + " " + d.getDebtor().getLastName() + " " + give.getText()
                    + " " + d.getCreditor().getFirstName() + " " + d.getCreditor().getLastName()
                    + " " + df.format(d.getAmount());
            HBox row = new HBox();
            row.setSpacing(50);
            Button undoButton = new Button(undo.getText());

            undoButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    removed.remove(d);
                    debts.add(d);
                    settledBox.getChildren().clear();
                    openDebtBox.getChildren().clear();
                    refresh();
                }
            });

            Participant creditor = d.getCreditor();
            Label bankDetails;
            if (creditor.getAccountName() != null) {
                bankDetails = new Label(bankAvail.getText() + creditor.getAccountName() + "\n" +
                        "IBAN: " + creditor.getIban() + "\n" +
                        "BIC: " + creditor.getBic());
            } else {
                bankDetails = new Label(bankUnavail.getText());
            }
            bankDetails.setMinHeight(100);
            bankDetails.setMinWidth(200);
            TitledPane creditorDetails = new TitledPane(dString, bankDetails);
            creditorDetails.setMinWidth(300);
            creditorDetails.setExpanded(false);
            row.getChildren().addAll(creditorDetails, undoButton);
            settledBox.getChildren().add(row);
        }
    }

    public void goBack() {
        for(Debt d : removed) {
            String description = settledDesc.getText().substring(0, settledDesc.getText().length() - 1);
            Expense e = new Expense(description, "EUR", d.getAmount(), Date.valueOf(LocalDate.now()));
            Set<ExpenseParticipant> debtors = new HashSet<>();
            debtors.add(new ExpenseParticipant(e, d.getDebtor(), 0, true));
            debtors.add(new ExpenseParticipant(e, d.getCreditor(), 100, false));
            e.setDebtors(debtors);
            server.addExpense(e, event);
            event = server.getByID(event.getId());
        }
        server.send("/app/updated",event);
        removed = new ArrayList<>();
        settledBox.getChildren().clear();
        openDebtBox.getChildren().clear();
        eventCtrl.showEventOverview(event);
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Main.isContrastMode()) {
            background.setStyle("-fx-background-color: #69e0ab;");
            backButton.setStyle(Main.changeUI(backButton));
            Main.buttonFeedback(backButton);
            settledPane.setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF;" +
                    "-fx-border-width: 2.5; -fx-border-insets: -2;-fx-control-inner-background:#211951");
            innerPane2.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF;" +
                    "-fx-border-width: 2.5; -fx-border-insets: -2;-fx-control-inner-background:#211951");
            openDebtPane.setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF;" +
                    "-fx-border-width: 2.5; -fx-border-insets: -2;-fx-control-inner-background:#211951");
            innerPane1.setStyle("-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF;" +
                    "-fx-border-width: 2.5; -fx-border-insets: -2;-fx-control-inner-background:#211951");
            settledLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            openDebtsLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
            settledDebtsLabel.setStyle("-fx-text-fill: black;-fx-font-weight: bolder;");
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                //setOpenDebts();
                break;
            case ESCAPE:
                goBack();
                break;
            default:
                break;
        }
    }
}
