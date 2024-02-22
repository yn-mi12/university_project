package commons;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Expense {
    private String description;
    private String currency;
    private Person giver;
    private List<Person> receivers;
    private double amount;
    private Date date;

    public Expense(String description, String currency, Person giver,
                   List<Person> receivers, double amount, Date date) {
        this.description = description;
        this.currency = currency;
        this.giver = giver;
        this.receivers = receivers;
        this.amount = amount;
        this.date = date;
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

    public Person getGiver() {
        return giver;
    }

    public void setGiver(Person giver) {
        this.giver = giver;
    }

    public List<Person> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Person> receivers) {
        this.receivers = receivers;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0 && Objects.equals(description, expense.description) && Objects.equals(currency, expense.currency) && Objects.equals(giver, expense.giver) && Objects.equals(receivers, expense.receivers) && Objects.equals(date, expense.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, currency, giver, receivers, amount, date);
    }
    @Override
    public String toString() {
        return "Expense{" +
                "description='" + description + '\'' +
                ", currency='" + currency + '\'' +
                ", giver=" + giver +
                ", receivers=" + receivers +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}