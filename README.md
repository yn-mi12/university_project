# OOPP TEAM 49 - Splitty Instructions
-
Startup: 
-
- To start the server you can either run it through the IntelliJ interface(Main from server) or through Gradle(bootRun).
- To start a client instance you can either run it through the IntelliJ interface(Main from Client, make sure JavaFx is properly set up in the VM arguments) or through Gradle(run).
- Use Gradle(run) for multiple clients.
- The server URl can either be changed from the config.yml file (from root directory) or inside the application.

Features:
-
StartScreen:

- To select your preferred language (English/Dutch/Romanian), you can use the language ComboBox at the bottom right of the StartScreen. This feature is also available when viewing an Event or in the AdminOverview.
- If you want to use the High Contrast Mode, press the button at the bottom right of the StartScreen. 
- To create an Event, enter the desired title in the specified textfield on the StartScreen and press the button Create Event.
- To join an Event, enter the invite code in the specified textfield on the StartScreen and press the button Join Event.
- The StartScreen also shows the past events that you created/joined, which are persisted on your local machine. (in the config file)
- To view a past event, select one from the Past Event ListView and press Show Event.
- To enter the AdminOverview, press the Here button at the bottom of the StartScreen and insert the token. (This token can be found in the Server console)
- To change the URL of the server, press the Set Server button at the bottom of the StartScreen and insert your desired Splitty Server. Please make sure that the URL starts with 'http://' and has a '/' at the end.
- To download an empty language template, press the plus Button that is next to the language choice ComboBox.

EventOverview:

- To go back to the StartScreen, press the left arrow Button at the top left of the EventOverviewScreen.
- To copy the invite code of the Event, press the clipboard Button at the top of the EventOverviewScreen.
- To edit the title of the Event, press the Edit Title Button at the top right of the EventOverviewScreen and insert the new Title.
- To add a Participant, press the plus Button and fill in the details of the participant.
- To edit/delete a participant, press the pencil Button, which will show the EditPartcipantScreen.
- To add an expense, select a participant from the Participants ComboBox and press Add.
- To see the minimum debts of all participants, press the Settle Debts Button.
- To view/edit an expense, select the desired expense in the Expenses ListView and press the View/Edit Button.
- To delete an expense, select the desired expense in the Expenses ListView and press the Delete Button.
- To filter expenses, select the desired Participant and use the All, From and Including Tab Panes.
- To delete an Event, press the Trash Button at the bottom left of the EventOverviewScreen.

Add/Edit Expense:

- The Participant who paid for the expense can be selected at the top of the Screen using the ComboBox.
- The description and amount can also be set, as well as the currency.
- The date is using the MM/DD/YYYY format.
- There are 2 options for splitting the expense: either check the first checkbox if all participants are part of the expense, or check the second checkbox and use CTRL CLICK or SHIFT CLICK to select the desired participants.
- To make a transfer between 2 people/to partially settle a debt, check the second checkbox and only select the participant who has to send the money.

Settle Debts:

- To see the bank details of the receiver, press on the debt to open a dropdown menu that shows all information, if it exists.
- To settle a debt, press the corresponding Mark Received Button.
- To undo a settled debt in case of an error, press the corresponding Undo Button. The undo option is not available anymore, if you go back and come back to the Settle Debts screen.

AdminOverview:

- To sort events by Title, Invite Code, Created Date and Last Updated Date, press the corresponding column headers.
- To see an event, select it from the TableView and press Show Event.
- To delete an event, select it from the TableView and press Delete Event.
- To export an event, select it from the TableView and press Export Event.
- To import an event, press Import Event and select a JSON file corresponding to an event.

Technical Features:
- websockets -> Almost everywhere inside the app. (They also use the REST Endpoints)
- long polling -> StartScreen past events
- config
- RestController/Controller/Services

