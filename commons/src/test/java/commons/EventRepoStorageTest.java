package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.SimpleTimeZone;

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
        event1 = new Event("Event 1");
        event2 = new Event("Event 2");
        updatedevent = new Event( "Event 1");
        nullevent = new Event("Event 1");
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
        assertEquals(event1, events1.getEventByInviteCode(event1.getInviteCode()));
    }

    @Test
    void createEvent() {
        events1.createEvent(event1);
        assertTrue(events1.getAllEvents().contains(event1));
    }


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
        events1.createEvent(event1);
        events2.createEvent(event1);
        assertEquals(events1,events2);
    }

    @Test
    void testNotEquals() {
        events1.createEvent(event1);
        events2.createEvent(event2);
        assertNotEquals(events1,events2);
    }

    @Test
    void testNotEqualsNull() {
        events1.createEvent(event1);
        assertNotEquals(events1,null);
    }

    @Test
    void testHashCode() {
        events1.createEvent(event1);
        events2.createEvent(event1);
        assertEquals(events1.hashCode(),events2.hashCode());
        assertEquals(events1.hashCode(),events1.hashCode());
        events2.createEvent(event1);
        assertNotEquals(events1.hashCode(),events2.hashCode());
    }

    @Test
    void testToString() {
        events1.createEvent(nullevent);
        events1.createEvent(nullevent);
        events1.createEvent(nullevent);
        events1.createEvent(nullevent);
        String inviteCode = nullevent.getInviteCode();
        String x = "EventRepoStorage{" + "events=" + events1.getAllEvents() + '}';
        assertEquals("EventRepoStorage{events=[" +
                                "Event{id=0, title='Event 1', inviteCode='"+ inviteCode +"'}, " +
                                    "Event{id=0, title='Event 1', inviteCode='"+ inviteCode +"'}, " +
                                        "Event{id=0, title='Event 1', inviteCode='"+ inviteCode +"'}, " +
                                            "Event{id=0, title='Event 1', inviteCode='"+ inviteCode +"'}]}",events1.toString());
    }
}