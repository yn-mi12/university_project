package client.scenes;

import commons.Event;
import commons.Participant;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.Pair;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.application.Platform;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class SplittyCtrlTest {

    private SplittyCtrl splittyCtrl;
    private StartScreenCtrl startScreenCtrl;
    private EventOverviewCtrl eventOverviewCtrl;
    private InvitationCtrl invitationCtrl;
    private AddExpenseCtrl addExpenseCtrl;
    private EditEventTitleCtrl editEventTitleCtrl;
    private AddParticipantCtrl addParticipantCtrl;

    @Start
    private void start(Stage primaryStage) {
        splittyCtrl = new SplittyCtrl();
        splittyCtrl.initialize(primaryStage);

        startScreenCtrl = mock(StartScreenCtrl.class);
        eventOverviewCtrl = mock(EventOverviewCtrl.class);
        invitationCtrl = mock(InvitationCtrl.class);
        addExpenseCtrl = mock(AddExpenseCtrl.class);
        editEventTitleCtrl = mock(EditEventTitleCtrl.class);
        addParticipantCtrl = mock(AddParticipantCtrl.class);

        splittyCtrl.initShowOverview(new Pair<>(startScreenCtrl, new StackPane()));
        splittyCtrl.initEventOverview(new Pair<>(eventOverviewCtrl, new StackPane()));
        splittyCtrl.initInvitePage(new Pair<>(invitationCtrl, new StackPane()));
        splittyCtrl.initExp(new Pair<>(addExpenseCtrl, new StackPane()));
        splittyCtrl.initEditTitle(new Pair<>(editEventTitleCtrl, new StackPane()));
        splittyCtrl.initAddParticipant(new Pair<>(addParticipantCtrl, new StackPane()));
    }

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }
    
    @BeforeEach
    public void setUp() {
        reset(startScreenCtrl, eventOverviewCtrl, invitationCtrl, addExpenseCtrl,
                editEventTitleCtrl, addParticipantCtrl);
    }

    @Test
    public void testShowDisplay() {
        doNothing().when(startScreenCtrl).refresh();

        Platform.runLater(() -> {
            splittyCtrl.display();
            verify(startScreenCtrl).refresh();
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Splitty", splittyCtrl.getPrimaryStage().getTitle());
        });
    }

    @Test
    public void testShowOverview() {
        doNothing().when(startScreenCtrl).refresh();

        Platform.runLater(() -> {
            splittyCtrl.showOverview();
            verify(startScreenCtrl).refresh();
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Splitty", splittyCtrl.getPrimaryStage().getTitle());
        });
    }

    @Test
    public void testShowEventOverview() {
        Platform.runLater(() -> {
            eventOverviewCtrl.eventTitle = new Label();
            splittyCtrl.showEventOverview(new Event("test"));
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Event: test", splittyCtrl.getPrimaryStage().getTitle());
        });
    }

    @Test
    public void testShowInvitePage() {
        Platform.runLater(() -> {
            invitationCtrl.eventInviteTitle = new Text();
            invitationCtrl.inviteCode = new Text();
            splittyCtrl.showInvitePage(new Event("test"));
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Event: test", splittyCtrl.getPrimaryStage().getTitle());
        });
    }

    @Test
    public void testShowExpOverview() {
        Platform.runLater(() -> {
            splittyCtrl.showExpOverview();
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Add/Edit expense", splittyCtrl.getPrimaryStage().getTitle());
        });
    }

    @Test
    public void testShowEditTitle() {
        Platform.runLater(() -> {
            editEventTitleCtrl.oldTitle = new Label();
            splittyCtrl.showEditTitle(new Event("test"));
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Edit Title", splittyCtrl.getPrimaryStage().getTitle());
        });
    }

    @Test
    public void testShowAddParticipant() {
        Platform.runLater(() -> {
            splittyCtrl.showAddParticipant(new Event("test"));
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Add participant", splittyCtrl.getPrimaryStage().getTitle());
        });
    }

    @Test
    public void testInitExpShowOverview() {
        Platform.runLater(() -> {
            splittyCtrl.initExpShowOverview(new Event("test"), new Participant("a", "b"));
            assertNotNull(splittyCtrl.getPrimaryStage().getScene());
            assertEquals("Add/Edit expense", splittyCtrl.getPrimaryStage().getTitle());
        });
    }
}
