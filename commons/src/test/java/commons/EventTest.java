package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private Event event2;
    private Event event3;
    private List<Person> persons;
    private List<Debt>  debts;
    private List<Expense> expenses;
    private Person lender;
    private Person borrower;
    private Expense expense;
    private Expense expense1;

    @BeforeEach
    void setUp(){
        persons = new ArrayList<>();
        lender = new Person("abc", "def");
        borrower = new Person("ghi", "jkl");
        persons.add(lender);
        debts = new ArrayList<>();
        expenses = new ArrayList<>();
        event = new Event(lender, "ski trip", "12345");
        event2 = new Event(lender, "ski trip", "12345");
        event3 = new Event(lender, "ski trip", "1234");
        expense = new Expense("tickets", "$", lender, persons,
                300, new Date(2024, Calendar.MAY, 12));
        expense1 = new Expense("food", "$", lender, persons,
                20, new Date(2024, Calendar.MARCH, 24));
    }

    @Test
    void testConstructor(){
        assertNotNull(event);
    }

    @Test
    void getTitle() {
        assertEquals("ski trip", event.getTitle());
    }

    @Test
    void setTitle() {
        event.setTitle("trip");
        assertEquals("trip", event.getTitle());
    }

    @Test
    void getParticipants() {
        event.addParticipant(borrower);
        persons.add(borrower);
        assertEquals(persons, event.getParticipants());
    }

    @Test
    void setParticipants() {
        persons.add(borrower);
        event.setParticipants(persons);
        assertEquals(persons, event.getParticipants());
    }

    @Test
    void getInviteCode() {
        assertEquals("12345", event.getInviteCode());
    }

    @Test
    void setInviteCode() {
        event.setInviteCode("123");
        assertEquals("123", event.getInviteCode());
    }

    @Test
    void getDebts() {
        assertEquals(debts, event.getDebts());
    }

    @Test
    void setDebts() {
        debts.add(new Debt(lender, borrower, 10, "$",false ));
        event.setDebts(debts);
        assertEquals(debts, event.getDebts());
    }

    @Test
    void getExpenses() {
        assertEquals(expenses, event.getExpenses());
    }

    @Test
    void setExpenses() {
        expenses.add(expense);
        event.setExpenses(expenses);
        assertEquals(expenses, event.getExpenses());
    }

    @Test
    void testEquals() {
        assertEquals(event, event);
        assertEquals(event, event2);
        assertNotEquals(event, event3);
    }

    @Test
    void testHashCode() {
        assertEquals(event.hashCode(), event2.hashCode());
        assertEquals(event.hashCode(), event.hashCode());
        assertNotEquals(event.hashCode(), event3.hashCode());
    }

    //needs to be changed when User class is created
//    @Test
//    void testToString() {
//        assertEquals("Event{ \n" +
//                "Title: ski trip\n" +
//                "Participants: commons.Person@5339bbad[\n" +
//                "  firstName=abc\n" +
//                "  id=0\n" +
//                "  lastName=def\n" +
//                "]\n" +
//                "Invite Code: 12345\n" +
//                "Debts: \n" +
//                "Expenses: }", event.toString());
//    }

    @Test
    void addExpense() {
        expenses.add(expense);
        event.addExpense(expense);
        assertEquals(expenses, event.getExpenses());
    }

    @Test
    void removeExpense() {
        event.addExpense(expense);
        event.addExpense(expense1);
        event.removeExpense(expense);
        expenses.add(expense1);
        assertEquals(expenses, event.getExpenses());
    }

    @Test
    void expensesSum() {
        event.addExpense(expense);
        event.addExpense(expense1);
        assertEquals(320, event.expensesSum());
    }

    @Test
    void addParticipant() {
        event.addParticipant(borrower);
        persons.add(borrower);
        assertEquals(persons, event.getParticipants());
    }
}