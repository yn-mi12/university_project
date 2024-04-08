package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class TagTest {

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag("Food", "Red");
    }

    @Test
    void constructorTest() {
        assertNotNull(tag);
        Tag empty = new Tag();
        assertNotNull(empty);
    }

    @Test
    void testEquals() {
        Tag t2 = new Tag("Food", "Black");
        assertNotEquals(tag, t2);
        Tag t3 = new Tag("Food", "Red");
        assertEquals(tag, t3);
    }

    @Test
    void testHashcode() {
        int expected = Objects.hash(tag.getLabel(), tag.getColor());
        assertEquals(expected, tag.hashCode());
    }

    @Test
    void testSetAndGetId() {
        tag.setId(1);
        assertEquals(1, tag.getId());
    }

    @Test
    void testGetLabel() {
        assertEquals("Food", tag.getLabel());
    }

    @Test
    void testGetColor() {
        assertEquals("Red", tag.getColor());
    }

    @Test
    void testSetLabel() {
        tag.setLabel("Drinks");
        assertEquals("Drinks", tag.getLabel());
    }

    @Test
    void testSetColor() {
        tag.setColor("Blue");
        assertEquals("Blue", tag.getColor());
    }

    @Test
    void eventTest() {
        Event event = new Event("Test");
        tag.setEvent(event);
        assertEquals(event, tag.getEvent());
    }

    @Test
    void expensesTest() {
        Expense e1 = new Expense("Food", "EUR", 10.0, Date.valueOf(LocalDate.now()));
        Expense e2 = new Expense("Drinks", "EUR", 10.0, Date.valueOf(LocalDate.now()));
        Set<Expense> expenses = new HashSet<>();
        expenses.add(e1);
        expenses.add(e2);
        tag.setExpenses(expenses);
        assertEquals(expenses, tag.getExpenses());
    }

    @Test
    void toStringTest() {
        assertEquals("Tag{id=0, label='Food', color='Red'}", tag.toString());
    }
}
