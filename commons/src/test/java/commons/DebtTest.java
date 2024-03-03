package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DebtTest {

    private Debt d1;
    private Debt d2;
    private Debt d3;

    @BeforeEach
    void setUp() {
        d1 = new Debt(new Participant("me",""), new Participant("you",""), new Expense(), 5, false, "EUR");
        d2 = new Debt(new Participant("me",""), new Participant("you",""), new Expense(), 5, false, "EUR");
        d3 = new Debt(new Participant("you",""), new Participant("me",""), new Expense(), 5, false, "EUR");
    }

    @Test
    public void checkConstructor() {

        assertEquals("me", d1.getLender().firstName);
        assertEquals("you", d1.getBorrower().firstName);
    }
    @Test
    public void equalsHashCode() {
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }
    @Test
    public void notEqualsHashCode() {
        assertNotEquals(d1, d3);
        assertNotEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    public void settleDebt() {
        d1.setSettled(true);
        assertTrue(d1.isSettled());
    }
}
