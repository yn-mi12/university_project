package client.scenes;

import client.Main;
import client.utils.ServerUtilsEvent;
import com.google.inject.Inject;
import javafx.event.ActionEvent;

public class AdminPopupCtrl {

    private final ServerUtilsEvent server;
    private final SplittyCtrl controller;

    @Inject
    public AdminPopupCtrl(ServerUtilsEvent server, SplittyCtrl eventCtrl) {
        this.server = server;
        this.controller = eventCtrl;
    }
    public void cancel() {
        controller.showOverview();
    }
}
