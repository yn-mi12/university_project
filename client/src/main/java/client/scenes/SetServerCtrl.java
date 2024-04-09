package client.scenes;

import client.Config;
import client.Main;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SetServerCtrl implements Initializable {

    private final SplittyCtrl controller;
    @FXML
    public AnchorPane background;
    @FXML
    public Button backButton;
    @FXML
    public Text serverUrlLabel;
    @FXML
    public Button setButton;
    @FXML
    private TextField serverUrl;
    @FXML
    private Label emptyLabel;
    @FXML
    private Label setLabel;

    @Inject
    public SetServerCtrl(SplittyCtrl controller) {
        this.controller = controller;
    }

    public void setServerUrl() {
        String url = serverUrl.getText();
        if(url.isEmpty()) {
            emptyLabel.setVisible(true);
            return;
        }
        emptyLabel.setVisible(false);
        setLabel.setVisible(true);
        Config.get().setHost(url);
        Config.get().save();
    }

    public void goBack() {
        controller.showOverview();
        emptyLabel.setVisible(false);
        setLabel.setVisible(false);
        serverUrl.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Main.isContrastMode())
        {
            background.setStyle("-fx-background-color: #69e0ab;");
            backButton.setStyle(Main.changeUI(backButton));
            Main.buttonFeedback(backButton);
            setButton.setStyle(Main.changeUI(setButton));
            Main.buttonFeedback(setButton);
            serverUrl.setStyle(Main.changeUI(serverUrl));
            emptyLabel.setStyle(Main.changeUI(emptyLabel));
            serverUrlLabel.setStyle(Main.changeUI(serverUrlLabel));
        }
    }
}
