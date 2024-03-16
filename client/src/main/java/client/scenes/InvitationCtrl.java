package client.scenes;

import client.utils.ServerUtilsEvent;
import jakarta.inject.Inject;

public class InvitationCtrl {

    private final ServerUtilsEvent server;
    private final SplittyCtrl mainCtrl;

    @Inject
    public InvitationCtrl(ServerUtilsEvent server, SplittyCtrl mainCtrl)
    {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
}
