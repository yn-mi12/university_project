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
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenses = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Debt> debts = new ArrayList<>();
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();
    @Column(nullable = false)
    private Timestamp creationDate;
    @Column(nullable = false)
    private Timestamp lastUpdateDate;

    @SuppressWarnings("unused")
    public Event() {
    }

    public Event(String title) {
        this.title = title;
        this.id = UUID.randomUUID().toString().substring(0, 11).replace("-", "");
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

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant) {
        participant.setEvent(this);
        participants.add(participant);
    }

    public void deleteParticipant(Participant participant) {
        participants.remove(participant);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void addExpense(Expense expense) {
        expense.setEvent(this);
        expenses.add(expense);
    }

    public void deleteExpense(Expense expense) {
        expenses.remove(expense);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        tag.setEvent(this);
        tags.add(tag);
    }

    public void deleteTag(Tag tag) {
        tags.remove(tag);
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

    public void updateDate() {
        lastUpdateDate = Timestamp.valueOf(LocalDateTime.now());
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public void addDebt(Debt debt) {
        debt.setEvent(this);
        debts.add(debt);
    }

    public void deleteDebt(Debt debt) {
        debts.remove(debt);
    }


    /**
     * Returns the participant with the specified name
     *
     * @param name the specified name
     * @return the participant with the specified name
     */
    public Participant getParticipantByName(String name) {
        String[] nameInArray = name.split(" ");
        if(nameInArray.length > 1){
            String first = nameInArray[0];
            String last = nameInArray[1];
            for(var x : participants) {
                if (x.getFirstName().equals(first) && x.getLastName().equals(last)){
                    return x;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", inviteCode='" + id + '\'' +
                ", participants=" + participants +
                ", expenses=" + expenses +
                ", debts=" + debts +
                ", tags=" + tags +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
