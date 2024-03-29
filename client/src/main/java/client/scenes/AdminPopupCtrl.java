package client.scenes;

import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class AdminPopupCtrl {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;
    public TextField insertedToken;

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
            if(server.checkToken(insertedToken.getText())) {
                controller.setAdmin(true);
                controller.showAdminOverview();
            }
            else System.out.println("NO!");
            insertedToken.clear();
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
