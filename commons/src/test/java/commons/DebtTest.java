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
        debtor = new Participant("John", "Doe", null, null, null, null);
        debtor.setEvent(event);
        creditor = new Participant("Jane", "Doe", null, null, null, null);
        creditor.setEvent(event);
        debt = new Debt(debtor, creditor, 100);
        debt.setEvent(event);
    }

    @Test
    void constructorTest() {
        assertNotNull(debt);
        Debt x = new Debt();
        assertNotNull(x);
    }

    @Test
    void setAndGetId() {
        debt.setId(42);
        assertEquals(42, debt.getId());
    }

    @Test
    void getDebtor() {
        Participant check = new Participant("John", "Doe", null, null, null, null);
        check.setEvent(event);
        assertEquals(check, debt.getDebtor());
    }

    @Test
    void setDebtor() {
        Participant check = new Participant("a", "b", null, null, null, null);
        check.setEvent(event);
        debt.setDebtor(check);
        assertEquals(check, debt.getDebtor());
    }

    @Test
    void getCreditor() {
        Participant check = new Participant("Jane", "Doe", null, null, null, null);
        check.setEvent(event);
        assertEquals(check, debt.getCreditor());
    }

    @Test
    void setCreditor() {
        Participant check = new Participant("d", "e", null, null, null, null);
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
    void getEvent() {
        assertEquals(event, debt.getEvent());
    }

    @Test
    void setEvent() {
        Event test = new Event("temp");
        debt.setEvent(test);
        assertEquals(test, debt.getEvent());
    }

    @Test
    void testEquals() {
        Debt d2 = new Debt(debtor, creditor, 20);
        assertNotEquals(d2, debt);
        Debt d3 = new Debt(
                new Participant("a", "b", null, null, null, null),
                new Participant("c", "d", null, null, null, null),
                100);
        assertNotEquals(d3, debt);
        Debt d4 = new Debt(debtor,
                new Participant("c", "d", null, null, null, null),
                100);
        assertNotEquals(d4, debt);
    }

    @Test
    void testHashCode() {
        int hash = Objects.hash(debtor, creditor, debt.getAmount());
        assertEquals(hash, debt.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Debt{id=0, debtor=Participant{id=0, firstName='John', lastName='Doe', email='null', " +
                "accountName='null', iban='null', bic='null'}, creditor=Participant{id=0, firstName='Jane', lastName=" +
                "'Doe', email='null', accountName='null', iban='null', bic='null'}, amount=100.0}", debt.toString());
    }
}
