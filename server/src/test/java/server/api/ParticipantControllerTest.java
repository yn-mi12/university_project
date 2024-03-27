package server.api;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ParticipantControllerTest {

    private TestParticipantRepository repo;
    private ParticipantController partc;

    private static Participant getParticipant(String a,String b,String c){
        return new Participant(a,b,c);
    }

    @BeforeEach
    void setUp() {
        repo = new TestParticipantRepository();
        partc = new ParticipantController(repo);
    }

    @Test
    void cannotAddNullPerson() {
        var actual = partc.save(getParticipant(null,"a",null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        actual = partc.save(getParticipant("a",null,null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void getAll() {
        partc.save(getParticipant("a","b","c"));
        partc.save(getParticipant("b","c","d"));
        partc.save(getParticipant("c","d","e"));
        partc.save(getParticipant("e","f",null));
        assertEquals(4,repo.count());
        assertTrue(repo.calledMethods.contains("count"));
        List<Participant> actual = partc.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
        assertEquals("a", actual.getFirst().getFirstName());
        assertEquals("b", actual.getFirst().getLastName());
        assertEquals("c", actual.getFirst().getEmail());
        assertEquals("e", actual.getLast().getFirstName());
        assertEquals("f", actual.getLast().getLastName());
        assertEquals(null, actual.getLast().getEmail());
    }

    @Test
    void getById() {
        Participant p1 = partc.save(getParticipant("a","b","c")).getBody();
        var actual = partc.getById(repo.count() + 1);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        Participant find = partc.getById(p1.getId()).getBody();
        assertTrue(repo.calledMethods.contains("findById"));
        assertEquals(p1, find);
    }

    @Test
    void getByEventId() {
        Event event = new Event("Event 1");
        event.setId(1);
        Participant p1 = new Participant("a", "b");
        Participant p2 = new Participant("c", "d");
        p1.setEvent(event);
        p2.setEvent(event);
        Participant saved = partc.save(p1).getBody();
        Participant saved2 = partc.save(p2).getBody();
        List<Participant> participants = new ArrayList<>();
        participants.add(saved);
        participants.add(saved2);
        List<Participant> find = partc.getByEventId(event.getId()).getBody();
        assertTrue(repo.calledMethods.contains("getByEventId"));
        assertEquals(participants, find);
    }

    @Test
    void save() {
        var actual = partc.save(getParticipant(null,null,null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        assertFalse(repo.calledMethods.contains("save"));
        partc.save(getParticipant("a","b",null));
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    void getEmail() {
        Participant p = partc.save(getParticipant("a","b","c")).getBody();
        String email = partc.getEmail(p.getId()).getBody();
        assertTrue(repo.calledMethods.contains("save"));
        assertEquals("c", email);
    }

}