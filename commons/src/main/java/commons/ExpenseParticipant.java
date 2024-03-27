package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class ExpenseParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @JsonIgnore
    @ManyToOne
    private Expense expense;
    @ManyToOne
    private Participant participant;
    private int share;
    private boolean owner = false;

    public ExpenseParticipant () {}

    public ExpenseParticipant(Expense expense, Participant participant, int share, boolean owner) {
        this.expense = expense;
        this.participant = participant;
        this.share = share;
        this.owner = owner;
    }


    public Long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseParticipant that = (ExpenseParticipant) o;
        return share == that.share && owner == that.owner && Objects.equals(expense, that.expense)
                && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expense, participant, share, owner);
    }

    @Override
    public String toString() {
        return "ExpenseParticipant{" +
                "id=" + id +
                ", participant=" + participant +
                ", share=" + share +
                ", owner=" + owner +
                '}';
    }
}
