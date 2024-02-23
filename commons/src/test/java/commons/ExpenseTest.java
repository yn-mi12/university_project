package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ExpenseTest {
    @Test
    public void checkConstructor() {
        var e = new Expense("item", "EUR",new Person("me",""), List.of(new Person("you","")),
                5, Date.valueOf(LocalDate.now()));
        assertEquals("me", e.getGiver().firstName);
        assertEquals("you", e.getReceivers().get(0).firstName);
    }
    @Test
    public void equalsHashCode() {
        var e1 = new Expense("item", "EUR",new Person("me",""), List.of(new Person("you","")),
                5, Date.valueOf(LocalDate.now()));
        var e2 = new Expense("item", "EUR",new Person("me",""), List.of(new Person("you","")),
                5, Date.valueOf(LocalDate.now()));
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
    @Test
    public void notEqualsHashCode() {
        var e1 = new Expense("item", "EUR",new Person("me",""), List.of(new Person("you","")),
                5, Date.valueOf(LocalDate.now()));
        var e2 = new Expense("apple", "EUR",new Person("me",""), List.of(new Person("you","")),
                2, Date.valueOf(LocalDate.now()));
        assertNotEquals(e1, e2);
        assertNotEquals(e1.hashCode(), e2.hashCode());
    }
}
