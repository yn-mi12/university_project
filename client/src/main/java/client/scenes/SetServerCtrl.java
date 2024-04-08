package client.scenes;

import client.Config;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SetServerCtrl {

    private final SplittyCtrl controller;
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
}
