package client.scenes;

import client.Config;
import client.Main;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class EditParticipantOverviewCtrl implements Initializable {
    private Participant selectedParticipant;
    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    private Event event;
    @FXML
    public ListView<String> participantList;
    @FXML
    public ComboBox<Label> languageBox;
    private Stage primaryStage;

    @FXML
    private Label noDeleteParticipant;

    @Inject
    public EditParticipantOverviewCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }

    public void setEvent(Event event) {
        this.event = event;
        List<Participant> participants = server.getEventParticipants(event);
        List<String> names = new ArrayList<>();
        for (Participant x : participants) {
            names.add(x.getId() + ": " + x.getFirstName());
        }
        participantList.setItems(FXCollections.observableList(names));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Label> x = FXCollections.observableArrayList();
        List<Config.SupportedLocale> languages = Config.get().getSupportedLocales().stream().toList();
        for (var item : languages) {
            Image icon;
            String iconPath = "client/images/" + item.getCode() + ".png";
            icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(iconPath)));
            ImageView iconImageView = new ImageView(icon);
            iconImageView.setFitHeight(25);
            iconImageView.setPreserveRatio(true);
            x.add(new Label(item.getName(), iconImageView));
        }
        languageBox.setItems(x);
        languageBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Label> call(ListView<Label> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Label item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                        } else {
                            item.setTextFill(Color.color(0, 0, 0));
                            setGraphic(item);
                        }
                    }
                };
            }
        });
        String current = Config.get().getCurrentLocaleName();
        languageBox.setValue(languageBox.getItems().stream()
                .filter(l -> String.valueOf(l.getText()).equals(current)).findFirst().orElse(null));
        languageBox.getSelectionModel().selectedItemProperty().addListener(((obs, oldVal, newVal) -> {
            if (newVal != null) {
                Config.get().setCurrentLocale(newVal.getText());
                Config.get().save();
                Main.refreshAdminOverview();
            }
        }));
    }

    public void clearFields() {
        participantList.getSelectionModel().clearSelection();
    }

    public void setParticipant() {
        String partIdName = participantList.getSelectionModel().getSelectedItem();
        String partId = partIdName.split(":")[0];
        this.selectedParticipant = server.getParticipantByID(Long.parseLong(partId));
    }

    public void editParticipant() {
        setParticipant();
        try {
            controller.showAddParticipant(event);
            AddParticipantCtrl addCtrl = controller.getAddParticipantCtrl();
            addCtrl.setParticipant(selectedParticipant);
            addCtrl.ok();

        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void cancel() {
        clearFields();
        selectedParticipant = null;
        Main.reloadUIEvent(event);
        controller.showEventOverview(event);
        noDeleteParticipant.visibleProperty().setValue(false);
    }

    public void deleteParticipant() {
        setParticipant();
        try {
            System.out.println("Delete Participant");
            if (!checkParticipantInExpenses()){
                server.deleteParticipant(server.getParticipantByID(selectedParticipant.getId()));
                event.deleteParticipant(selectedParticipant);
                noDeleteParticipant.visibleProperty().setValue(false);
                cancel();
            }else{
                noDeleteParticipant.visibleProperty().setValue(true);
            }

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public boolean checkParticipantInExpenses(){
        List<Participant> participantsInExpenses = new ArrayList<>();
        for (Expense expense : event.getExpenses()){
            Set<ExpenseParticipant> debtors = expense.getDebtors();
            List<ExpenseParticipant> debtorsList = new ArrayList<>(debtors);
            for (ExpenseParticipant expenseParticipant : debtorsList) {
                participantsInExpenses.add(expenseParticipant.getParticipant());
            }
        }
        for (Participant participant: participantsInExpenses){
            if (selectedParticipant.getFirstName().equals(participant.getFirstName())){
                return true;
            }
        }
        return false;
    }

    public void HideLabel() {
        noDeleteParticipant.visibleProperty().setValue(false);
    }
}

