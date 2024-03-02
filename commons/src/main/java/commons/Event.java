
package commons;

import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
import java.util.UUID;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String inviteCode;
//    private List<Participant> participants;
//    private List<Expense> expenses;

    public Event(String title) {
        this.title = title;
        this.inviteCode = UUID.randomUUID().toString();
        //let me know if the generation of the invite code is fine like this when reviewing
//        this.participants = new ArrayList<>();
//        this.expenses = new ArrayList<>();
    }

    public Event() {

    }

//    public void addCreator(Participant creator){
//        this.participants.add(creator);
////        creator should be automatically added as a participant when the vent is created
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInviteCode() {
        return inviteCode;
    }
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }


}
