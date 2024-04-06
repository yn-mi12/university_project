package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String description;
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private Set<ExpenseParticipant> debtors;
    @Column(nullable = false)
    private String currency;
    @Column(nullable = false)
    private double amount;
    private LocalDate date;
    @ManyToOne
    private Tag tag;
    @JsonIgnore
    @ManyToOne
    private Event event;

    @SuppressWarnings("unused")
    public Expense() {}

    public Expense(String description, String currency, double amount, LocalDate date) {
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ExpenseParticipant> getDebtors() {
        return debtors;
    }

    public void setDebtors(Set<ExpenseParticipant> debtors) {
        this.debtors = debtors;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0 &&
                Objects.equals(description, expense.description) &&
                Objects.equals(currency, expense.currency) &&
                Objects.equals(date, expense.date) &&
                Objects.equals(tag, expense.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, currency, amount, date, tag);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", debtors=" + debtors +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", tag=" + tag +
                '}';
    }
}
