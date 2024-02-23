package commons;

import java.util.Objects;

public class Debt {
    /**
     * who lends the money.
     */
    private Person lender;
    /**
     * who owes the money.
     */
    private Person borrower;
    /**
     * how much money does the borrower owe.
     */
    private double amount;
    /**
     * is the debt settled ot not.
     */
    private boolean settled;
    /**
     * what type of currency is used.
     */
    private String currency;

    /**
     * creates a new Debt object.
     * @param lender - lends the money
     * @param borrower - owes the money
     * @param amount - amount of money
     * @param currency - type of currency
     * @param settled - is the debt settled
     */
    public Debt(Person lender, Person borrower, double amount, String currency, boolean settled) {
        this.lender = lender;
        this.borrower = borrower;
        this.amount = amount;
        this.currency = currency;
        this.settled = settled;
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
        return settled;
    }

    /**
     * decide if the debt is settled or unsettled.
     * @param settled - specified option
     */
    public void setSettled(boolean settled) {
        this.settled = settled;
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
        return Double.compare(amount, debt.amount) == 0 && settled == debt.settled
                && Objects.equals(lender, debt.lender) && Objects.equals(borrower, debt.borrower)
                && Objects.equals(currency, debt.currency);
    }
    /**
     * @return a unique number for each object
     */
    @Override
    public int hashCode() {
        return Objects.hash(lender, borrower, amount, settled, currency);
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
                ", settled=" + settled +
                '}';
    }
}
