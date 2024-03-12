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
        var add = FXML.load(AddEventCtrl.class, "client", "scenes", "AddEvent.fxml");
        //var modify = FXML.load(ModifyEventCtrl.class, "client","scenes", "ModifyEvent.fxml");
        var eventOverview = FXML.load(EventOverviewNewCtrl.class, "client", "scenes", "EventOverviewNew.fxml");
        var addExp = FXML.load(AddExpenseCtrl.class, "client", "scenes", "AddExpense.fxml");

        var mainCtrl = INJECTOR.getInstance(SplittyCtrl.class);
        mainCtrl.initialize(primaryStage);
        mainCtrl.initShowOverview(overview);
        mainCtrl.initAdd(add);
        mainCtrl.initExp(addExp);
       // mainCtrl.initModify(modify);
        mainCtrl.initEventOverview(eventOverview);
    }
}