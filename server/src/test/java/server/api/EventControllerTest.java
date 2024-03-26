//package server.api;
//
//import commons.Event;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Random;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.http.HttpStatus.BAD_REQUEST;
//
//class EventControllerTest {
//
//    public int nextInt;
//    private TestEventRepository repo;
//    private EventController eventc;
//
//    @BeforeEach
//    void setUp() {
//        repo = new TestEventRepository();
//        eventc = new EventController(repo);
//    }
//
//    @Test
//    void getAll() {
//        Event x1 = new Event("Event 1");
//        Event x2 = new Event("Event 2");
//        Event x3 = new Event("Event 3");
//        eventc.save(x1);
//        eventc.save(x2);
//        eventc.save(x3);
//        assertEquals(3,repo.count());
//        assertTrue(repo.calledMethods.contains("count"));
//        var actual = eventc.getAll();
//        assertTrue(repo.calledMethods.contains("findAll"));
//        assertEquals(x1,actual.getFirst());
//    }
//
//    @Test
//    void save() {
//        var actual = eventc.save(new Event(null));
//        assertEquals(BAD_REQUEST, actual.getStatusCode());
//        assertFalse(repo.calledMethods.contains("save"));
//        eventc.save(new Event("Event 1"));
//        assertTrue(repo.calledMethods.contains("save"));
//    }
//
//    @Test
//    void deleteById() {
//        Event x1 = new Event("Event 1");
//        Event x2 = new Event("Event 2");
//        Event x3 = new Event("Event 3");
//        eventc.save(x1);
//        eventc.save(x2);
//        eventc.save(x3);
//        var actual = eventc.deleteById(eventc.getAll().get(0).getId());
//        assertTrue(repo.calledMethods.contains("deleteById"));
//        assertEquals(x1, actual.getBody());
//        assertFalse(repo.events.contains(x1));
//        assertTrue(repo.events.contains(x3));
//    }
//
//    @Test
//    void updateTitle() {
//        Event x1 = new Event("Event 1");
//        Event x2 = new Event("Event 2");
//        Event x3 = new Event("Event 3");
//        eventc.save(x1);
//        eventc.save(x2);
//        eventc.save(x3);
//        var actual = eventc.updateTitle(eventc.getAll().get(0).getId(),"New Event");
//        assertEquals("New Event",eventc.getAll().get(0).getTitle());
//    }
//
//    @SuppressWarnings("serial")
//    public class MyRandom extends Random {
//
//        public boolean wasCalled = false;
//
//        /**
//         * Method for manipulating the next instance in the Random function
//         *
//         * @param bound the upper bound (exclusive).  Must be positive.
//         * @return the next random position
//         */
//        @Override
//        public int nextInt(int bound) {
//            wasCalled = true;
//            return nextInt;
//        }
//    }
//}