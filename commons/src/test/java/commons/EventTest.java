package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private Event event2;
    private final Participant p = new Participant("a", "b");
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
        assertEquals(List.of(), event.getTags());
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
                "participants=[], expenses=[], tags=[], " +
                "creationDate=" + event.getCreationDate() + ", " +
                "lastUpdateDate=" + event.getLastUpdateDate() + '}', event.toString());
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
        event.setParticipants(participantList);
        assertEquals(p, event.getParticipantByName("a"));
        assertNull(event.getParticipantByName("x"));
    }
}
