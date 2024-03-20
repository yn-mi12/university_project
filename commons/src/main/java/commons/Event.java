package commons;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String inviteCode;
    @OneToMany
    private List<Participant> participants = new ArrayList<>();
    @OneToMany
    private List<Expense> expenses = new ArrayList<>();
    @OneToMany
    private List<Tag> tags = new ArrayList<>();

    @SuppressWarnings("unused")
    public Event() {}

    /**
     * Creates an Event object
     * @param title the title of the Event
     */
    public Event(String title) {
        this.title = title;
        this.inviteCode = UUID.randomUUID().toString();
    }

    /**
     * Getter for the id
     * @return the id of the event
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for the title
     * @return the title of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the title
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the invite code
     * @return the invite code
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Getter for the participants
     * @return the participants
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    /**
     * Setter for the participants
     * @param participants ne participants
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * Getter for the expenses
     * @return the expenses
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Adds a new expense to the expense list
     * @param newExpense the expense to be added
     */
    public void addExpense(Expense newExpense){
        expenses.add(newExpense);
    }

    /**
     * Setter for the expenses
     * @param expenses the new expenses
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag (Tag tag) {
        tags.add(tag);
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Tests equality of two Events
     * @param o the object to be tested with
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(title, event.title)
                && Objects.equals(inviteCode, event.inviteCode)
                && Objects.equals(participants, event.participants)
                && Objects.equals(expenses, event.expenses)
                && Objects.equals(tags, event.tags);
    }

    /**
     * Hash code generator for an Event
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, inviteCode, participants, expenses, tags);
    }

    /**
     * Gives a human-friendly representation of an Event
     * @return the human-friendly representation of the Event
     */
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", participants=" + participants +
                ", expenses=" + expenses +
                ", tags=" + tags +
                '}';
    }
}
