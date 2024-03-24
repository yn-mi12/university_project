package commons;

import jakarta.persistence.*;
import java.util.Objects;


@Entity
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Participant lender;
    /**
     * borrower needs to pay to lender.
     */
    @ManyToOne
    private Participant borrower;
    private double amount;
    private String currency;

    @SuppressWarnings("unused")
    public Debt() {}

    /**
     * creates a new Debt object.
     * @param lender - lends the money
     * @param borrower - owes the money
     * @param amount - amount of money
     * @param currency - type of currency
     */
    public Debt(Participant lender, Participant borrower, double amount, String currency) {
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * @return the id of the Debt object
     */
    public long getId() {
        return id;
    }

    /**
     * @param id - The new id of the Debt object
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the type of currency used
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * set the currency.
     * @param currency - specified currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the participant who lent the money
     */
    public Participant getLender() {
        return lender;
    }

    /**
     * set the participant who lends the money.
     * @param lender - the specified lender
     */
    public void setLender(Participant lender) {
        this.lender = lender;
    }

    /**
     * @return - the participant who owes money to the lender
     */
    public Participant getBorrower() {
        return borrower;
    }

    /**
     * set the borrower.
     * @param borrower - the specified participant who owes the money
     */
    public void setBorrower(Participant borrower) {
        this.borrower = borrower;
    }

    /**
     * @return - the amount of money owed
     */
    public double getAmount() {
        return amount;
    }

    /**
     * set the amount of money that should be returned.
     * @param amount - the specified amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * checks if two objects have the same properties.
     * @param o - the object we are comparing with
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Double.compare(amount, debt.amount) == 0
                && Objects.equals(lender, debt.lender)
                && Objects.equals(borrower, debt.borrower)
                && Objects.equals(currency, debt.currency);
    }


    /**
     * @return a unique number for each object
     */
    @Override
    public int hashCode() {
        return Objects.hash(lender, borrower, amount, currency);
    }

    /**
     * @return the information an object contains in a readable format
     */
    @Override
    public String toString() {
        return "Debt{" +
                "id=" + id +
                ", lender=" + lender +
                ", borrower=" + borrower +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }

}
