package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "debtor_id")
    private Participant debtor;
    @ManyToOne
    @JoinColumn(name = "creditor_id")
    private Participant creditor;
    private double amount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Event event;

    @SuppressWarnings("unused")
    public Debt() {
    }

    public Debt(Participant debtor, Participant creditor, double amount) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Participant getDebtor() {
        return debtor;
    }

    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
    }

    public Participant getCreditor() {
        return creditor;
    }

    public void setCreditor(Participant creditor) {
        this.creditor = creditor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Double.compare(amount, debt.amount) == 0 && Objects.equals(debtor, debt.debtor) && Objects.equals(creditor, debt.creditor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debtor, creditor, amount);
    }

    @Override
    public String toString() {
        return "Debt{" + "id=" + id + ", debtor=" + debtor + ", creditor=" + creditor + ", amount=" + amount + '}';
    }
}
