# Week 7 meeting agenda

| Key          | Value              |
|--------------|--------------------|
| Date:        | 2024-03-26         |
| Time:        | 15:45 - 16:30      |
| Location:    | DW PC1 Cubicle 13  |
| Chair        | Daniel Dumitru     |
| Minute Taker | Jake Nijssen       |
| Attendees:   | Ioana-Lisandra Draganescu, TA<br/> Daniel Dumitru</br> Yana Mihaylova<br/> Jake Nijssen<br/> Ipshit Raychaudhuri<br/> Kasparas Savickis<br/>  Maria Zmpainou |


## Agenda Items:
- Opening by chair (1 min)
- Check -in: How is everyone doing? (2 min)
- Announcements by the team (2 min)
- Approval of the agenda - Does anyone have any additions? (1 min)
- Approval of last minutes - Did everyone read the minutes from the previous meeting? (1 min)
---
- Announcements by the TA (3 min)
- Presentation of the current app to TA (2 min)
---
- Talking Points: (Inform/ brainstorm/ decision-making/ discuss):
    - Discuss how to implement the Foreign Currency System. (5 min)
    - UI:
        - Decide on a *final* UI style. (7 min)
        - Differentiating between admin and user. (4 min)
    - Test coverage regarding the scenes(fxml). (2 min)
    - How to resolve the user authentification problem? (2 min)
    - Where and how to implement Long Polling and Web Sockets? (4 min)
    - Next meeting: time and location (2 min)
- Summarize action points: Who , what , when? (2 min)
---
- Feedback round: What went well and what can be improved next time? (1 min)
- Planned meeting duration != actual duration? Where/why did you mis -estimate? (2 min)
- Question round: Does anyone have anything to add before the meeting closes? (1 min)
- Closure (1 min)

## Issues Discussed
- Product draft & presentation to be added to the agenda
- Ipshit found Dan's minutes great
- Features feedback at the end of the week, make sure that the main branch is functioning by the beginning of the weekend
- Yana shows the general working of the app to the TA
    - We discussed that the invite code cannot be copied, to be fixed
    - Ipshit mentioned that we do not have a proper debt system yet
    - Kasparas mentioned that the in the current system you cannot delete the first event, to be fixed
- What everyone has done last week
    - Jake has fixed a database bug, and started on Admin frontend UI
    - Kasparas made the final structure of the database
    - Yana put allot of effort into fixing the endpoints
    - Ipshit fixed 2 major bugs: language switching put you back to another screen and database got deleted and fixed crash + increased the test coverage
    - Dan worked on endpoints
    - Maria added endpoints for editing the title of an event + the frontend UI for that feature.
- Talking points:
    - Foreign currency system
        - Should we use exchange rate at date of expense creation instead of calculating at the end
        - Store the date at which the expense is paid, use that date during the settlements to calculate the conversion
    - UI
        - UI should be decided during this meeting.
        - Choose the colours to make sure it contrasts at the beginning
        - We chose to use google's Roboto font
        - Decided on font size for different elements(Titles, buttons, labels etc) so it is uniform accross all scenes
            - Title: 16/17px
            - Labels/Buttons: 13/14px
        - Responsive UI is not required(maybe?)
            - Message the TA for validation
            - Language switching should not mess with sizing(i.e. it making everything unaligned) **NOT A PRIORITY**
        - Text should be high contrast (i.e. dark background should contain light text and vice versa)
        - Agreed on the pallette
            - #211951
            - #836FFF
            - #15F5BA
            - #F0F3FF
        - Text the TA about clarification on undo buttons
        - Admin should get its own start screen, where all the events are shown
    - Websockets & long Polling
        - We have to use both
        - Use websockets for synchronization on events for if 2 users are changing stuff for it
        - Use long polling for entire past events overview
    - Test coverage
        - JavaFX scene test coverage is lacking at the moment, should be worked on
    - Security system now doesn't block H2 access anymore
- Provide the draft for your presentation this week if you want feedback
- Next meeting will be before the TA meeting week 8
        
## Final Notes
This meeting was a bit chaotic due to the many issues and questions that needed to be covered, as such it finished with 1 minute left. Furthermore the meeting was productive and will help push the project further.
