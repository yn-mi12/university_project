package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminPopupCtrl implements Initializable {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    @FXML
    public TextField insertedToken;
    @FXML
    public Label incorrectToken;
    @FXML
    public AnchorPane background;
    @FXML
    public Text enterAdminLabel;
    @FXML
    public Button backButton;
    @FXML
    public Button loginButton;

    @Inject
    public AdminPopupCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }
    public void cancel() {
        controller.showOverview();
    }

    public void login() {
        try {
            if (!Objects.equals(insertedToken.getText(), "")&&server.checkToken(insertedToken.getText())) {
                controller.setAdmin(true);
                Main.refreshAdminOverview();
                controller.showAdminOverview();
                insertedToken.clear();
                incorrectToken.visibleProperty().setValue(false);
            } else {
                incorrectToken.visibleProperty().setValue(true);
            }
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        insertedToken.clear();
        if(Main.isContrastMode()){
            background.setStyle("-fx-background-color: #69e0ab;");
            backButton.setStyle(Main.changeUI(backButton));
            Main.buttonFeedback(backButton);
            loginButton.setStyle(Main.changeUI(loginButton));
            Main.buttonFeedback(loginButton);
            enterAdminLabel.setStyle(Main.changeUI(enterAdminLabel));
            insertedToken.setStyle(Main.changeUI(insertedToken));
            incorrectToken.setStyle(Main.changeUI(incorrectToken));
        }
    }
}
