package commons;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String description;
    @ManyToOne
    private Participant paidBy;
    private String currency;
    private double amount;
    private Date date;
    @ManyToOne
    private Tag tag;

    @SuppressWarnings("unused")
    public Expense() {}

    public Expense(String description, Participant paidBy, String currency, double amount, Date date) {
        this.description = description;
        this.paidBy = paidBy;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
    }

    // TO BE DELETED, some @Dan controllers dont work otherwise
    public Expense(String description, String currency, double amount, Date date) {
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
    }

    public Long getId() {
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

    public Participant getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Participant paidBy) {
        this.paidBy = paidBy;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0 &&
                Objects.equals(description, expense.description) &&
                Objects.equals(paidBy, expense.paidBy) &&
                Objects.equals(currency, expense.currency) &&
                Objects.equals(date, expense.date) &&
                Objects.equals(tag, expense.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, paidBy, currency, amount, date, tag);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", paidBy=" + paidBy +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", tag=" + tag +
                '}';
    }
}
