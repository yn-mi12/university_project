package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantTest {

    private Participant x;

    @BeforeEach
    void setUp() {
        x = new Participant("a", "b");
    }

    @Test
    void checkConstructor() {
        Participant withEmail = new Participant("a","b","c");
        assertEquals("a",withEmail.getFirstName());
        assertEquals("b",withEmail.getLastName());
        assertEquals("c",withEmail.getEmail());
        Participant empty = new Participant();
        assertNotNull(empty);
    }

    @Test
    void checkConstructorNoEmail() {
        assertEquals("a",x.getFirstName());
        assertEquals("b",x.getLastName());
        assertNull(x.getEmail());
    }

    @Test
    void setId() {
        Participant y = new Participant("a","b");
        x.setId(1);
        y.setId(1);
        assertEquals(x.getId(),y.getId());
    }

    @Test
    void getFirstName() {
        assertEquals("a",x.getFirstName());
    }

    @Test
    void setFirstName() {
        x.setFirstName("c");
        assertEquals("c",x.getFirstName());
    }

    @Test
    void getLastName() {
        assertEquals("b",x.getLastName());
    }

    @Test
    void setLastName() {
        x.setLastName("c");
        assertEquals("c",x.getLastName());
    }

    @Test
    void setEmail() {
        x.setEmail("test");
        assertEquals("test", x.getEmail());
    }

    @Test
    void getAndSetAccountName() {
        x.setAccountName("John Doe");
        assertEquals(x.getAccountName(), "John Doe");
    }

    @Test
    void getAndSetIban() {
        x.setIban("NL12 3456 7890 5345 2342");
        assertEquals("NL12 3456 7890 5345 2342", x.getIban());
    }

    @Test
    void getAndSetBic() {
        x.setBic("ABCDEFGH");
        assertEquals("ABCDEFGH", x.getBic());
    }

    @Test
    void setExpenseWhereDebtor() {
        Expense e1 = new Expense("Food", "EUR", 10.0, Date.valueOf(LocalDate.now()));
        Expense e2 = new Expense("Drinks", "EUR", 12.0, Date.valueOf(LocalDate.now()));
        ExpenseParticipant ep1 = new ExpenseParticipant(e1, x, 20, false);
        ExpenseParticipant ep2 = new ExpenseParticipant(e2, x, 40, false);
        Set<ExpenseParticipant> expenseWhereDebtor = new HashSet<>();
        expenseWhereDebtor.add(ep1);
        expenseWhereDebtor.add(ep2);

        x.setExpenses(expenseWhereDebtor);
        assertEquals(expenseWhereDebtor, x.getExpenses());
    }

    @Test
    void eventTest() {
        Event event = new Event("Test");
        x.setEvent(event);
        assertEquals(event, x.getEvent());
    }

    @Test
    void testEquals() {
        Participant y = new Participant("a","b");
        assertEquals(x,y);
        x.setLastName("abc");
        assertNotEquals(x,y);
        assertNotEquals(null,x);

        Participant p1 = new Participant("a", "b", "c", "d", "e", "f");
        Participant p2 = new Participant("a", "b", "d", "d", "e", "f");
        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode() {
        Participant y = new Participant("a","b");
        assertEquals(x.hashCode(),y.hashCode());
        x.setLastName("z");
        assertNotEquals(x.hashCode(),y.hashCode());

        Participant p1 = new Participant("a", "b", "c", "d", "e", "f");
        Participant p2 =new Participant("a", "b", "d", "d", "e", "f");
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Participant{id=0, firstName='a', lastName='b', email='null'" +
                ", accountName='null', iban='null', bic='null'}", x.toString());
    }

    @Test
    void debtsWhereDebtorTest() {
        Debt test = new Debt(new Participant("a", "b"), new Participant("c", "d"), 100);
        Participant x = new Participant("John","Doe");
        x.setDebtsWhereDebtor(List.of(test));
        assertEquals(List.of(test), x.getDebtsWhereDebtor());
    }

    @Test
    void debtsWhereCreditorTest() {
        Debt test = new Debt(new Participant("a", "b"), new Participant("c", "d"), 100);
        Participant x = new Participant("John","Doe");
        x.setDebtsWhereCreditor(List.of(test));
        assertEquals(List.of(test), x.getDebtsWhereCreditor());
    }
}