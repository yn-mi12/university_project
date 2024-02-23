package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
public class DebtTest {
    @Test
    public void checkConstructor() {
        var d = new Debt(new Person("me",""), new Person("you",""),
                5, "EUR",false);
        assertEquals("me", d.getLender().firstName);
        assertEquals("you", d.getBorrower().firstName);
    }
    @Test
    public void equalsHashCode() {
        var d1 = new Debt(new Person("me",""), new Person("you",""),
                5, "EUR",false);
        var d2 = new Debt(new Person("me",""), new Person("you",""),
                5, "EUR",false);
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }
    @Test
    public void notEqualsHashCode() {
        var d1 = new Debt(new Person("me",""), new Person("you",""),
                5, "EUR",false);
        var d2 = new Debt(new Person("me",""), new Person("you",""),
                12, "USD",false);
        assertNotEquals(d1, d2);
        assertNotEquals(d1.hashCode(), d2.hashCode());
    }
}
