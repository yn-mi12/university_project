package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    //Optional parameter. Will not be part of equals or hashcode.
    private String email;
    @JsonIgnore
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private Set<ExpenseParticipant> expenseWhereDebtor;
    @JsonIgnore
    @ManyToOne
    private Event event;

    @SuppressWarnings("unused")
    public Participant() {}

    public Participant(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Participant(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public Set<ExpenseParticipant> getExpenseWhereDebtor() {
        return expenseWhereDebtor;
    }

    public void setExpenseWhereDebtor(Set<ExpenseParticipant> expenseWhereDebtor) {
        this.expenseWhereDebtor = expenseWhereDebtor;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if(email == null) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Participant that = (Participant) o;
            return Objects.equals(firstName, that.firstName)
                    && Objects.equals(lastName, that.lastName);
        } else {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Participant that = (Participant) o;
            return Objects.equals(firstName, that.firstName)
                    && Objects.equals(lastName, that.lastName)
                    && Objects.equals(email, that.email);
        }
    }

    @Override
    public int hashCode() {
        int hashcode;
        if(email == null) {
            hashcode = Objects.hash(firstName, lastName);
        } else {
            hashcode = Objects.hash(firstName, lastName, email);
        }
        return hashcode;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // I don't know if this one belongs here or should be moved to Event.java Class
    public static Participant getById(List<Participant> all, long id){
        for(Participant part: all){
            if(part.getId() == id)
                return part;
        }
        return null;
    }
}
