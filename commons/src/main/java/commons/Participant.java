package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private String email;
    private String accountName;
    private String iban;
    private String bic;
    @JsonIgnore
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private Set<ExpenseParticipant> expenses;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private Event event;
    @JsonIgnore
    @OneToMany(mappedBy = "debtor", cascade = CascadeType.ALL)
    private List<Debt> debtsWhereDebtor = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "creditor", cascade = CascadeType.ALL)
    private List<Debt> debtsWhereCreditor = new ArrayList<>();

    @SuppressWarnings("unused")
    public Participant() {

    }

    public Participant(String firstName, String lastName, String email, String accountName, String iban, String bic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountName = accountName;
        this.iban = iban;
        this.bic = bic;
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
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public Set<ExpenseParticipant> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<ExpenseParticipant> expenses) {
        this.expenses = expenses;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Debt> getDebtsWhereDebtor() {
        return debtsWhereDebtor;
    }

    public void setDebtsWhereDebtor(List<Debt> debtsWhereDebtor) {
        this.debtsWhereDebtor = debtsWhereDebtor;
    }

    public List<Debt> getDebtsWhereCreditor() {
        return debtsWhereCreditor;
    }

    public void setDebtsWhereCreditor(List<Debt> debtsWhereCreditor) {
        this.debtsWhereCreditor = debtsWhereCreditor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(email, that.email)
                && Objects.equals(accountName, that.accountName)
                && Objects.equals(iban, that.iban)
                && Objects.equals(bic, that.bic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, accountName, iban, bic);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accountName='" + accountName + '\'' +
                ", iban='" + iban + '\'' +
                ", bic='" + bic + '\'' +
                '}';
    }
}
