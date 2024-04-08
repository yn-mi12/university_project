package server.api;

import commons.Event;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

class EventControllerTest {

    private TestEventRepository repo;
    private EventController eventc;
    private Event x1;
    private Event x2;
    private Event x3;

    @BeforeEach
    void setUp() {
        repo = new TestEventRepository();
        eventc = new EventController(repo);
        x1 = new Event("Event 1");
        x2 = new Event("Event 2");
        x3 = new Event("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
    }

    @Test
    void getAll() {
        assertEquals(3, repo.count());
        assertEquals("count", repo.calledMethods.getLast());

        var actual = eventc.getAll();
        assertEquals(OK, actual.getStatusCode());
        assertEquals("findAll", repo.calledMethods.getLast());
        assertEquals(List.of(x1, x2, x3), actual.getBody());
    }


    @Test
    void save1() {
        var actual = eventc.save(new Event(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void save2() {
        var actual = eventc.save(new Event(""));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void save3() {
        Event x = new Event("k");
        x.setId(null);
        var actual = eventc.save(x);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void save4() {
        Event x = new Event("k");
        x.setId("");
        var actual = eventc.save(x);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void save5() {
        Event x = new Event("k");
        long c = repo.count();
        var actual = eventc.save(x);
        assertEquals(OK, actual.getStatusCode());
        assertTrue(repo.calledMethods.contains("save"));
        assertEquals(c+1, repo.count());
    }

    @Test
    void save6() {
        Event x = new Event("k");
        long c = repo.count();
        x.setId(x1.getId());
        var actual = eventc.save(x);
        assertEquals(OK, actual.getStatusCode());
        assertTrue(repo.calledMethods.contains("save"));
        assertEquals(c, repo.count());
    }

    @Test
    void getById() {
        var bad_req = eventc.getById("");
        assertEquals(NOT_FOUND, bad_req.getStatusCode());

        var good_req = eventc.getById(x1.getId());
        assertTrue(repo.calledMethods.contains("findById"));
        assertEquals(x1, good_req.getBody());
        assertEquals(OK, good_req.getStatusCode());
    }


    @Test
    void deleteById() {
        var bad_req = eventc.deleteById("c");
        assertEquals(NOT_FOUND, bad_req.getStatusCode());

        var c = repo.count();
        var actual = eventc.deleteById(x1.getId());
        assertTrue(repo.calledMethods.contains("deleteById"));
        assertEquals(x1, actual.getBody());
        assertEquals(OK, actual.getStatusCode());
        assertFalse(repo.events.contains(x1));
        assertTrue(repo.events.contains(x3));
        assertEquals(c-1, repo.count());
    }

    @Test
    void updateTitle1() {
        var bad_req = eventc.updateTitle(x1.getId(), "");
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());

        var bad_req2 = eventc.updateTitle(x1.getId(), null);
        assertEquals(BAD_REQUEST, bad_req2.getStatusCode());
    }

    @Test
    void updateTitle2() {
        var bad_req = eventc.updateTitle(null, "kebabai");
        assertEquals(NOT_FOUND, bad_req.getStatusCode());
    }

    @Test
    void updateTitle3() {
        var actual = eventc.updateTitle(x1.getId(), "randomEvent");
        assertEquals(OK, actual.getStatusCode());
        assertEquals(x1, actual.getBody());
    }


}
