package commons;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.Objects;

public class Debt {


    @OneToOne
    private Person lender;
    /**
     * borrower needs to pay to lender.
     */
    @OneToOne
    private Person borrower;
    @ManyToOne
    private Expense source;
    /**
     * how much money does the borrower owe.
     */
    private double amount;
    private boolean isSettled;
    private String currency;


    @SuppressWarnings("unused")
    public Debt() {

    }



    /**
     * creates a new Debt object.
     * @param lender - lends the money
     * @param borrower - owes the money
     * @param amount - amount of money
     * @param currency - type of currency
     * @param isSettled - is the debt settled
     */
    public Debt(Person lender, Person borrower, double amount, String currency, boolean isSettled) {
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
        this.currency = currency;
        this.isSettled = isSettled;
    }

    /**
     * creates a new Debt object.
     * @param lender - lends the money
     * @param borrower - owes the money
     * @param source - the source of Debt
     * @param amount - amount of money
     * @param currency - type of currency
     * @param isSettled - is the debt settled
     */
    public Debt(Person lender, Person borrower, Expense source, double amount, boolean isSettled, String currency) {
        this.lender = lender;
        this.borrower = borrower;
        this.source = source;
        this.amount = amount;
        this.isSettled = isSettled;
        this.currency = currency;
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
     * @return the person who lent the money
     */
    public Person getLender() {
        return lender;
    }

    /**
     * set the person who lends the money.
     * @param lender - the specified lender
     */
    public void setLender(Person lender) {
        this.lender = lender;
    }

    /**
     * @return - the person who owes money to the lender
     */
    public Person getBorrower() {
        return borrower;
    }

    /**
     * set the borrower.
     * @param borrower - the specified person who owes the money
     */
    public void setBorrower(Person borrower) {
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
     * @return true if the debt is already settled
     */
    public boolean isSettled() {
        return isSettled;
    }

    /**
     * decide if the debt is settled or unsettled.
     * @param settled - specified option
     */
    public void setSettled(boolean settled) {
        this.isSettled = settled;
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
        Debt debt = (Debt) o;
        return Double.compare(amount, debt.amount) == 0 && isSettled == debt.isSettled
                && Objects.equals(lender, debt.lender) && Objects.equals(borrower, debt.borrower)
                && Objects.equals(currency, debt.currency);
    }
    /**
     * @return a unique number for each object
     */
    @Override
    public int hashCode() {
        return Objects.hash(lender, borrower, amount, isSettled, currency);
    }
    /**
     * @return the information an object contains in a readable format
     */
    @Override
    public String toString() {
        return "Debt{" +
                "lender=" + lender +
                ", borrower=" + borrower +
                ", amount=" + amount +
                ", currency=" + currency +
                ", settled=" + isSettled +
                '}';
    }
}
