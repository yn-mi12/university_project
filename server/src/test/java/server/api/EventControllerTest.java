package server.api;

import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class EventControllerTest {

    private TestEventRepository repo;
    private EventController eventc;

    @BeforeEach
    void setUp() {
        repo = new TestEventRepository();
        TestParticipantRepository partRepo = new TestParticipantRepository();
        eventc = new EventController(repo,partRepo);
    }

    @Test
    void getAll() {
        Event x1 = new Event("Event 1");
        Event x2 = new Event("Event 2");
        Event x3 = new Event("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
        assertEquals(3,repo.count());
        assertTrue(repo.calledMethods.contains("count"));
        var actual = eventc.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
        assertEquals(x1,actual.getFirst());
    }


    @Test
    void save() {
        var actual = eventc.save(new Event(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        assertFalse(repo.calledMethods.contains("save"));
        eventc.save(new Event("Event 1"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    void getById() {
        Event x = new Event("Event 1");
        Event event = eventc.save(x).getBody();
        var bad_req = eventc.getById(4L);
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());
        Event find = eventc.getById(event.getId()).getBody();
        assertTrue(repo.calledMethods.contains("findById"));
        assertEquals(event, find);
    }

    @Test
    void getByInviteCode() {
        Event x = new Event("Test");
        Event event = eventc.save(x).getBody();
        String inviteCode = event.getInviteCode();
        assertEquals(event, eventc.getByInviteCode(inviteCode).getBody());
        assertEquals(BAD_REQUEST, eventc.getByInviteCode(null).getStatusCode());
    }

    @Test
    void deleteById() {
        Event x1 = new Event("Event 1");
        Event x2 = new Event("Event 2");
        Event x3 = new Event("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
        var actual = eventc.deleteById(eventc.getAll().get(0).getId());
        var bad_req = eventc.deleteById(-1);
        assertTrue(repo.calledMethods.contains("deleteById"));
        assertEquals(x1, actual.getBody());
        assertFalse(repo.events.contains(x1));
        assertTrue(repo.events.contains(x3));
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());
    }

    @Test
    void updateTitle() {
        Event x1 = new Event("Event 1");
        eventc.save(x1);
        eventc.updateTitle(eventc.getAll().get(0).getId(),"New Event");
        var bad_req = eventc.updateTitle(12423432L, "Test");
        assertTrue(repo.calledMethods.contains("save"));
        assertEquals("New Event",eventc.getAll().get(0).getTitle());
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());
    }
}
