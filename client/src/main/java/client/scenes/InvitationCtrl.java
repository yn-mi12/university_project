package client.scenes;

import client.utils.ServerUtilsEvent;
import commons.Event;
import jakarta.inject.Inject;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;


import static jakarta.ws.rs.core.Response.ok;

public class InvitationCtrl {

    private final ServerUtilsEvent server;
    private final SplittyCtrl mainCtrl;
    public Text eventInviteTitle;

    @Inject
    public InvitationCtrl(ServerUtilsEvent server, SplittyCtrl mainCtrl)
    {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setSelectedEvent(Event selectedEvent) {
        //TO DO
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

    private void cancel() {
    }
}
