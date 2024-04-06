package commons;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String inviteCode;
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();
    @Column(nullable = false)
    private LocalDateTime creationDate;
    @Column(nullable = false)
    private LocalDateTime lastUpdateDate;

    @SuppressWarnings("unused")
    public Event() {}

    /**
     * Creates an Event object
     * @param title the title of the Event
     */
    public Event(String title) {
        this.title = title;
        this.inviteCode = UUID.randomUUID().toString().substring(0,11).replace("-", "");
        this.creationDate = LocalDateTime.now();
        this.lastUpdateDate = LocalDateTime.of(creationDate.getYear(), creationDate.getMonth(),
                creationDate.getDayOfMonth(), creationDate.getHour(),
                creationDate.getMinute(), creationDate.getSecond());
    }

    public long getId() {
        return id;
    }

    //TESTING ONLY
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

    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) { participants.add(participant); }

    /**
     * Updates a participant in the list of participants
     * @param participant - the participant to be updated
     */
    public void updateParticipant(Participant participant) {
        for (Participant p : participants) {
            if (p.getId() == participant.getId()) {
                p.setFirstName(participant.getFirstName());
                p.setLastName(participant.getLastName());
                p.setEmail(participant.getEmail());
            }
        }
    }
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
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
        return Objects.equals(inviteCode, event.inviteCode);
    }

    /**
     * Hash code generator for an Event
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(inviteCode);
    }

    /**
     * Gives a human-friendly representation of an Event
     *
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
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }

}
