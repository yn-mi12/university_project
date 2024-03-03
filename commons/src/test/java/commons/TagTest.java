package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class TagTest {

    private Participant participant;
    private Expense expense;
    private List<Expense> expenseList;
    private Tag tag;

    @BeforeEach
    void setUp() {
        participant = new Participant("a", "b");
        expenseList = new ArrayList<>();
        expense = new Expense("food", "$", participant, 20, Date.valueOf(LocalDate.now()));
        expenseList.add(expense);
        tag = new Tag("Food", "Red");
    }

    @Test
    void constructorTest() {
        assertNotNull(tag);
    }

    @Test
    void testEquals() {
        Tag t2 = new Tag("Food", "Black");
        assertEquals(tag, tag);
        assertNotEquals(tag, t2);
    }

    @Test
    void testHashcode() {
        int expected = Objects.hash(tag.getId(), tag.getExpenses(), tag.getLabel(), tag.getColor());
        assertEquals(expected, tag.hashCode());
    }

    @Test
    void testGetId() {
        assertNotNull(tag.getId());
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
    void testSetExpenses() {
        List<Expense> newExpenses = new ArrayList<>();
        Expense e = new Expense("drinks", "$", participant, 10, Date.valueOf(LocalDate.now()));
        newExpenses.add(e);
        tag.setExpenses(newExpenses);
        assertEquals(newExpenses, tag.getExpenses());
    }

    @Test
    void testAddExpense() {
        Expense e = new Expense("taxi", "$", participant, 15, Date.valueOf(LocalDate.now()));
        tag.setExpenses(expenseList);
        tag.addExpense(e);
        assertTrue(tag.getExpenses().contains(e));
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
    void toStringTest() {
        tag.setExpenses(expenseList);
        String expected = "Tag{id=0, expenses=Expense: food', paid by Participant{id=3105, firstName='a', lastName='b'}" +
                ", currency is '$', amount is 20.0\n" +
                "date is" + Date.valueOf(LocalDate.now()).toString() + ", id=null, label='Food', color='Red'}";
        assertEquals(expected, tag.toString());
    }
}
