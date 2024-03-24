package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import commons.dto.EventDTO;
import commons.dto.ExpenseDTO;
import commons.dto.ParticipantDTO;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class AddExpenseCtrl implements Initializable {
    private EventOverviewNewCtrl ctrl;
    private EventDTO event;
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private List<ParticipantDTO> participants;
    private ParticipantDTO expensePayer;
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
    private TextArea tags;
    @FXML
    private ChoiceBox<String> language = new ChoiceBox<>();

    @Inject
    public AddExpenseCtrl(ServerUtilsEvent server, SplittyCtrl ctrl) {
        this.controller = ctrl;
        this.server = server;
    }

    public void setEvent(ParticipantDTO paid, EventOverviewNewCtrl ctrl) {
        expensePayer = paid;
        this.ctrl = ctrl;
        this.event = ctrl.getSelectedEvent();
        this.participants = event.getParticipants();
        ObservableList<MenuItem> names = FXCollections.observableArrayList();
        HashMap<MenuItem,ParticipantDTO> map = new HashMap<>();

        for (ParticipantDTO p : participants) {
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
        controller.showEventOverview(ctrl.getSelectedEvent());
    }

    public void ok() {
        try {
            System.out.println("Add expense");
            System.out.println("Id:" + event.getId());
            server.addExpense(getExpense());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        controller.showEventOverview(ctrl.getSelectedEvent());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Removed this because we don't need to have language switching in the Expense overview
    }

//    private void retrieveCurrencies() {
//        //List<String> values = List.of("USD", "EUR", "CHF");
//        //Label label = new Label();
//        currency = new ChoiceBox<>();
//        currency.getSelectionModel().selectFirst();
//        //currency.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
////            @Override
////            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
////                label.setText(values.get(0));
////            }
////        });
//    }

    public ExpenseDTO getExpense() {
        System.out.println("Get expense");
        var description = this.whatFor.getText();
        var amount = howMuch.getText();
        var currency = this.currency.getValue();
        var date = this.date.getValue();
        //var tags = this.tags.getText();
        ExpenseDTO result = new ExpenseDTO(description, currency, expensePayer,
                Double.parseDouble(amount), java.sql.Date.valueOf(date));
        System.out.println(result.toString());
        return result;
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

}
