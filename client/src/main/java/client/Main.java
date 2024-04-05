/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import com.google.inject.Injector;
import commons.Event;
import javafx.application.Application;
import javafx.stage.Stage;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        reloadUI();
    }

    /**
     * Reloads the UI with new resource definitions, like language.
     */
    public static void reloadUI() {
        var overview = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        Main.primaryStage.setOnCloseRequest(e -> overview.getKey().stop());
        var eventOverview = FXML.load(EventOverviewCtrl.class, "client", "scenes", "EventOverview.fxml");
        var invite = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var partOverview = FXML.load(EditParticipantOverviewCtrl.class, "client", "scenes", "EditParticipantOverview.fxml");
        var addExp = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var editTitle = FXML.load(EditEventTitleCtrl.class,"client", "scenes", "EditTitle.fxml");
        var addParticipant = FXML.load(AddParticipantCtrl.class, "client", "scenes", "AddParticipant.fxml");
        var mainCtrl = INJECTOR.getInstance(SplittyCtrl.class);
        var adminPopup = FXML.load(AdminPopupCtrl.class,"client", "scenes", "AdminPopup.fxml");
        var adminOverview =FXML.load(AdminOverviewCtrl.class, "client", "scenes", "AdminOverview.fxml");
        mainCtrl.initialize(primaryStage);
        mainCtrl.initAdminPopup(adminPopup);
        mainCtrl.initShowOverview(overview);
        mainCtrl.initPartUpdate(partOverview);
        mainCtrl.initEventOverview(eventOverview);
        mainCtrl.initInvitePage(invite);
        mainCtrl.initExp(addExp);
        mainCtrl.initEditTitle(editTitle);
        mainCtrl.initAddParticipant(addParticipant);
        mainCtrl.initializeAdminOverview(adminOverview);
        mainCtrl.display();
    }

    /**
     * Called when the language is switched in the event overview
     * @param selectedEvent - The event that is currently being viewed
     */
    public static void reloadUIEvent(Event selectedEvent) {
        var overview = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var eventOverview = FXML.load(EventOverviewCtrl.class, "client", "scenes", "EventOverview.fxml");
        var partOverview = FXML.load(EditParticipantOverviewCtrl.class, "client", "scenes", "EditParticipantOverview.fxml");
        var invite = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var addExp = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var editTitle = FXML.load(EditEventTitleCtrl.class, "client", "scenes", "EditTitle.fxml");
        var addParticipant = FXML.load(AddParticipantCtrl.class, "client", "scenes", "AddParticipant.fxml");
        var mainCtrl = INJECTOR.getInstance(SplittyCtrl.class);
        var adminPopup = FXML.load(AdminPopupCtrl.class,"client", "scenes", "AdminPopup.fxml");
        mainCtrl.initialize(primaryStage);
        mainCtrl.initPartUpdate(partOverview);
        mainCtrl.initAdminPopup(adminPopup);
        mainCtrl.initShowOverview(overview);
        mainCtrl.initEventOverview(eventOverview);
        mainCtrl.initInvitePage(invite);
        mainCtrl.initExp(addExp);
        mainCtrl.initEditTitle(editTitle);
        mainCtrl.initAddParticipant(addParticipant);
        mainCtrl.showEventOverview(selectedEvent);
    }

    public static void refreshAdminOverview()
    {
        var adminOverview =FXML.load(AdminOverviewCtrl.class, "client", "scenes", "AdminOverview.fxml");
        var mainCtrl = INJECTOR.getInstance(SplittyCtrl.class);
        mainCtrl.initializeAdminOverview(adminOverview);
        mainCtrl.showAdminOverview();
    }

}