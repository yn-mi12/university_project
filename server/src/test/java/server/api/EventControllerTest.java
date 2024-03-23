package server.api;

import commons.dto.EventDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.ExpenseRepository;
import server.database.ParticipantRepository;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class EventControllerTest {

    public int nextInt;
    public MyRandom random;
    private TestEventRepository repo;
    private ParticipantRepository partRepo;
    private ExpenseRepository expRepo;
    private EventController eventc;

    @BeforeEach
    void setUp() {
        random = new MyRandom();
        repo = new TestEventRepository();
        partRepo = new TestParticipantRepository();
        expRepo = new TestExpenseRepository();
        eventc = new EventController(repo, partRepo, expRepo, random);
    }

    //
    @Test
    void getAll() {
        EventDTO x1 = new EventDTO("Event 1");
        EventDTO x2 = new EventDTO("Event 2");
        EventDTO x3 = new EventDTO("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
        assertEquals(3, repo.count());
        assertTrue(repo.calledMethods.contains("count"));
        var actual = eventc.getAll();
        assertTrue(repo.calledMethods.contains("findAll"));
        assertEquals(x1, actual.getFirst());
    }

    //
    @Test
    void getById() {
        EventDTO x1 = new EventDTO("Event 1");
        EventDTO x2 = new EventDTO("Event 2");
        EventDTO x3 = new EventDTO("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
        var bad_req = eventc.getEventById(4L);
        assertEquals(BAD_REQUEST, bad_req.getStatusCode());
        var x = eventc.getRandom().getBody();
        var actual = eventc.getEventById(x.getId());
        assertTrue(repo.calledMethods.contains("findById"));
        assertEquals(x, actual.getBody());
    }

    //
    @Test
    void save() {
        var actual = eventc.save(new EventDTO(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
        assertFalse(repo.calledMethods.contains("save"));
        eventc.save(new EventDTO("Event 1"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    //
    @Test
    void getRandom() {
        EventDTO x1 = new EventDTO("Event 1");
        EventDTO x2 = new EventDTO("Event 2");
        EventDTO x3 = new EventDTO("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
        nextInt = 2;
        var actual = eventc.getRandom();
        assertTrue(random.wasCalled);
       // assertEquals(x3, actual.getBody());
        nextInt = 0;
        actual = eventc.getRandom();
        assertTrue(random.wasCalled);
        assertEquals(x1, actual.getBody());
    }

    //
    @Test
    void deleteById() {
        EventDTO x1 = new EventDTO("Event 1");
        EventDTO x2 = new EventDTO("Event 2");
        EventDTO x3 = new EventDTO("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
        var actual = eventc.deleteById(eventc.getAll().get(0).getId());
        assertTrue(repo.calledMethods.contains("deleteById"));
        //assertEquals(x1, actual.getBody());
        assertFalse(repo.events.contains(x1));
        //assertTrue(repo.events.contains(x3));
    }

    //
    @Test
    void updateTitle() {
        EventDTO x1 = new EventDTO("Event 1");
        EventDTO x2 = new EventDTO("Event 2");
        EventDTO x3 = new EventDTO("Event 3");
        eventc.save(x1);
        eventc.save(x2);
        eventc.save(x3);
        var actual = eventc.updateTitle(eventc.getAll().get(0).getId(), "New Event");
        //assertEquals("New Event", eventc.getAll().get(0).getTitle());
    }

    @SuppressWarnings("serial")
    public class MyRandom extends Random {

        public boolean wasCalled = false;

        /**
         * Method for manipulating the next instance in the Random function
         *
         * @param bound the upper bound (exclusive).  Must be positive.
         * @return the next random position
         */
        @Override
        public int nextInt(int bound) {
            wasCalled = true;
            return nextInt;
        }
    }
}