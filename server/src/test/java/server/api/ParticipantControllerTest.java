package server.api;

import commons.Event;
import commons.ExpenseParticipant;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
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
        p1 = new Participant("Tom", "Cruise", null, null, null, null);
        p2 = new Participant("Ben", "Ten", "yo@ko.co", null, null, null);
        p1.setExpenses(new HashSet<>());
        p2.setExpenses(new HashSet<>());
        p2.getExpenses().add(new ExpenseParticipant());
    }

    @Test
    void cannotAddNullPerson() {
        var bad_req = partc.saveToEvent(e.getId(), new Participant(null,"a", null, null, null, null));
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());
        bad_req = partc.saveToEvent(e.getId(), new Participant("A", null, null, null, null, null));
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
        Participant p3 = new Participant("ds","jk", null, null, null, null);
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

    @Test
    void updateParticipant() {
        partc.saveToEvent(e.getId(), p1).getBody();
        partc.saveToEvent(e.getId(), p2).getBody();
        long count = repo.count();
        Participant random = new Participant("John", "Deere","john@deer.co", null, null, null);
        var actual = partc.updateParticipant(p1.getId(), random);
        assertEquals(count, repo.count());
        assertEquals(OK, actual.getStatusCode());
        assertEquals(p1, actual.getBody());
    }

    @Test
    void invalidUpdateParticipantId() {
        partc.saveToEvent(e.getId(), p1).getBody();
        partc.saveToEvent(e.getId(), p2).getBody();
        long count = repo.count();
        Participant random = new Participant("John", "Deere","john@deer.co", null, null, null);

        var actual = partc.updateParticipant(-1L, random);
        assertEquals(count, repo.count());
        assertEquals(NOT_FOUND, actual.getStatusCode());
        assertNotEquals(p1, random);
    }

    @Test
    void invalidUpdateParticipantName() {
        partc.saveToEvent(e.getId(), p1).getBody();
        partc.saveToEvent(e.getId(), p2).getBody();
        long count = repo.count();
        Participant random = new Participant("John", "","john@deer.co", null, null, null);

        var actual = partc.updateParticipant(p1.getId(), random);
        assertEquals(count, repo.count());
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        assertNotEquals(p1, random);
    }

    @Test
    void deleteParticipant() {
        partc.saveToEvent(e.getId(), p1).getBody();
        partc.saveToEvent(e.getId(), p2).getBody();
        long count = repo.count();
        var req = partc.deleteById(p1.getId());
        assertEquals(count-1, repo.count());
        assertEquals(List.of(p2), repo.findAll());
        assertEquals(OK, req.getStatusCode());
    }

    @Test
    void deleteParticipant2() {
        partc.saveToEvent(e.getId(), p1).getBody();
        partc.saveToEvent(e.getId(), p2).getBody();
        long count = repo.count();
        var req = partc.deleteById(-1L);
        assertEquals(count, repo.count());
        assertEquals(List.of(p1, p2), repo.findAll());
        assertEquals(NOT_FOUND, req.getStatusCode());
    }

    @Test
    void deleteParticipant3() {
        partc.saveToEvent(e.getId(), p1).getBody();
        partc.saveToEvent(e.getId(), p2).getBody();
        long count = repo.count();
        var req = partc.deleteById(p2.getId());
        assertEquals(count, repo.count());
        assertEquals(List.of(p1, p2), repo.findAll());
        assertEquals(BAD_REQUEST, req.getStatusCode());
    }
}