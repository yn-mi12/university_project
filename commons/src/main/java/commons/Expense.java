package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private Set<ExpenseParticipant> debtors;
    private String currency;
    private double amount;
    private Date date;
    @ManyToMany
    private Set<Tag> tags;

    @JsonIgnore
    @ManyToOne
    private Event event;

    @SuppressWarnings("unused")
    public Expense() {}

    public Expense(String description, String currency, double amount, Date date) {
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<ExpenseParticipant> getDebtors() {
        return debtors;
    }

    public void setDebtors(Set<ExpenseParticipant> debtors) {
        this.debtors = debtors;
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
                Objects.equals(tags, expense.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, currency, amount, date, tags);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", tags=" + tags +
                '}';
    }
}

