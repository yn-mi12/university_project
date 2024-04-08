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
    private Event e;
    @BeforeEach
    void setUp() {
        e = new Event("k");
        eventRepository.saveAndFlush(e);
    }

    @AfterEach
    void tearDown() {
        eventRepository.delete(e);
        eventRepository.flush();
    }

    @Test
    void saveTest() {
        assertDoesNotThrow(() -> eventRepository.saveAndFlush(e));

    }

    @Test
    void noIDTest() {
        e.setId(null);
        assertThrows(Exception.class, () -> eventRepository.saveAndFlush(e));
    }

    @Test
    void badTitleSaveTest() {
        e.setTitle(null);
        assertThrows(DataIntegrityViolationException.class, () -> eventRepository.saveAndFlush(e));
    }

    @Test
    void badDate1() {
        e.setCreationDate(null);
        assertThrows(DataIntegrityViolationException.class, () -> eventRepository.saveAndFlush(e));
    }
    @Test
    void badDate2() {
        e.setLastUpdateDate(null);
        assertThrows(DataIntegrityViolationException.class, () -> eventRepository.saveAndFlush(e));
    }

    @Test
    void findById() {
        Event e2 = eventRepository.findById(e.getId()).orElse(null);
        assertNotNull(e2);
        assertEquals(e.getTitle(), e2.getTitle());
        assertEquals(e.getId(), e2.getId());
        assertEquals(e, e2);
    }

    @Test
    void dontFindById() {
        Event e2 = eventRepository.findById("no").orElse(null);
        assertNull(e2);
    }

}
