package server.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import commons.Event;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"},
        showSql = false)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;
    private Event testEvent;

    @BeforeEach
    public void setUp() {
        testEvent = new Event("event_no1");
        eventRepository.save(testEvent);
    }

    @Test
    void badTitleSaveTest() {
        Event noNameEvent = new Event(null);
        assertThrows(DataIntegrityViolationException.class, () -> eventRepository.save(noNameEvent));
        noNameEvent.setTitle("title");
        assertDoesNotThrow( () -> eventRepository.save(noNameEvent));
    }

    @Test
    void badInviteCodeTest() {
        Event noInviteCode = new Event("Event without invite");
        noInviteCode.setInviteCode(null);
        assertThrows(DataIntegrityViolationException.class, () -> eventRepository.save(noInviteCode));
        noInviteCode.setInviteCode("inviteCode");
        assertDoesNotThrow( () -> eventRepository.save(noInviteCode));
    }

    @Test
    void duplicateInviteCodes() {
        Event e2 = new Event("e2");
        e2.setInviteCode(testEvent.getInviteCode());
        assertThrows(DataIntegrityViolationException.class, () -> eventRepository.saveAndFlush(e2));
    }

    @Test
    void findById() {
        Event savedEvent = eventRepository.findById(testEvent.getId()).orElse(null);
        assertNotNull(savedEvent);
        assertEquals(testEvent.getTitle(), savedEvent.getTitle());
        assertEquals(testEvent.getInviteCode(), savedEvent.getInviteCode());
        assertEquals(testEvent, savedEvent);
    }

    @Test
    void existsByInviteCode() {
        Event savedEvent = eventRepository.findById(-1L).orElse(null);
        assertNull(savedEvent);
    }

    @Test
    void testExistsByInviteCode() {
        String inviteCode = testEvent.getInviteCode();
        assertTrue(eventRepository.existsByInviteCode(inviteCode));
        assertFalse(eventRepository.existsByInviteCode(""));
    }

    @Test
    void findByInviteCode() {
        String inviteCode = testEvent.getInviteCode();
        assertTrue(eventRepository.findByInviteCode("NonExistantInviteCode").isEmpty());
        assertTrue(eventRepository.findByInviteCode(inviteCode).isPresent());
        assertEquals(testEvent, eventRepository.findByInviteCode(inviteCode).get());
    }

    @AfterEach
    public void tearDown() {
        eventRepository.delete(testEvent);
    }
}
