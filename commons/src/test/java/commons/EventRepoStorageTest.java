package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.synth.SynthMenuItemUI;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EventRepoStorageTest {
    private EventRepoStorage events1;
    private EventRepoStorage events2;
    private Event event1;
    private Event event2;
    private Event updatedevent;
    private Event nullevent;

    @BeforeEach
    void setUp() {
        events1 = new EventRepoStorage();
        events2 = new EventRepoStorage();
        event1 = new Event(new Person("Riley", "Smith"), "Event 1", "1234");
        event2 = new Event(new Person("Juan", "Carter"), "Event 2", "0000");
        updatedevent = new Event(new Person("Harley", "Quinn"), "Event 1", "1234");
        nullevent = new Event(null, "Event 1", "1234");
    }

    @Test
    void getAllEvents() {
        events1.createEvent(event1);
        events1.createEvent(event2);
        ArrayList<Event> x = new ArrayList<>();
        x.add(event1);
        x.add(event2);
        assertEquals(x,events1.getAllEvents());
    }

    @Test
    void getEventByInviteCode() {
        events1.createEvent(event1);
        assertEquals(event1, events1.getEventByInviteCode("1234"));
    }

    @Test
    void createEvent() {
        events1.createEvent(event1);
        assertTrue(events1.getAllEvents().contains(event1));
    }

    @Test
    void updateEvent() {
        events1.createEvent(event1);
        events1.updateEvent(updatedevent);
        assertTrue(events1.getAllEvents().contains(updatedevent));
    }

    @Test
    void setEvents() {
        events1.createEvent(event1);
        events2.createEvent(event2);
        events1.setEvents(events2.getAllEvents());
        assertEquals(events1.getAllEvents(), events2.getAllEvents());
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
        events1.createEvent(nullevent);
        events1.createEvent(nullevent);
        events1.createEvent(nullevent);
        events1.createEvent(nullevent);
        String x = "EventRepoStorage{" + "events=" + events1.getAllEvents() + '}';
        assertEquals("EventRepoStorage{events=" +
                "[Event{title='Event 1', participants=[null], " +
                "inviteCode='1234', debts=[], expenses=[]}, " +
                "Event{title='Event 1', participants=[null], " +
                "inviteCode='1234', debts=[], expenses=[]}, " +
                "Event{title='Event 1', participants=[null], " +
                "inviteCode='1234', debts=[], expenses=[]}, " +
                "Event{title='Event 1', participants=[null], " +
                "inviteCode='1234', debts=[], expenses=[]}]}",events1.toString());
    }
}