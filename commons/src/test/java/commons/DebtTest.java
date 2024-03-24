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
        d1 = new Debt(new Participant("me",""), new Participant("you",""), 5, "EUR");
        d2 = new Debt(new Participant("me",""), new Participant("you",""), 5, "EUR");
        d3 = new Debt(new Participant("you",""), new Participant("me",""),5, "EUR");
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
        assertNotEquals(d1.hashCode(), d3.hashCode());
    }

    @Test
    public void toStringTest() {
        Debt d = new Debt(new Participant("John", "Doe"), new Participant("Jane", "Doe"), 10.0, "EUR");
        d.setId(1);
        assertEquals("Debt{id=1, lender=Participant{id=0, firstName='John', lastName='Doe', email='null'}," +
                " borrower=Participant{id=0, firstName='Jane', lastName='Doe', email='null'}, amount=10.0, currency='EUR'}",
                d.toString());
    }

    @Test
    public void testGettersAndSetters() {
        d1.setId(1);
        assertEquals(1, d1.getId());

        d1.setCurrency("USD");
        assertEquals("USD", d1.getCurrency());

        Participant lender = new Participant("John", "Doe");
        d1.setLender(lender);
        assertEquals(lender, d1.getLender());

        Participant borrower = new Participant("Jane", "Doe");
        d1.setBorrower(borrower);
        assertEquals(borrower, d1.getBorrower());

        d1.setAmount(10.0);
        assertEquals(10.0, d1.getAmount());
    }
}
