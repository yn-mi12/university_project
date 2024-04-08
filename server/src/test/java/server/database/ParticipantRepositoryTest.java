package server.database;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"},
        showSql = false)
class ParticipantRepositoryTest {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    private final Event e1 = new Event("One");
    private final Event e2 = new Event("Two");
    @BeforeEach
    void setUp() {
        eventRepository.save(e1);
        eventRepository.save(e2);
        eventRepository.flush();
    }

    @AfterEach
    void tearDown() {
        participantRepository.deleteAll();
        participantRepository.flush();
        eventRepository.delete(e1);
        eventRepository.delete(e2);
        eventRepository.flush();
    }

    @Test
    void findByEventId() {
        Participant p1 = new Participant("one", "pirm", null, null, null, null);
        Participant p2 = new Participant("two", "antr", null, null, null, null);
        Participant p3 = new Participant("g", "ds", null, null, null, null);
        p1.setEvent(e1);
        p2.setEvent(e1);
        p3.setEvent(e2);
        p1 = participantRepository.saveAndFlush(p1);
        p2 = participantRepository.saveAndFlush(p2);
        p3 = participantRepository.saveAndFlush(p3);

        assertEquals(List.of(p1, p2), participantRepository.findByEventId(e1.getId()));
        assertEquals(List.of(p3), participantRepository.findByEventId(e2.getId()));
        assertEquals(List.of(), participantRepository.findByEventId("noID"));

        participantRepository.delete(p1);
        participantRepository.delete(p2);
        participantRepository.delete(p3);
        participantRepository.flush();
    }

    @Test
    void noEvent() {
        Participant p1 = new Participant("aewda", "Bdwa", null, null, null, null);
        p1.setEvent(e1);
        assertDoesNotThrow( () -> participantRepository.saveAndFlush(p1));
        p1.setEvent(null);
        assertThrows(DataIntegrityViolationException.class, () -> participantRepository.saveAndFlush(p1));
        participantRepository.delete(p1);
        participantRepository.flush();
    }

    @Test
    void noFirstName () {
        Participant p1 = new Participant("agrefw", "Betvrf", null, null, null, null);
        p1.setEvent(e1);
        assertDoesNotThrow( () -> participantRepository.saveAndFlush(p1));
        p1.setFirstName(null);
        assertThrows(DataIntegrityViolationException.class, () -> participantRepository.saveAndFlush(p1));
        participantRepository.delete(p1);
        participantRepository.flush();
    }

    @Test
    void noLastName () {
        Participant p1 = new Participant("abevrfw", "B3refsd", null, null, null, null);
        p1.setEvent(e1);
        assertDoesNotThrow( () -> participantRepository.saveAndFlush(p1));
        p1.setLastName(null);
        assertThrows(DataIntegrityViolationException.class, () -> participantRepository.saveAndFlush(p1));
        participantRepository.delete(p1);
        participantRepository.flush();
    }
}
