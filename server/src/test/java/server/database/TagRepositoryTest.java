package server.database;

import commons.Event;
import commons.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
class TagRepositoryTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TagRepository tagRepository;
    private final Event e1 = new Event("One");
    private final Event e2 = new Event("Two");

    @BeforeEach
    void setUp(){
        eventRepository.save(e1);
        eventRepository.save(e2);
        eventRepository.flush();
    }

    @AfterEach
    void teadrDown(){
        tagRepository.deleteAll();;
        tagRepository.flush();
        eventRepository.delete(e1);
        eventRepository.delete(e2);
        eventRepository.flush();
    }

    @Test
    void findByEventId() {
        Tag tag1 = new Tag("a", "b");
        Tag tag2 = new Tag("c", "d");
        Tag tag3 = new Tag("e", "f");
        tag1.setEvent(e1);
        tag2.setEvent(e2);
        tag3.setEvent(e2);
        tag1 = tagRepository.saveAndFlush(tag1);
        tag2 = tagRepository.saveAndFlush(tag2);
        tag3 = tagRepository.saveAndFlush(tag3);

        // Ensuring that the entities are persisted before querying
        entityManager.clear();

        assertEquals(List.of(tag1), tagRepository.findByEventId(e1.getId()));
        assertEquals(List.of(tag2, tag3), tagRepository.findByEventId(e2.getId()));
        assertEquals(List.of(), tagRepository.findByEventId("noId"));

        // Clean up
        tagRepository.delete(tag1);
        tagRepository.delete(tag2);
        tagRepository.delete(tag3);
        tagRepository.flush();
    }


    @Test
    void noEvent() {
        Tag tag1 = new Tag("a", "b");
        tag1.setEvent(e1);
        assertDoesNotThrow(() -> tagRepository.saveAndFlush(tag1));
        assertThrows(DataIntegrityViolationException.class, () -> {
            tag1.setEvent(null);
            tagRepository.saveAndFlush(tag1);
        });

        tagRepository.delete(tag1);
        tagRepository.flush();
    }

    @Test
    void noLabel(){
        Tag tag1 = new Tag("a", "b");
        tag1.setEvent(e1);
        assertDoesNotThrow(() -> tagRepository.saveAndFlush(tag1));
        tag1.setLabel(null);
        assertThrows(DataIntegrityViolationException.class, () -> tagRepository.saveAndFlush(tag1));
        tagRepository.delete(tag1);
        tagRepository.flush();
    }

    @Test
    void noColor() {
        Tag tag1 = new Tag("a", "b");
        tag1.setEvent(e1);
        assertDoesNotThrow(() -> tagRepository.saveAndFlush(tag1));
        tag1.setColor(null);
        assertDoesNotThrow(() -> tagRepository.saveAndFlush(tag1));
        tagRepository.delete(tag1);
        tagRepository.flush();
    }

}