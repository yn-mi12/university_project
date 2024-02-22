package commons;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Expense {
    /**
     * what is the type of the expense.
     */
    private String description;
    /**
     * //what currency is used.
     */
    private String currency;
    /**
     * //who paid.
     */
    private Person giver;
    /**
     * //who received the item/service.
     */
    private List<Person> receivers;
    /**
     * //the cost of the expense.
     */
    private double amount;
    /**
     * //on what date was the expense made.
     */
    private Date date;

    /**
     * creates an Expense object.
     *
     * @param description - type of expense
     * @param currency    - type of currency
     * @param giver       - person who paid
     * @param receivers   - people who received the item/service
     * @param amount      - cost of the expense
     * @param date        - date of the expense
     */
    public Expense(String description, String currency, Person giver,
                   List<Person> receivers, double amount, Date date) {
        this.description = description;
        this.currency = currency;
        this.giver = giver;
        this.receivers = receivers;
        this.amount = amount;
        this.date = date;
    }

    /**
     * @return the description of the expense
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description of an expense.
     *
     * @param description - the given description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the currency of the expense
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * set the currency of an expense.
     *
     * @param currency - the given currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the giver (person who paid)
     */
    public Person getGiver() {
        return giver;
    }

    /**
     * set the giver of an expense.
     *
     * @param giver - the specified giver
     */
    public void setGiver(Person giver) {
        this.giver = giver;
    }

    /**
     * @return the receivers of the expense
     */
    public List<Person> getReceivers() {
        return receivers;
    }

    /**
     * set the receivers of an expense.
     *
     * @param receivers - the specified one or many receivers
     */
    public void setReceivers(List<Person> receivers) {
        this.receivers = receivers;
    }

    /**
     * @return the cost of the expense
     */
    public double getAmount() {
        return amount;
    }

    /**
     * set the cost of an expense.
     *
     * @param amount - the given cost
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * @return the date of the expense
     */
    public Date getDate() {
        return date;
    }

    /**
     * set the date of an expense.
     *
     * @param date - the given date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * checks if two objects have the same properties.
     * @param o - the object we are comparing with
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0 && Objects.equals(description, expense.description) && Objects.equals(currency, expense.currency) && Objects.equals(giver, expense.giver) && Objects.equals(receivers, expense.receivers) && Objects.equals(date, expense.date);
    }

    /**
     * @return a unique number for each object
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, currency, giver, receivers, amount, date);
    }

    /**
     * @return the information an object contains in a readable format
     */
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
