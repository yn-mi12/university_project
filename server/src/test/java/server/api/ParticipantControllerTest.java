package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

public class ParticipantControllerTest {

    private TestParticipantRepository repo;
    private TestEventRepository eventRepository;
    private ParticipantController partc;
    private Event e;
    private Participant p1;
    private Participant p2;


    @BeforeEach
    void setUp() {
        repo = new TestParticipantRepository();
        eventRepository = new TestEventRepository();
        partc = new ParticipantController(repo, eventRepository);
        e = new Event("event");
        eventRepository.save(e);
        p1 = new Participant("Tom", "Cruise");
        p2 = new Participant("Ben", "Ten", "yo@ko.co");
    }

    @Test
    void cannotAddNullPerson() {
        var bad_req = partc.saveToEvent(e.getId(), new Participant(null,"a"));
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());
        bad_req = partc.saveToEvent(e.getId(), new Participant("A", null));
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());
    }

    @Test
    void noEvent() {
        var bad_req = partc.saveToEvent("lol", p1);
        assertEquals(NOT_FOUND, bad_req.getStatusCode());
    }

    @Test
    void successfulSaveToEvent() {
        var actual1 = partc.saveToEvent(e.getId(), p1);
        p1 = actual1.getBody();
        assertEquals(OK, actual1.getStatusCode());
        assertEquals(p1, actual1.getBody());
    }

    @Test
    void getById() {
        assertEquals(0,repo.count());
        p1 = partc.saveToEvent(e.getId(), p1).getBody();
        p2 = partc.saveToEvent(e.getId(), p2).getBody();
        assertEquals(2,repo.count());
        assertTrue(repo.calledMethods.contains("count"));

        var bad_req = partc.getById(-1L);
        assertEquals(NOT_FOUND, bad_req.getStatusCode());

        var actual = partc.getById(p1.getId());
        assertEquals(OK, actual.getStatusCode());
        assertEquals(p1, actual.getBody());

        actual = partc.getById(p2.getId());
        assertEquals(OK, actual.getStatusCode());
        assertEquals(p2, actual.getBody());
    }

    @Test
    void getByEventId() {
        Event e2 = new Event("2");
        eventRepository.save(e2);
        Participant p3 = new Participant("ds","jk");
        p3.setEvent(e2);
        p3 = partc.saveToEvent(e2.getId(), p3).getBody();
        p1.setEvent(e);
        p2.setEvent(e);
        p1 = partc.saveToEvent(e.getId(), p1).getBody();
        p2 = partc.saveToEvent(e.getId(), p2).getBody();

        var bad_req = partc.getByEventId("");
        assertEquals(NOT_FOUND, bad_req.getStatusCode());

        var actual = partc.getByEventId(e.getId());
        assertTrue(repo.calledMethods.contains("findByEventId"));
        assertEquals(OK, actual.getStatusCode());
        assertEquals(List.of(p1, p2), actual.getBody());

        actual = partc.getByEventId(e2.getId());
        assertTrue(repo.calledMethods.contains("findByEventId"));
        assertEquals(OK, actual.getStatusCode());
        assertEquals(List.of(p3), actual.getBody());
    }
}