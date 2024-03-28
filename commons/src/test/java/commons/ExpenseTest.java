package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {

    private Expense e1;
    private Expense e2;
    private Event event;

    @BeforeEach
    public void setUp() {
        event = new Event("test");
        e1 = new Expense("item", "EUR", 20,
                Date.valueOf(LocalDate.now()));
        e2 = new Expense("item", "EUR", 20,
                Date.valueOf(LocalDate.now()));
    }

    @Test
    public void testConstructor() {
        assertNotNull(e1);
        assertNotNull(e2);
        assertNotNull(new Expense());
    }

    @Test
    public void equalsHashCode() {
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Expense e3 = new Expense("drinks", "USD", 10,
                Date.valueOf(LocalDate.now()));
        assertNotEquals(e1, e3);
        assertNotEquals(e1.hashCode(), e3.hashCode());
    }

    @Test
    public void idTest() {
        e1.setId(1);
        e2.setId(2);
        assertNotEquals(e1.getId(), e2.getId());
    }

    @Test
    public void descriptionTest() {
        e1.setDescription("Expense for drinks");
        assertEquals("Expense for drinks", e1.getDescription());
    }

    @Test
    public void currencyTest() {
        e1.setCurrency("USD");
        assertEquals("USD", e1.getCurrency());
    }

    @Test
    public void amountTest() {
        e1.setAmount(10.0);
        assertEquals(10.0, e1.getAmount());
    }

    @Test
    public void dateTest() {
        e1.setDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), e1.getDate());
    }

    @Test
    public void tagTest() {
        Tag t = new Tag("Food", "Red");
        e1.setTag(t);
        assertEquals(t, e1.getTag());
    }

    @Test
    public void eventTest() {
        e1.setEvent(event);
        assertEquals(event, e1.getEvent());
    }

    @Test
    public void debtorsTest() {
        Set<ExpenseParticipant> debtors = new HashSet<>();
        debtors.add(new ExpenseParticipant(e1, new Participant("Jane", "Doe"), 50, false));
        debtors.add(new ExpenseParticipant(e1, new Participant("John", "Doe"), 50, true));
        e1.setDebtors(debtors);
        assertEquals(debtors, e1.getDebtors());
    }

    @Test
    public void toStringTest() {
        assertEquals("Expense{id=0, description='item', currency='EUR', amount=20.0, date=" +
                        Date.valueOf(LocalDate.now()) + ", tag=null}", e1.toString());
    }
}
