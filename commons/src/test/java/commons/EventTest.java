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
    private Participant participant;
    private List<Participant> participantList;
    private Expense expense;
    private List<Expense> expenseList;

    @BeforeEach
    void setUp(){
        event = new Event("Event1");
        event2 = new Event("Event2");
        event3 = new Event("Event2");
        participant = new Participant("a", "b");
        participantList = new ArrayList<>();
        participantList.add(participant);
        expenseList = new ArrayList<>();
        expense = new Expense("food", "$", participant, 20, Date.valueOf(LocalDate.now()));
        expenseList.add(expense);

    }


    @Test
    void addCreator() {
        event.addCreator(participant);
        assertEquals(event.getParticipants(), participantList);
    }

    @Test
    void getId() {
        long id = event.getId();
        assertEquals(id, event.getId());
    }

    @Test
    void setId() {
        event.setId(2);
        assertEquals(2, event.getId());
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
    void setInviteCode() {
        event.setInviteCode("1234");
        assertEquals("1234", event.getInviteCode());
    }

    @Test
    void getParticipants() {
        event.addCreator(participant);
        assertEquals(participantList, event.getParticipants());
    }

    @Test
    void setParticipants() {
        event.setParticipants(participantList);
        assertEquals(participantList, event.getParticipants());
    }

    @Test
    void getExpenses() {
        event.addExpense(expense);
        assertEquals(expenseList, event.getExpenses());
    }

    @Test
    void setExpenses() {
        event.setExpenses(expenseList);
        assertEquals(expenseList, event.getExpenses());
    }

    @Test
    void testEquals() {
        assertNotEquals(event, event2);
        assertEquals(event, event);
        assertNotEquals(event2, event3);
    }

    @Test
    void testHashCode() {
        assertEquals(event.hashCode(), event.hashCode());
    }

    @Test
    void testToString() {
        String inviteCode = event.getInviteCode();
        assertEquals("Event{id=0, title='Event1', inviteCode='"+ inviteCode +"', participants=[], expenses=[]}", event.toString());
    }
}