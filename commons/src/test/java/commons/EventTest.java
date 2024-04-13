package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private Event event2;
    private final Participant p = new Participant("a", "b", null, null, null, null);
    private final List<Participant> participantList = List.of(p);
    private final Expense e = new Expense("food", "$", 20, Date.valueOf(LocalDate.now()));
    private final List<Expense> expenseList = List.of(e);

    @BeforeEach
    void setUp(){
        event = new Event("Event1");
        event2 = new Event("Event2");
    }

    @Test
    void constructorTest() {
        assertNotNull(event);
        assertNotNull(event.getTitle());
        assertNotNull(event.getId());
        assertNotNull(event.getCreationDate());
        assertNotNull(event.getLastUpdateDate());
        assertEquals(List.of(), event.getExpenses());
        assertEquals(List.of(), event.getParticipants());
        Event empty = new Event();
        assertNotNull(empty);
    }

    @Test
    void getId() {
        String id = event.getId();
        assertEquals(id, event.getId());
    }

    @Test
    void setId() {
        event.setId("RandomID");
        assertEquals("RandomID", event.getId());
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
    void deleteParticipant() {
        event.addParticipant(p);
        assertEquals(List.of(p), event.getParticipants());
        event.deleteParticipant(p);
        assertEquals(List.of(), event.getParticipants());
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
    void testEquals() {
        assertNotEquals(new Event("a"), new Event("a"));
        assertNotEquals(event, event2);
        event2.setId(event.getId());
        assertEquals(event, event2);
    }

    @Test
    void testHashCode() {
        assertEquals(event.hashCode(), event.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Event{title='Event1', " +
                "inviteCode='"+ event.getId() +"', " +
                "participants=[], expenses=[], debts=[], " +
                "creationDate=" + event.getCreationDate() + ", " +
                "lastUpdateDate=" + event.getLastUpdateDate() + '}', event.toString());
    }

    @Test
    void timestampTest() {
        try {
            Timestamp ts = event.getCreationDate();
            TimeUnit.SECONDS.sleep(1);
            event.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
            event.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            assertNotEquals(ts, event.getCreationDate());
            assertNotEquals(ts, event.getLastUpdateDate());

            ts = event.getLastUpdateDate();
            TimeUnit.SECONDS.sleep(1);
            event.updateDate();
            assertNotEquals(ts, event.getLastUpdateDate());
        } catch (Exception ignored) {}
    }

    @Test
    void getParticipantByName() {
        assertNull(event.getParticipantByName("a b"));
        event.setParticipants(participantList);
        assertEquals(p, event.getParticipantByName("a b"));
        assertNull(event.getParticipantByName("x"));
    }

    @Test
    void debtsTest() {
        Debt d = new Debt(new Participant("John", "Doe", null, null, null, null), new Participant("Jane", "Doe", null, null, null, null), 100);
        List<Debt> debts = new ArrayList<>();
        debts.add(d);
        event.setDebts(debts);
        assertEquals(debts, event.getDebts());
        Debt d2 = new Debt(new Participant("a", "b", null, null, null, null), new Participant("c", "d", null, null, null, null), 20);
        event.addDebt(d2);
        assertTrue(event.getDebts().contains(d2));
    }

}
