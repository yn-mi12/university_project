package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    private Event event;
    private Event event2;
    private Event event3;
    private final Participant p = new Participant("a", "b");
    private final List<Participant> participantList = List.of(p);
    private final Expense e = new Expense("food", "$", 20, Date.valueOf(LocalDate.now()));
    private final List<Expense> expenseList = List.of(e);

    @BeforeEach
    void setUp(){
        event = new Event("Event1");
        event2 = new Event("Event2");
        event3 = new Event("Event2");
    }

    @Test
    void getId() {
        long id = event.getId();
        assertEquals(id, event.getId());
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
    void setExpenses() {
        event.setExpenses(expenseList);
        assertEquals(expenseList, event.getExpenses());
    }

    @Test
    void testEquals() {
        assertNotEquals(event, event2);
        assertNotEquals(new Event("a"), new Event("a"));
        assertNotEquals(event2, event3);
    }

    @Test
    void testHashCode() {
        assertEquals(event.hashCode(), event.hashCode());
    }

    @Test
    void testToString() {
        String inviteCode = event.getInviteCode();
        assertEquals("Event{id=0, title='Event1', inviteCode='"+ inviteCode +"', participants=[], expenses=[], tags=[]}", event.toString());
    }
}