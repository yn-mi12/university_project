package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private Event event2;
    private Event event3;
    private final Participant p = new Participant("a", "b");
    private final List<Participant> participantList = List.of(p);
    private final Expense e = new Expense("food", "$", 20, Date.valueOf(LocalDate.now()));
    private final List<Expense> expenseList = List.of(e);
    private final Tag t = new Tag("Food", "Red");
    private final List<Tag> tagList = List.of(t);

    @BeforeEach
    void setUp(){
        event = new Event("Event1");
        event2 = new Event("Event2");
        event3 = new Event("Event3");
    }

    @Test
    void constructorTest() {
        assertNotNull(event);
        assertNotNull(new Event());
    }
    @Test
    void getId() {
        long id = event.getId();
        assertEquals(id, event.getId());
    }

    @Test
    void setId() {
        event.setId(1);
        assertEquals(1, event.getId());
    }
    @Test
    void getTitle() {
        assertEquals("Event1", event.getTitle() );
    }

    @Test
    void setTitle() {
        event.setTitle("abc");
        assertEquals("abc", event.getTitle());
    }

    @Test
    void getInviteCode() {
        String inviteCode = event.getInviteCode();
        assertEquals(inviteCode, event.getInviteCode());
    }

    @Test
    void setParticipants() {
        event.setParticipants(participantList);
        assertEquals(participantList, event.getParticipants());
    }

    @Test
    void getParticipants() {
        event.addParticipant(p);
        assertEquals(List.of(p), event.getParticipants());
    }

    @Test
    void getExpenses() {
        event.addExpense(e);
        assertEquals(expenseList, event.getExpenses());
    }

    @Test
    void setExpenses() {
        event.setExpenses(expenseList);
        assertEquals(expenseList, event.getExpenses());
    }

    @Test
    void setInviteCode() {
        event.setInviteCode("1234");
        assertEquals("1234", event.getInviteCode());
    }

    @Test
    void testEquals() {
        event2.setTitle("Test");
        event2.setInviteCode("1234");
        event2.setParticipants(participantList);
        event2.setExpenses(expenseList);
        event2.setTags(tagList);
        event3.setTitle("Test");
        event3.setInviteCode("1234");
        event3.setParticipants(participantList);
        event3.setExpenses(expenseList);
        event3.setTags(tagList);
        assertEquals(event2, event3);
        assertNotEquals(new Event("a"), new Event("a"));
    }

    @Test
    void testHashCode() {
        assertEquals(event.hashCode(), event.hashCode());
    }

    @Test
    void testToString() {
        String inviteCode = event.getInviteCode();
        assertEquals("Event{id=0, title='Event1', inviteCode='"+ inviteCode +"', " +
                "participants=[], expenses=[], tags=[]}", event.toString());
    }

    @Test
    void tagsTest() {
        Tag t1 = new Tag("Food", "Red");
        Tag t2 = new Tag("Drinks", "Blue");

        event.addTag(t1);
        assertEquals(List.of(t1), event.getTags());

        event.setTags(List.of(t1, t2));
        assertEquals(List.of(t1, t2), event.getTags());
    }

    @Test
    void updateParticipant() {
        Participant test = new Participant("John", "Doe");
        test.setId(42);
        event.addParticipant(test);
        test.setFirstName("Jane");
        event.updateParticipant(test);
        assertTrue(event.getParticipants().get(0).getFirstName().equals("Jane"));
    }

    @Test
    void deleteParticipant() {
        Participant test = new Participant("John", "Doe");
        test.setId(42);
        event.addParticipant(test);
        event.deleteParticipant(test);
        assertTrue(event.getParticipants().isEmpty());
    }

    @Test
    void getParticipantByName() {
        Participant test = new Participant("John", "Doe");
        event.addParticipant(test);
        assertEquals(test, event.getParticipantByName("John Doe"));
        assertNull(event.getParticipantByName("Jack Doe"));
    }

    @Test
    void debtsTest() {
        Debt d = new Debt(new Participant("John", "Doe"), new Participant("Jane", "Doe"), 100);
        List<Debt> debts = new ArrayList<>();
        debts.add(d);
        event.setDebts(debts);
        assertEquals(debts, event.getDebts());
        Debt d2 = new Debt(new Participant("a", "b"), new Participant("c", "d"), 20);
        event.addDebt(d2);
        assertTrue(event.getDebts().contains(d2));
    }

}