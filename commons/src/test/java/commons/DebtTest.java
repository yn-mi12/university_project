package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class DebtTest {

    private Debt debt;
    private Participant debtor;
    private Participant creditor;
    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event("Test");
        debtor = new Participant("John", "Doe");
        debtor.setEvent(event);
        creditor = new Participant("Jane", "Doe");
        creditor.setEvent(event);
        debt = new Debt(debtor, creditor, 100, false);
    }

    @Test
    void constructorTest() {
        assertNotNull(debt);
    }

    @Test
    void getDebtor() {
        Participant check = new Participant("John", "Doe");
        check.setEvent(event);
        assertEquals(check, debt.getDebtor());
    }

    @Test
    void setDebtor() {
        Participant check = new Participant("a", "b");
        check.setEvent(event);
        debt.setDebtor(check);
        assertEquals(check, debt.getDebtor());
    }

    @Test
    void getCreditor() {
        Participant check = new Participant("Jane", "Doe");
        check.setEvent(event);
        assertEquals(check, debt.getCreditor());
    }

    @Test
    void setCreditor() {
        Participant check = new Participant("d", "e");
        check.setEvent(event);
        debt.setCreditor(check);
        assertEquals(check, debt.getCreditor());
    }

    @Test
    void getAmount() {
        assertEquals(100, debt.getAmount());
    }

    @Test
    void setAmount() {
        debt.setAmount(1000);
        assertEquals(1000, debt.getAmount());
    }

    @Test
    void isPaid() {
        assertEquals(false, debt.isPaid());
    }

    @Test
    void setPaid() {
        debt.setPaid(true);
        assertEquals(true, debt.isPaid());
    }

    @Test
    void testEquals() {
        assertEquals(debt, debt);
        Debt d2 = new Debt(debtor, creditor, 20, true);
        assertNotEquals(d2, debt);
        Debt d3 = new Debt(new Participant("a", "b"), new Participant("c", "d"), 100, false);
        assertNotEquals(d3, debt);
    }

    @Test
    void testHashCode() {
        int hash = Objects.hash(debtor, creditor, debt.getAmount(), debt.isPaid());
        assertEquals(hash, debt.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Debt{debtor=Participant{id=0, firstName='John', lastName='Doe', email='null'}, " +
                "creditor=Participant{id=0, firstName='Jane', lastName='Doe', email='null'}, amount=100.0, " +
                "isPaid=false}", debt.toString());
    }
}
