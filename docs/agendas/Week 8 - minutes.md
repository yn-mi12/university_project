# Week 8 meeting agenda

| Key          | Value                                                                                                                                      |
|--------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| Date:        | 2024-04-02                                                                                                                                 |
| Time:        | 15:45 - 16:30                                                                                                                              |
| Location:    | DW PC1 Cubicle 13                                                                                                                          |
| Chair        | Maria Zmpainou                                                                                                                             |
| Minute Taker | Kasparas Savickis                                                                                                                          |
| Attendees:   | Ioana-Lisandra Draganescu, TA<br/> Daniel Dumitru</br> Yana Mihaylova<br/> Ipshit Raychaudhuri<br/> Kasparas Savickis<br/>  Maria Zmpainou |


## Agenda Items:
- Opening by chair (1 min)
- Check -in: How is everyone doing? (2 min)
- Announcements by the team (2 min)
- Approval of the agenda - Does anyone have any additions? (1 min)
- Approval of last minutes - Did everyone read the minutes from the previous meeting? (1 min)
---
- Announcements by the TA (3 min)
  - Consider adding web sockets / long polling as soon as possible (no pressure)

- Presentation of the current app to TA (2 min)
---
- Talking Points: (Inform/ brainstorm/ decision-making/ discuss):
- Talk about any bugs/changes to the current app (7 min)
  - Add confirmation screens/etc
    - Add UNDO buttons
    - admin is enough to be able to delete expense and user can delete one as well
    - Debt - Ipshit on client side
    - Dan - websockets for event overview, recent events long polling Yana
    - Do we need to have other screens like participants update live as well? to be confirmed later
  - Discuss how to handle the debts (5 min)
    - Ipshit: calculate debts on the client from available data
  - Who, where and how to implement Long Polling and Web Sockets? (7 min)
    - Dan - websockets for admin overview
    - Yana - long polling for Event overview
  - Discuss testing - agree on testing everything we merge(?) (5 min)
    - Kasparas - write tests for ExpenseController
    - Remove security code (we don't need it and no one knows it well enough to test it)
    - add creation time/last update time in event 
    - sort events
  - write tests 
  - Next meeting: time and location (2 min) 
    - After current meeting
    
- Summarize action points: Who , what , when? (2 min)
  - Ipshit - debts on client side and statistics if time allows
  - Maria - testing, fix errors, edit and delete participant
  - Kasparas - create confirmation pages for actions, expense controller tests, css
  - Dan - websockets for event overview, 
  - Yana - recent events long polling  
  - Everyone - fix bug with expense not being added after creating new participant

---
- Feedback round: What went well and what can be improved next time? (1 min)
- Planned meeting duration != actual duration? Where/why did you mis -estimate? (2 min)
- Question round: Does anyone have anything to add before the meeting closes? (1 min)
- Closure (1 min)



- Misc:
  - write features to the end, do not start new features if not needed
  - not deleting participant if he has any debts / expenses he cannot be deleted
  - font - comic sans