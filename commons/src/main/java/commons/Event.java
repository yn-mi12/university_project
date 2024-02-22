package commons;


import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private String title;
    private List<User> participants;
    private String inviteCode;
    private List<Debt> debts;
    private List<Expense> expenses;
    //I'm not putting creator as a parameter because once the event is created all users have the same rights

    public Event(User creator, String title, String inviteCode) {
        this.title = title; //user gives the title when creating an event (mandatory!)
        this.participants = new ArrayList<User>();
        this.participants.add(creator);//the user creating it should be automatically added
        this.inviteCode = inviteCode; //invite code should be generated and passed when an event is created
        this.debts = new ArrayList<Dept>(); //empty list by default
        this.expenses = new ArrayList<Expense>(); //empty list by default
    }

    public String getTitle() {
        return title;
    }

    //edit title
    public void editTitle(String title) {
        this.title = title;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
}
