package commons;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String firstName;
    public String lastName;
    /**
     * Optional parameter. Will not be part of toString, equals or hashcode.
     */
    public String email;

    @ManyToMany(mappedBy = "participants")
    private List<Event> events = new ArrayList<>();
    @OneToMany(mappedBy = "paidBy")
    private List<Expense> expenses = new ArrayList<>();
    @OneToMany(mappedBy = "lender")
    private List<Debt> debtsLendTo = new ArrayList<>();
    @OneToMany(mappedBy = "borrower")
    private List<Debt> debtsOwedTo = new ArrayList<>();

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void addDebtsLendTo(Debt debt) {
        debtsLendTo.add(debt);
    }
    public List<Debt> getDebtsLendTo() {
        return debtsLendTo;
    }
    public void addDebtsOwedTo(Debt debt) {
        debtsOwedTo.add(debt);
    }
    public List<Debt> getDebtsOwedTo() {
        return debtsOwedTo;
    }
    public static Participant getById(List<Participant> all, long id){
        for(Participant part: all){
            if(part.getId() == id)
                return part;
        }
        return null;
    }

    public Participant(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        String x = firstName + lastName;
        this.id = x.hashCode();
        this.email = null;
    }

    public Participant(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        String x = firstName + lastName;
        this.id = x.hashCode();
        this.email = email;
    }

    /**
     * Unused, it is here to get rid of the warning
     */
    public Participant() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        String x = firstName + lastName;
        this.id = x.hashCode();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        String x = firstName + lastName;
        this.id = x.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return id == that.id && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
