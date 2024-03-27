package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "expense_participant")
public class ExpenseParticipant {
    @Id
    private final UUID id = UUID.randomUUID();
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


    public UUID getId() {
        return id;
    }

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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
