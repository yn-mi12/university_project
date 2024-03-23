package commons.dto;

import commons.Tag;

import java.util.Date;
import java.util.Objects;

public class ExpenseDTO {
    private Long id;
    private String description;
    private ParticipantDTO paidBy;
    private String currency;
    private double amount;
    private Date date;
    private EventDTO event;
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
    public ExpenseDTO(String description, String currency, ParticipantDTO paidBy,
                      double amount, Date date) {
        this.description = description;
        this.currency = currency;
        this.paidBy = paidBy;
        this.amount = amount;
        this.date = date;
    }

    public ExpenseDTO() {

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
    public ParticipantDTO getPaidBy() {
        return paidBy;
    }

    /**
     * set the person who paid for the expense.
     *
     * @param paidBy - the specified person
     */
    public void setPaidBy(ParticipantDTO paidBy) {
        this.paidBy = paidBy;
    }

    /**
     * @return the information the object contains in a human-readable format
     */
    @Override
    public String toString() {
        return "ExpenseDTO: " +
                description + '\'' +
                ", paid by " + paidBy +
                ", currency is '" + currency + '\'' +
                ", amount is " + amount +
                "\ndate is" + date +
                ", id=" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseDTO that = (ExpenseDTO) o;
        return Double.compare(amount, that.amount) == 0 && Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(currency, that.currency) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, currency, amount, date);
    }
}