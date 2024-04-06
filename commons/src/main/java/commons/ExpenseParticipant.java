package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class ExpenseParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Expense expense;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Participant participant;
    @Column(nullable = false)
    private double share;
    @Column(nullable = false)
    private boolean owner = false;

    public ExpenseParticipant () {}

    public ExpenseParticipant(Expense expense, Participant participant, double share, boolean owner) {
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

    public double getShare() {
        return share;
    }

    public void setShare(double share) {
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
        return share == that.share
                && owner == that.owner
                && expense.getId() == that.expense.getId()
                && Objects.equals(participant, that.participant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expense.getId(), participant, share, owner);
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
