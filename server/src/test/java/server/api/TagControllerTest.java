package server.api;

import commons.Event;
import commons.ExpenseParticipant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

class TagControllerTest {
    private TestTagsRepository repo;
    private TestEventRepository eventRepository;
    private TagController tContr;
    private Event e;
    private Tag t1;
    private Tag t2;

    @BeforeEach
    void setUp(){
        repo = new TestTagsRepository();
        eventRepository = new TestEventRepository();
        tContr = new TagController(repo, eventRepository);
        e = new Event("event");
        eventRepository.save(e);
        t1 = new Tag("a", "b");
        t2 = new Tag("c", "d");
    }

    @Test
    void getByEventId() {
        Event e2 = new Event("2");
        eventRepository.save(e2);
        Tag t3 = new Tag("fs", "sdf");
        t3.setEvent(e2);
        t1.setEvent(e);
        t2.setEvent(e);

        t1 = tContr.saveToEvent(e.getId(), t1).getBody();
        t2 = tContr.saveToEvent(e.getId(), t2).getBody();

        var bad_req = tContr.getByEventId("");
        assertEquals(NOT_FOUND, bad_req.getStatusCode());

        var actual = tContr.getByEventId(e.getId());
        assertTrue(repo.calledMethods.contains("findByEventId"));
        assertEquals(OK, actual.getStatusCode());
        assertEquals(List.of(t1, t2), actual.getBody());

        actual = tContr.getByEventId(e2.getId());
        assertEquals(OK, actual.getStatusCode());

    }

    @Test
    void getById() {
        assertEquals(0,repo.count());
        t1 = tContr.saveToEvent(e.getId(), t1).getBody();
        t2 = tContr.saveToEvent(e.getId(), t2).getBody();
        assertEquals(2,repo.count());
        assertTrue(repo.calledMethods.contains("count"));

        var bad_req = tContr.getById(-1L);
        assertEquals(NOT_FOUND, bad_req.getStatusCode());

        var actual = tContr.getById(t1.getId());
        assertEquals(OK, actual.getStatusCode());
        assertEquals(t1, actual.getBody());

        actual = tContr.getById(t2.getId());
        assertEquals(OK, actual.getStatusCode());
        assertEquals(t2, actual.getBody());
    }

    @Test
    void successfulSaveToEvent() {
        var actual1 = tContr.saveToEvent(e.getId(), t1);
        t1 = actual1.getBody();
        assertEquals(OK, actual1.getStatusCode());
        assertEquals(t1, actual1.getBody());
    }

    @Test
    void noEvent() {
        var bad_req = tContr.saveToEvent("hgf", t1);
        assertEquals(NOT_FOUND, bad_req.getStatusCode());
    }
}