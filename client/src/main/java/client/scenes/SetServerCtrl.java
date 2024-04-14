package client.scenes;

import client.Config;
import client.Main;
import client.utils.ServerUtilsEvent;
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
    private final ServerUtilsEvent server;
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
    @FXML
    private Label failedLabel;

    @Inject
    public SetServerCtrl(ServerUtilsEvent server, SplittyCtrl controller) {
        this.server = server;
        this.controller = controller;
    }

    public void setServerUrl() {
        failedLabel.setVisible(false);
        String oldurl = Config.get().getHost();
        String url = serverUrl.getText();
        if(url.isEmpty()) {
            emptyLabel.setVisible(true);
            return;
        }
        if(!url.startsWith("http://")) {
            emptyLabel.setVisible(false);
            setLabel.setVisible(false);
            failedLabel.setVisible(true);
            return;
        }
        emptyLabel.setVisible(false);
        setLabel.setVisible(true);
        server.setServer(url);
        if(Config.get().getHost().equals(oldurl) && !oldurl.equals(url)) {
            setLabel.setVisible(false);
            failedLabel.setVisible(true);
        }
    }

    public void goBack() {
        controller.showOverview();
        emptyLabel.setVisible(false);
        setLabel.setVisible(false);
        failedLabel.setVisible(false);
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
            failedLabel.setStyle(Main.changeUI(failedLabel));
            setLabel.setStyle("-fx-text-fill: #04530a;-fx-font-weight: bolder;");
        }
    }
}
