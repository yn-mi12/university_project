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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Background;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.*;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private static Stage primaryStage;
    private static SplittyCtrl mainCtrl;
    private static boolean contrastMode = false;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        mainCtrl = INJECTOR.getInstance(SplittyCtrl.class);
        start();
    }

    /**
     * Reloads the UI with new resource definitions, like language.
     */
    public static void reloadUI() {
        reload();
        mainCtrl.display();
    }

    /**
     * Called when the language is switched in the event overview
     * @param selectedEvent - The event that is currently being viewed
     */
    public static void reloadUIEvent(Event selectedEvent) {
    reload();
        mainCtrl.showEventOverview(selectedEvent);
    }

    public static void refreshAdminOverview()
    {
        reload();
        mainCtrl.showAdminOverview();
    }

    public static void start(){
        reload();
        var adminOverview =FXML.load(AdminOverviewCtrl.class, "client", "scenes", "AdminOverview.fxml");
        adminOverview.getKey().launch();
        mainCtrl.display();
    }

    public static void reload(){
        var overview = FXML.load(StartScreenCtrl.class, "client", "scenes", "StartScreen.fxml");
        var eventOverview = FXML.load(EventOverviewCtrl.class, "client", "scenes", "EventOverview.fxml");
        var invite = FXML.load(InvitationCtrl.class, "client", "scenes", "Invitation.fxml");
        var partOverview = FXML.load(EditParticipantOverviewCtrl.class, "client", "scenes", "EditParticipantOverview.fxml");
        var addExp = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");
        var editTitle = FXML.load(EditEventTitleCtrl.class,"client", "scenes", "EditTitle.fxml");
        var addParticipant = FXML.load(AddParticipantCtrl.class, "client", "scenes", "AddParticipant.fxml");
        var adminPopup = FXML.load(AdminPopupCtrl.class,"client", "scenes", "AdminPopup.fxml");
        var adminOverview = FXML.load(AdminOverviewCtrl.class, "client", "scenes", "AdminOverview.fxml");
        var settleDebts = FXML.load(SettleDebtsCtrl.class, "client", "scenes", "SettleDebts.fxml");
        mainCtrl.initialize(primaryStage);
        mainCtrl.initAdminPopup(adminPopup);
        mainCtrl.initShowOverview(overview);
        mainCtrl.initPartUpdate(partOverview);
        mainCtrl.initEventOverview(eventOverview);
        mainCtrl.initInvitePage(invite);
        mainCtrl.initExp(addExp);
        mainCtrl.initEditTitle(editTitle);
        mainCtrl.initAddParticipant(addParticipant);
        mainCtrl.initAdminOverview(adminOverview);
        mainCtrl.initSettleDebts(settleDebts);
    }

    public static void changeContrast()
    {
        contrastMode = (!contrastMode);
        reloadUI();
    }

    public static boolean isContrastMode() {
        return contrastMode;
    }
    public static String changeUI(Object o)
    {
        if(o.getClass() == Button.class || o.getClass() == Text.class)
            return "-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1";
        if(o.getClass() == TextField.class)
            return "-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder; -fx-prompt-text-fill: #BDBDBD; -fx-border-color: #211951;";
        if(o.getClass() == ComboBox.class || o.getClass() == ToggleButton.class)
            return "-fx-background-color: #211951; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                    "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 2.5; -fx-border-insets: -2";
        if(o.getClass() == Label.class)
            return "-fx-text-fill: #ff0000;-fx-font-weight: bolder;";
        return "";
    }

    public static void buttonFeedback(Button o)
    {
        o.setOnMouseEntered(e -> o.setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;"+
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1;"));
        o.setOnMouseExited(e -> o.setStyle(Main.changeUI(o)));
        o.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                o.setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                        "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 1.5; -fx-border-insets: -1;");
            }
            else o.setStyle(Main.changeUI(o));
        });
    }

    public static void languageFeedback(ComboBox<Label> o)
    {
        o.setOnMouseEntered(e -> o.setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 2.5; -fx-border-insets: -2"));
        o.setOnMouseExited(e -> o.setStyle(Main.changeUI(o)));
        o.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                o.setStyle("-fx-background-color: #836FFF; -fx-text-fill: #F0F3FF;-fx-font-weight: bolder;" +
                        "-fx-border-color: #836FFF; -fx-border-radius: 20; -fx-background-radius:20; -fx-border-width: 2.5; -fx-border-insets: -2");
            }
            else o.setStyle(Main.changeUI(o));
        });
    }

}