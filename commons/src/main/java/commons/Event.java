package commons;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Event {
    @Id
    private String id;
    @Column(nullable = false)
    private String title;
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();
    @Column(nullable = false)
    private Timestamp creationDate;
    @Column(nullable = false)
    private Timestamp lastUpdateDate;

    @SuppressWarnings("unused")
    public Event() {}

    /**
     * Creates an Event object
     * @param title the title of the Event
     */
    public Event(String title) {
        this.title = title;
        this.id = UUID.randomUUID().toString().substring(0,11).replace("-", "");
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
        this.lastUpdateDate = (Timestamp) creationDate.clone();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) { participants.add(participant); }

    public void deleteParticipant(Participant participant) {
        participants.remove(participant);
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
    public void addExpense(Expense newExpense){ expenses.add(newExpense); }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag (Tag tag) { tags.add(tag); }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Returns the participant with the specified name
     * @param name the specified name
     * @return the participant with the specified name
     */

    public Participant getParticipantByName(String name){
        for(var x : participants){
            if (x.getFirstName().equals(name)){
                return x;
            }
        }
        return null;
        //throw new NoSuchElementException("There is no participant with name: " + name);
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
        return Objects.equals(id, event.id);
    }

    /**
     * Hash code generator for an Event
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Gives a human-friendly representation of an Event
     *
     * @return the human-friendly representation of the Event
     */
    @Override
    public String toString() {
        return "Event{" +
                ", title='" + title + '\'' +
                ", inviteCode='" + id + '\'' +
                ", participants=" + participants +
                ", expenses=" + expenses +
                ", tags=" + tags +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

}
