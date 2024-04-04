package client.scenes;

import com.google.inject.Inject;
import commons.Debt;
import commons.Event;

import java.util.List;

public class SettleDebtsCtrl {

    private final SplittyCtrl eventCtrl;
    private List<Debt> debts;
    private Event event;

    @Inject
    public SettleDebtsCtrl(SplittyCtrl eventCtrl) {
        this.eventCtrl = eventCtrl;
    }

    public void refresh() {

    }

    public void goBack() {
        eventCtrl.showEventOverview(event);
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
