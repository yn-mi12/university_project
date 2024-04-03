package commons;

import java.util.Objects;

public class Debt {

    private Participant debtor;
    private Participant creditor;
    private double amount;
    private boolean isPaid;

    public Debt(Participant debtor, Participant creditor, double amount, boolean isPaid) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
        this.isPaid = isPaid;
    }

    public Participant getDebtor() {
        return debtor;
    }

    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
    }

    public Participant getCreditor() {
        return creditor;
    }

    public void setCreditor(Participant creditor) {
        this.creditor = creditor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Double.compare(amount, debt.amount) == 0 && isPaid == debt.isPaid
                && Objects.equals(debtor, debt.debtor) && Objects.equals(creditor, debt.creditor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debtor, creditor, amount, isPaid);
    }

    @Override
    public String toString() {
        return "Debt{" +
                "debtor=" + debtor +
                ", creditor=" + creditor +
                ", amount=" + amount +
                ", isPaid=" + isPaid +
                '}';
    }
}
