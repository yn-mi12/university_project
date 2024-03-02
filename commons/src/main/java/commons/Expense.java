package commons;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    @ManyToOne(targetEntity = Participant.class, optional = false)
    @JoinColumn(name = "PARTICIPANT_FK", nullable = false)
    private Participant paidBy;
    private String currency;
    private double amount;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    /**
     * creates an Expense object.
     *
     * @param description - type of expense
     * @param currency    - type of currency
     * @param paidBy      - who paid for the expense
     * @param amount      - cost of the expense
     * @param date        - date of the expense
     */
    public Expense(String description, String currency, Participant paidBy,
                   double amount, Date date) {
        this.description = description;
        this.currency = currency;
        this.paidBy = paidBy;
        this.amount = amount;
        this.date = date;
    }

    public Expense() {

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
     * @return - the id of the expense
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the person who paid
     */
    public Participant getPaidBy() {
        return paidBy;
    }

    /**
     * set the person who paid for the expense.
     *
     * @param paidBy - the specified person
     */
    public void setPaidBy(Participant paidBy) {
        this.paidBy = paidBy;
    }

    /**
     * compares two objects to see if they have the same attributes.
     *
     * @param o - the object we are comparing the current object with
     * @return true if the objects have the same properties
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0
                && Objects.equals(description, expense.description)
                && Objects.equals(paidBy, expense.paidBy)
                && Objects.equals(currency, expense.currency)
                && Objects.equals(date, expense.date)
                && Objects.equals(id, expense.id);
    }

    /**
     * @return a unique number corresponding to the attributes of the object
     * equals objects have the same hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, paidBy, currency, amount, date, id);
    }

    /**
     * @return the information the object contains in a human-readable format
     */
    @Override
    public String toString() {
        return "Expense: " +
                description + '\'' +
                ", paid by " + paidBy +
                ", currency is '" + currency + '\'' +
                ", amount is " + amount +
                "\ndate is" + date +
                ", id=" + id;
    }
}